package org.antlr.parser.antlr4;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;

public abstract class LexerAdaptor extends Lexer {

    /**
     *  Generic type for OPTIONS, TOKENS and CHANNELS
     */
	public LexerAdaptor(CharStream input) {
		super(input);
	}

	/**
	 * Track whether we are inside of a rule and whether it is lexical parser. _currentRuleType==Token.INVALID_TYPE
	 * means that we are outside of a rule. At the first sign of a rule name reference and _currentRuleType==invalid, we
	 * can assume that we are starting a parser rule. Similarly, seeing a token reference when not already in rule means
	 * starting a token rule. The terminating ';' of a rule, flips this back to invalid type.
	 *
	 * This is not perfect logic but works. For example, "grammar T;" means that we start and stop a lexical rule for
	 * the "T;". Dangerous but works.
	 *
	 * The whole point of this state information is to distinguish between [..arg actions..] and [charsets]. Char sets
	 * can only occur in lexical rules and arg actions cannot occur.
	 */
	private static int PREQUEL_CONSTRUCT = -10;
	private int _currentRuleType = Token.INVALID_TYPE;

	protected void handleBeginArgument()
	{
	    if (inLexerRule()) {
		pushMode(ANTLRv3Lexer.LexerCharSet);
		more();
	    } else {
		pushMode(ANTLRv3Lexer.Argument);
	    }
	}

	protected void handleEndArgument() {
	    popMode();
	    if (_modeStack.size() > 0) {
		setType(ANTLRv3Lexer.ARGUMENT_CONTENT);
	    }
	}

	protected void handleEndAction() {
	    int oldMode = _mode;
	    int newMode = popMode();
	    boolean isActionWithinAction = _modeStack.size() > 0
					   && newMode == ANTLRv3Lexer.Actionx
					   && oldMode == newMode;

	    if (isActionWithinAction) {
		setType(ANTLRv3Lexer.ACTION_CONTENT);
	    }
	}

	protected void handleOptionsLBrace() {
//	    if (insideOptionsBlock) {
//		setType(ANTLRv3Lexer.BEGIN_ACTION);
//		pushMode(ANTLRv3Lexer.Actionx);
//	    } else {
		setType(ANTLRv3Lexer.LBRACE);
//		insideOptionsBlock = true;
//	    }
	}

//	public int getCurrentRuleType() {
//		return _currentRuleType;
//	}

//	public void setCurrentRuleType(int ruleType) {
//		this._currentRuleType = ruleType;
//	}

	@Override
	public Token emit() {
		if ((_type == ANTLRv3Lexer.OPTIONS || _type == ANTLRv3Lexer.TOKENS)
				&& _currentRuleType == Token.INVALID_TYPE) { // enter prequel construct ending with an RBRACE
			_currentRuleType = PREQUEL_CONSTRUCT;
		} else if (_type == ANTLRv3Lexer.RBRACE && _currentRuleType == PREQUEL_CONSTRUCT) { // exit prequel construct
			_currentRuleType = Token.INVALID_TYPE;
		} else if (_type == ANTLRv3Lexer.AT && _currentRuleType == Token.INVALID_TYPE) { // enter action
			_currentRuleType = ANTLRv3Lexer.AT;
		} else if (_type == ANTLRv3Lexer.END_ACTION && _currentRuleType == ANTLRv3Lexer.AT) { // exit action
			_currentRuleType = Token.INVALID_TYPE;
		} else if (_type == ANTLRv3Lexer.ID) {
			String firstChar = _input.getText(Interval.of(_tokenStartCharIndex, _tokenStartCharIndex));
			if (Character.isUpperCase(firstChar.charAt(0))) {
				_type = ANTLRv3Lexer.TOKEN_REF;
			} else {
				_type = ANTLRv3Lexer.RULE_REF;
			}

			if (_currentRuleType == Token.INVALID_TYPE) { // if outside of rule def
				_currentRuleType = _type; // set to inside lexer or parser rule
			}
		} else if (_type == ANTLRv3Lexer.SEMI) { // exit rule def
			_currentRuleType = Token.INVALID_TYPE;
		}

		return super.emit();
	}

	private boolean inLexerRule() {
		return _currentRuleType == ANTLRv3Lexer.TOKEN_REF;
	}

	@SuppressWarnings("unused")
	private boolean inParserRule() { // not used, but added for clarity
		return _currentRuleType == ANTLRv3Lexer.RULE_REF;
	}
}