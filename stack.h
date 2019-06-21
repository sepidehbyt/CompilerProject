#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct stack{
	char* data;
	struct stack* next;
};

void push(struct stack** head, char* string);
char* pop(struct stack** head);
void printStack(struct stack** head);
void clearStack(struct stack** head);
char* createStringStack(char* string);

void push(struct stack** head, char* string){
	struct stack* node = (struct stack*) malloc(sizeof(struct stack));
	node->data = createStringStack(string);
	node->next = NULL;
	
	if(*head == NULL){
		*head = node;
	}else{
		struct stack* currentNode = *head;
		while(currentNode->next != NULL) currentNode = currentNode->next;
		currentNode->next = node;
	}
}

char* pop(struct stack** head){
	if(*head == NULL) return NULL;
	
	if((*head)->next == NULL){
		char* value = (*head)->data;
		free(*head);
		*head = NULL;
		return value;
	}
	
	struct stack* node = *head;
	while(node->next->next != NULL) node = node->next;
	char* value = node->next->data;
	free(node->next);
	node->next = NULL;
	return value;
}

void printStack(struct stack** head){
	struct stack* node = *head;
	printf("\n");
	if(*head == NULL) printf("Stack is empty.");
	while(node != NULL){
		printf("%s\n", node->data);
		node = node->next;
	}
}

void clearStack(struct stack** head){
	struct stack* node = *head;
	struct stack* temp;
	while(node != NULL){
		temp = node->next;
		free(node->data);
		free(node);
		node = temp;
	}
	*head = NULL;
}

char* createStringStack(char* string){
	char* pointer = (char*) malloc(strlen(string) + 1);
	strcpy(pointer, string);
	pointer[strlen(string)] = 0;
	return pointer;
}