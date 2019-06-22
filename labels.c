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
	MainFunction : scopes = scopes - 1; setValue(scopes,"v1",0);

printf("salam");
L1: if(getValue(scopes,"o0perator")!=0) goto L2;
setValue(scopes,"v1",1);
goto L0;
L2: if(getValue(scopes,"o0perator")!=1) goto L3;
setValue(scopes,"v1",2);
goto L0;
L3: if(getValue(scopes,"o0perator")!=2) goto L4;
setValue(scopes,"v1",3);
goto L0;
L4: if(getValue(scopes,"o0perator")!=3) goto L5;
setValue(scopes,"v1",4);
goto L0;
L5: if(getValue(scopes,"o0perator")!=4) goto L6;
setValue(scopes,"v1",5);
goto L0;
L6: L0: 
printf("salam");
printf("salam %d", getValue(scopes, "v1"));
return 0;}
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