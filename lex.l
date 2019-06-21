%{
       #include "yacc.tab.h"
       #include <stdlib.h>
       #include <string.h>

       char* tempString;
       
%} 
  
%%

\n {}
\t {}
"And Then" {return AndThen;}
"Or Else" {return OrElse;}
" " {}
"Program" {return Program;}
"Int" {return Int;}
"Real" {return Real;}
"Bool" {return Bool;}
"Procedure" { return Procedure;}
"Function" {return Function;}
"Begin" {return Begin;}
"End" {return End;}
"If" {return If;}
"While" {return While;}
"Do" {return Do;}
"For" {return For;}
"To" {return To;}
"Downto" {return Downto;}
"Case" {return Case;}
"Return" {return Return;}
"Then" {return Then;}
"Else" {return Else;}
"True" {return True;}
"False" {return False;}
"(" {return LEFTP;}
")" {return RIGHTP;}
":" {return COLON;}
";" {return SEMICOLON;}
":=" {return ASSIGNMENT;}
"," {return COMMA;}
".GT." {return GT;}
".GE." {return GE;}
".NE." {return NE;}
".EQ." {return EQ;}
".LT." {return LT;}
".LE." {return LE;}
"+" {return ADD;}
"-" {return SUB;}
"*" {return MUL;}
"/" {return DIV;}
[a-zA-Z_][a-zA-Z0-9_]* { 
       char *res = malloc( (size_t)yytext );
	strcpy(res, yytext);
       yylval.sval = res;
       return IDtoken;}
#0+[0-9]+(\.{0,1})([eE][-+]{0,1}[0-9]+){0,1}(0+[0-9]+){0,1} {printf("Error on : %s\n", yytext);}
#[-+]{0,1}((0|[1-9][0-9]*)(\.(0|[0-9]*[1-9]+)))([eE][-+]{0,1}(0|([1-9][0-9]))){0,1}0+ {printf("Error on : %s\n", yytext);}
#0|#[-+]{0,1}[1-9][0-9]* {
       char *res1 = malloc( (size_t)yytext );
	strcpy(res1, yytext);
	yylval.sval = res1;
       return INTtoken;}
#[-+]{0,1}((0|[1-9][0-9]*)(\.(0|[0-9]*[1-9]+)){0,1})([eE][-+]{0,1}(0|([1-9][0-9]))){0,1} {
       char *res2 = malloc( (size_t)yytext );
	strcpy(res2, yytext);
	yylval.sval = res2;
       return REALtoken;}
[^ \t\n\/+-/(),*:;":="".GT."".GE."".NE."".EQ."".LT."".LE."]+ { printf("Error on : %s\n", yytext);}

%%

int yywrap(){
       return 1;
}