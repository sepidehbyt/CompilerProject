#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct variable{
	char* id;
	double value;
	struct variable* next;
};

void setValue(struct variable** scope, char* id, double value);
double getValue(struct variable** scope, char* id);
char* createString(char* string);

int main(){
	void* returnAddress;
double* top = (double*) malloc(1000 * sizeof(double));
	void** rtop = (void**) malloc(1000 * sizeof(void*));
	struct variable** scopes = (struct variable**) malloc(100 * sizeof(struct variable*));
	
	top += 1000;
	rtop += 1000;
	scopes += 100;	goto MainFunction; 
MainFunction : scopes = scopes - 1; setValue(scopes,"T0",10/2);
setValue(scopes,"T1",20-getValue(scopes,"T0"));
setValue(scopes,"n1",getValue(scopes,"T1"));
setValue(scopes,"T2",3+2);
setValue(scopes,"T3",getValue(scopes,"n1")/getValue(scopes,"T2"));
setValue(scopes,"n2",getValue(scopes,"T3"));
setValue(scopes,"T4",getValue(scopes,"n1")*getValue(scopes,"n2"));
setValue(scopes,"T5",getValue(scopes,"T4")-10);
setValue(scopes,"n3",getValue(scopes,"T5"));
if(getValue(scopes,"n1")>2) goto L0;
goto L1;
L0: L4: if(getValue(scopes,"n2")>0) goto L2;
goto L3;
L2: setValue(scopes,"T6",getValue(scopes,"n3")+1);
setValue(scopes,"n3",getValue(scopes,"T6"));
setValue(scopes,"T7",getValue(scopes,"n2")-1);
setValue(scopes,"n2",getValue(scopes,"T7"));
goto L4;L3: 
 L1: return 0;}
void setValue(struct variable** scope, char* id, double value){
	if(*scope == NULL){
		struct variable* newVar = (struct variable*) malloc(sizeof(struct variable));
		newVar->id = createString(id);
		newVar->value = value;
		newVar->next = NULL;
		*scope = newVar;
	}else{
		struct variable* node = *scope;
		while(node->next != NULL){
			if(strcmp(node->id, id) == 0){
				node->value = value;
				return;
			}
			node = node->next;
		}
		if(strcmp(node->id, id) == 0){
			node->value = value;
			return;
		}
		struct variable* newVar = (struct variable*) malloc(sizeof(struct variable));
		newVar->id = createString(id);
		newVar->value = value;
		newVar->next = NULL;
		node->next = newVar;
	}
}

double getValue(struct variable** scope, char* id){
	while(1){
		struct variable* node = *scope;
		while(node != NULL){
			if(strcmp(node->id, id) == 0){
				return node->value;
			}
			node = node->next;
		}
		scope = scope + 1;
	}
}

char* createString(char* string){
	char* pointer = (char*) malloc(strlen(string) + 1);
	strcpy(pointer, string);
	pointer[strlen(string)] = 0;
	return pointer;
}