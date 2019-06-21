%{
#include<stdio.h>
#include "stack.h"

extern FILE *yyin;
FILE *fout;
extern int yylineno;
extern char* yytext;
int yylex();
void yyerror(char* error);
%}


%union {
	char *sval;
	char *temp;
}

%start program

%token Real  Int  RIGHTP  LEFTP  SEMICOLON  ASSIGNMENT  COLON  COMMA  If
%token Procedure  Function Begin  End  True False While  Do  For  To  Downto
%token IDtoken  Bool  INTtoken REALtoken Program Case Return 

%nonassoc Then
%nonassoc Else

%left OrElse
%left AndThen 
%left LT  GT  LE  GE  EQ  NE
%left ADD SUB
%left MUL DIV


%%

program : Program IDtoken SEMICOLON declist block SEMICOLON {fprintf(fout, "Program IDtoken SEMICOLON declist block SEMICOLON -> program yytext = %s \n",  yytext);}
	| Program IDtoken SEMICOLON block SEMICOLON {fprintf(fout, "Program IDtoken SEMICOLON block SEMICOLON -> program yytext = %s \n",  yytext);}
declist : dec {fprintf(fout, "declist -> dec yytext = %s \n",  yytext);}
	| declist dec {fprintf(fout, "declist dec -> declist yytext = %s \n",  yytext);}
dec : vardec {fprintf(fout, "vardec -> dec yytext = %s \n",  yytext);}
	| procdec {fprintf(fout, "procdec -> dec yytext = %s \n",  yytext);}
	| funcdec {fprintf(fout, "funcdec -> dec yytext = %s \n",  yytext);} 
type : Int {fprintf(fout, "Int -> type yytext = %s \n",  yytext);}
 	| Real {fprintf(fout, "Real -> type yytext = %s \n",  yytext);} 
	| Bool {fprintf(fout, "Bool -> type yytext = %s \n",  yytext);}
iddec : IDtoken {fprintf(fout, "IDtoken -> iddec yytext = %s \n", yylval.sval);} 
	| IDtoken ASSIGNMENT exp {fprintf(fout, "iddec -> IDtoken ASSIGNMENT exp yytext = %s \n",  yytext);}
idlist : iddec {fprintf(fout, "iddec -> idlist yytext = %s \n",  yytext);} 
	| idlist COMMA iddec {fprintf(fout, "idlist -> idlist COMMA iddec yytext = %s \n",  yytext);} 
vardec : type idlist SEMICOLON {fprintf(fout, "vardec -> type idlist SEMICOLON yytext = %s \n",  yytext);}
procdec : Procedure IDtoken LEFTP paramdecs RIGHTP declist block SEMICOLON {fprintf(fout, "Procedure IDtoken LEFTP paramdecs RIGHTP declist block SEMICOLON -> procdec yytext = %s \n",  yytext);}
	| Procedure IDtoken LEFTP paramdecs RIGHTP block SEMICOLON {fprintf(fout, "Procedure IDtoken LEFTP paramdecs RIGHTP block SEMICOLON -> procdec yytext = %s \n",  yytext);}
funcdec : Function funcValue LEFTP paramdecs RIGHTP COLON type declist block SEMICOLON {fprintf(fout, "Function funcValue LEFTP paramdecs RIGHTP COLON type declist block SEMICOLON ->  funcdec yytext = %s \n",  yytext);}
	| Function funcValue LEFTP paramdecs RIGHTP COLON type block SEMICOLON {fprintf(fout, "Function funcValue LEFTP paramdecs RIGHTP COLON type block SEMICOLON ->  funcdec yytext = %s \n",  yytext);}
paramdecs : paramdec {fprintf(fout, "paramdec -> paramdecs yytext = %s \n",  yytext);} 
	| paramdecs SEMICOLON paramdec  {fprintf(fout, "paramdecs -> paramdecs SEMICOLON paramdec yytext = %s \n",  yytext);} 
paramdec : type paramlist {fprintf(fout, "paramdec -> type paramlist yytext = %s \n",  yytext);} 
paramlist : IDtoken {fprintf(fout, "IDtoken -> paramlist yytext = %s \n",  yytext);}  
	| paramlist COMMA IDtoken {fprintf(fout, "paramlist COMMA IDtoken -> paramlist yytext = %s \n",  yytext);} 
block : Begin stmtlist End {fprintf(fout, "Begin stmtlist End -> block yytext = %s \n",  yytext);}  
	| stmt {fprintf(fout, "stmt -> block yytext = %s \n",  yytext);} 
stmtlist : stmt {fprintf(fout, "stmt -> stmtlist yytext = %s \n",  yytext);} 
	| stmtlist SEMICOLON stmt {fprintf(fout, "stmtlist SEMICOLON stmt -> stmtlist yytext = %s \n",  yytext);} 
