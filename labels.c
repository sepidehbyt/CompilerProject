  
// function to check even or not 
void checkEvenOrNot(int num) 
{ 
    if (num % 2 == 0) 
        goto even; // jump to even 
    else
        goto odd; // jump to odd 
  
even: 
    printf(" is evenn"); 
    return; // return if even 
odd: 
    printf(" is odd"); 
} 
  
// Driver program to test above function 
int main() 
{ 
    //int num = 26; 
    //checkEvenOrNot(num); 
    int a = 22;
    int b = 20;
    int c = 30;
    int d = 10;
    int e = 0;
    int t;
L5: if(a>b) goto L0;
goto L1;
L0: L4: if(c>d) goto L2;
goto L3;
L2: e = 100; 
b = b + 1;
d = d + 1;
goto L4;
L3: goto L5;
L1:  t=3 ;
printf("%d", e);
    return 0; 
} 