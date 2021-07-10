/*
BSD License

Copyright (c) 2021, Tom Everett
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. Neither the name of Tom Everett nor the names of its contributors
   may be used to endorse or promote products derived from this software
   without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

// https://en.wikipedia.org/wiki/Mathematical_operators_and_symbols_in_Unicode

grammar ctl;

proposition
   : CTL_DOWNTACK
   | CTL_UPTACK
   | 'p'
   | '(' CTL_NOT CTL_PROPOSITION ')'
   | '(' CTL_PROPOSITION CTL_AND CTL_PROPOSITION ')'
   | '(' CTL_PROPOSITION CTL_OR CTL_PROPOSITION ')'
   | '(' CTL_PROPOSITION CTL_RIGHTWARDS_DOUBLE_ARROW CTL_PROPOSITION ')'
   | '(' CTL_PROPOSITION CTL_LEFT_RIGHT_DOUBLE_ARROW CTL_PROPOSITION ')'
   | 'A' 'X' CTL_PROPOSITION
   | 'E' 'X' CTL_PROPOSITION
   | 'A' 'X' CTL_PROPOSITION
   | 'A' 'F' CTL_PROPOSITION
   | 'A' 'G' CTL_PROPOSITION
   | 'E' 'G' CTL_PROPOSITION
   | 'A' '[' CTL_PROPOSITION 'U' CTL_PROPOSITION ']'
   | 'E' '[' CTL_PROPOSITION 'U' CTL_PROPOSITION ']'
   ;

CTL_PROPOSITION
   : '\u2205'
   ;

CTL_LEFT_RIGHT_DOUBLE_ARROW
   : '\u21d4'
   ;
   //CTL_LEFTWARDS_DOUBLE_ARROW: '\u021d0';
   
CTL_RIGHTWARDS_DOUBLE_ARROW
   : '\u021d2'
   ;

CTL_AND
   : '\u2227'
   ;

CTL_OR
   : '\u2228'
   ;

CTL_DOWNTACK
   : '\u22a4'
   ;

CTL_UPTACK
   : '\u22a5'
   ;

CTL_NOT
   : '\u2310'
   ;

WS
   : [ \r\n\t]+ -> skip
   ;