lvalue : IDtoken  {fprintf(fout, "IDtoken -> lvalue yytext = %s \n",  yylval.sval);} 
caseValue : INTtoken  {fprintf(fout, "INTtoken -> caseValue yytext = %s \n",  yylval.sval);} 
funcValue : IDtoken  {fprintf(fout, "IDtoken -> funcValue yytext = %s \n",  yylval.sval);}
funcCallValue : IDtoken  {fprintf(fout, "IDtoken -> funcCallValue yytext = %s \n",  yylval.sval);}
stmt : lvalue ASSIGNMENT exp {fprintf(fout, "lvalue ASSIGNMENT exp -> stmt yytext = %s \n",  yylval.sval);} 
	| If exp Then block {fprintf(fout, "If exp Then block -> stmt yytext = %s \n",  yytext);} 
	| If exp Then block Else block {fprintf(fout, "If exp Then block Else block -> stmt yytext = %s \n",  yytext);} 
	| While exp Do block {fprintf(fout, "While exp Do block -> stmt yytext = %s \n",  yytext);} 
	| For lvalue ASSIGNMENT exp To exp Do block {fprintf(fout, "For IDtoken ASSIGNMENT exp To exp Do block -> stmt yytext = %s \n",  yytext);} 
	| For lvalue ASSIGNMENT exp Downto exp Do block {fprintf(fout, "For IDtoken ASSIGNMENT exp Downto exp Do block -> stmt yytext = %s \n",  yytext);} 
	| Case exp COLON caseelement End {fprintf(fout, "Case exp caseelement End -> stmt yytext = %s \n",  yytext);} 
	| Return exp {fprintf(fout, "Return exp -> stmt yytext = %s \n",  yytext);} 
	| exp {fprintf(fout, "exp -> stmt yytext = %s \n",  yytext);} 
exp : exp AndThen exp {fprintf(fout, "exp AndThen exp -> exp yytext = %s \n",  yytext);}  
	| exp OrElse exp {fprintf(fout, "exp OrElse exp -> exp yytext = %s \n",  yytext);} 
	| exp ADD exp {fprintf(fout, "exp ADD exp -> exp yytext = %s \n",  yytext);} 
	| exp SUB exp {fprintf(fout, "exp SUB exp -> exp yytext = %s \n",  yytext);} 
	| exp MUL exp {fprintf(fout, "exp MUL exp -> exp yytext = %s \n",  yytext);} 
	| exp DIV exp {fprintf(fout, "exp DIV exp -> exp yytext = %s \n",  yytext);} 
	| LEFTP exp RIGHTP {fprintf(fout, "LEFTP exp RIGHTP -> exp yytext = %s \n",  yytext);} 
	| exp GT exp {fprintf(fout, "exp GT exp -> exp yytext = %s \n",  yytext);} 
	| exp GE exp {fprintf(fout, "exp GE exp -> exp yytext = %s \n",  yytext);} 
	| exp NE exp {fprintf(fout, "exp NE exp -> exp yytext = %s \n",  yytext);} 
	| exp EQ exp {fprintf(fout, "exp EQ exp -> exp yytext = %s \n",  yytext);} 
	| exp LT exp {fprintf(fout, "exp LT exp -> exp yytext = %s \n",  yytext);} 
	| exp LE exp {fprintf(fout, "exp LE exp -> exp yytext = %s \n",  yytext);} 
	| INTtoken {fprintf(fout, "INTtoken -> exp yytext = %s \n",  yytext);} 
	| REALtoken {fprintf(fout, "REALtoken -> exp yytext = %s \n",  yytext);} 
	| True {fprintf(fout, "True -> exp yytext = %s \n",  yytext);} 
	| False {fprintf(fout, "False -> exp yytext = %s \n",  yytext);} 
	| lvalue {fprintf(fout, "lvalue -> exp yytext = %s \n",  yylval.sval);}
	| funcCallValue LEFTP explist RIGHTP {fprintf(fout, "funcCallValue LEFTP explist RIGHTP -> exp yytext = %s \n",  yytext);} 
caseelement : caseValue COLON block SEMICOLON {fprintf(fout, "caseValue COLON block SEMICOLON -> caseelement yytext = %s \n",  yytext);} 
	| caseelement caseValue COLON block SEMICOLON {fprintf(fout, "caseelement caseValue COLON block SEMICOLON -> caseelement yytext = %s \n",  yytext);} 
explist : exp {fprintf(fout, "exp -> explist yytext = %s \n",  yytext);} 
	| explist COMMA exp {fprintf(fout, "explist COMMA exp -> explist yytext = %s \n",  yytext);} 
			
%%

int main(){
    yyin = fopen("input.txt", "r");
	fout = fopen("output.txt", "w");
	yyparse();
	return 0;
}

void yyerror(char *s) {
	fprintf(fout, "**Error: Line %d near token '%s' --> Message: %s **\n", yylineno,yytext, s);
}