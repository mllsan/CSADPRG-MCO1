// ***********************************
// Last names: Santos
// Language: C 
// Paradigm(s): Procedural Programming
// ***********************************

/* preprocessor directives */
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <ctype.h>

/* definitions */
typedef struct AccountDetails {
    char AccountName[100];
    float Balance;
    char Currency[4];
    int InterestRate;
} Account;

typedef struct CurrencyDetails {
    char Name[100];
    char Code[4];
    float ExchangeRate[6];
} Currencies;

/* helper functions */
void getCurrencyDetails (Currencies Currencies[]){
    strcpy(Currencies[0].Name, "Philippine Peso");
    strcpy(Currencies[0].Code, "PHP");
    Currencies[0].ExchangeRate[0] = 1.00;   // php
    Currencies[0].ExchangeRate[1] = 0.016;  // usd
    Currencies[0].ExchangeRate[2] = 2.52;   // jpy
    Currencies[0].ExchangeRate[3] = 0.012;  // gbp
    Currencies[0].ExchangeRate[4] = 0.014;  // eur
    Currencies[0].ExchangeRate[5] = 0.11;   // cny

    strcpy(Currencies[1].Name, "United States Dollar");
    strcpy(Currencies[1].Code, "USD");
    Currencies[1].ExchangeRate[0] = 62.00;
    Currencies[1].ExchangeRate[1] = 1.00;
    Currencies[1].ExchangeRate[2] = 158.32;
    Currencies[1].ExchangeRate[3] = 0.76;
    Currencies[1].ExchangeRate[4] = 0.90;
    Currencies[1].ExchangeRate[5] = 6.69;

    strcpy(Currencies[2].Name, "Japanese Yen");
    strcpy(Currencies[2].Code, "JPY");
    Currencies[2].ExchangeRate[0] = 0.40;
    Currencies[2].ExchangeRate[1] = 0.0063;
    Currencies[2].ExchangeRate[2] = 1.00;
    Currencies[2].ExchangeRate[3] = 0.0048;
    Currencies[2].ExchangeRate[4] = 0.0057;
    Currencies[2].ExchangeRate[5] = 0.042;

    strcpy(Currencies[3].Name, "British Pound Sterling");
    strcpy(Currencies[3].Code, "GBP");
    Currencies[3].ExchangeRate[0] = 82.91;
    Currencies[3].ExchangeRate[1] = 1.32;
    Currencies[3].ExchangeRate[2] = 208.70;
    Currencies[3].ExchangeRate[3] = 1.00;
    Currencies[3].ExchangeRate[4] = 1.18;
    Currencies[3].ExchangeRate[5] = 8.82;

    strcpy(Currencies[4].Name, "Euro");
    strcpy(Currencies[4].Code, "EUR");
    Currencies[4].ExchangeRate[0] = 70.12;
    Currencies[4].ExchangeRate[1] = 1.11;
    Currencies[4].ExchangeRate[2] = 176.54;
    Currencies[4].ExchangeRate[3] = 0.85;
    Currencies[4].ExchangeRate[4] = 1.00;
    Currencies[4].ExchangeRate[5] = 7.46;

    strcpy(Currencies[5].Name, "Chinese Yuan Renminni");
    strcpy(Currencies[5].Code, "CNY");
    Currencies[5].ExchangeRate[0] = 9.40;
    Currencies[5].ExchangeRate[1] = 0.15;
    Currencies[5].ExchangeRate[2] = 23.67;
    Currencies[5].ExchangeRate[3] = 0.11;
    Currencies[5].ExchangeRate[4] = 0.13;
    Currencies[5].ExchangeRate[5] = 1.00;
}

int getValidInput(int min, int max) {
    int num;

    while (1) {
        if (scanf("%d", &num) != 1) {
            while (getchar() != '\n'); 
            printf("Error: Please enter a valid integer.\n");
            printf("Choice: ");
        } 
        else if (num < min || num > max) {
            printf("Error: Please enter a number between %d and %d.\n", min, max);
            printf("Choice: ");
        } 
        else {
            return num;
        }
    }
}

int getValidName(char *str) {
    int Comma = 0, beforeComma = 0, afterComma = 0, valid = 1;

    int len = strlen(str);
    if (len > 0 && str[len - 1] == '\n') {
        str[len - 1] = '\0';
        len--; 
    }

    if (len == 0) 
        return 0; 

    for (int i = 0; i < len; i++) {
        char c = str[i];

        if (isdigit(c)) {
            return 0; 
        }
        if (c == ',') {
            Comma++;
        } 
        else if (Comma == 0) {
            if (isalpha(c) || c == ' ' || c == '-') {
                beforeComma++;
            } 
            else {
                valid = 0; 
            }
        } 
        else {
            if (isalpha(c) || c == ' ' || c == '-') {
                afterComma++;
            } 
            else {
                valid = 0;
            }
        }
    }

    if (Comma != 1 || beforeComma == 0 || afterComma == 0) {
        valid = 0;
    }

    return valid;
}

/* function implementations */
void MainMenu(int *choice){
    printf("Select Transaction: \n");
    printf("[1] Register Account Name\n");
    printf("[2] Deposit Amount\n");
    printf("[3] Withdraw Amount\n");
    printf("[4] Currency Exchange\n");
    printf("[5] Record Exchange Rates\n");
    printf("[6] Show Interest Amount\n");

    printf("\n");
    printf("Choice: ");
    *choice = getValidInput(1, 6);
}

void registerAccount(Account RegisteredAcc[], int *activeAccs){
    char Name[100];
    int c;
    int valid = 0;

    printf("Register Account Name\n");

    printf("Format: Last Name, First Name\n");
    while ((c = getchar()) != '\n' && c != EOF);
    while(valid==0){
        printf("Account Name: ");
        if (fgets(Name, sizeof(Name), stdin) != NULL) {
                if (getValidName(Name)) {
                    strcpy(RegisteredAcc[*activeAccs].AccountName, Name);
                    valid = 1;
                } 
                else {
                    printf("Error: Invalid format\n\n");
                }
            }
    }

    (*activeAccs)++;
}

void depositAmount(Account RegisteredAcc[], int activeAccs){
    float amount = 0.0;
    int approved = 0, index = 0, validAcc = 0;
    char AccName[100];

    printf("Deposit Amount\n");
    printf("Account Name: ");
    scanf(" %[^\n]", AccName);
    while(validAcc == 0){
        for(int i = 0; i<activeAccs; i++){
            if(strcmp(AccName, RegisteredAcc[i].AccountName) == 0){
                index = i;
                validAcc = 1;
            }
        }
        if(validAcc == 0){
                printf("Error: Please enter a valid account\n\n");
                printf("Account Name: ");
                scanf(" %[^\n]", AccName);
        }
    }

    printf("Current Balance: %.2f\n", RegisteredAcc[index].Balance);
    printf("Currency: %s\n", RegisteredAcc[index].Currency);

    printf("\n");
    while(approved==0){
        printf("Deposit Amount: ");
        scanf("%f", &amount);
        if(amount<=0)
            printf("Error: Please enter a valid amount\n");
        else
            approved = 1;
    }
    RegisteredAcc[index].Balance += amount;
    printf("Updated Balance: %.2f\n", RegisteredAcc[index].Balance);
}

void withdrawAmount(Account RegisteredAcc[], int activeAccs){
    float amount = 0.0;
    int approved = 0, index = 0, validAcc = 0;
    char AccName[100];

    printf("Withdraw Amount\n");
    printf("Account Name: ");
    scanf(" %[^\n]", AccName);
    while(validAcc == 0){
        for(int i = 0; i<activeAccs; i++){
            if(strcmp(AccName, RegisteredAcc[i].AccountName) == 0){
                index = i;
                validAcc = 1;
            }
        }
        if(validAcc == 0){
                printf("Error: Please enter a valid account\n\n");
                printf("Account Name: ");
                scanf(" %[^\n]", AccName);
        }
    }

    printf("Current Balance: %.2f\n", RegisteredAcc[index].Balance);
    printf("Currency: %s\n", RegisteredAcc[index].Currency);

    printf("\n");
    while(approved==0){
        printf("Withdraw Amount: ");
        scanf("%f", &amount);
        if(amount<=0)
            printf("Error: Please enter a valid amount\n");
        else if(amount<=RegisteredAcc[index].Balance)
            approved = 1;
        else
            printf("Error: Please enter an amount within the balance\n");
    }
    RegisteredAcc[index].Balance -= amount;
    printf("Updated Balance: %.2f\n", RegisteredAcc[index].Balance);
}

void displayExchangeRate(Currencies Currencies[]){
    int approved = 0, validChoice = 0, sourceChoice = 0, foreignChoice = 0;
    float rate = 0.0;

    printf("Record Exchange Rate\n\n");
    printf("Source Currency Option: ");
    printf("\n");
    for (int i = 1; i<=6; i++){
        printf("[%d] %s (%s)\n", i, Currencies[i-1].Name, Currencies[i-1].Code);
    }
    printf("\n");

    while (getchar() != '\n');
    while (validChoice == 0) {
        printf("Source Currency: ");
        if (scanf("[%d]", &sourceChoice) == 1) {
            if (sourceChoice >= 1 && sourceChoice <= 6) {
                validChoice = 1;
            }
        }
        while (getchar() != '\n');
        if (validChoice == 0) {
            printf("Error: Please enter a valid rate in brackets\n\n");
        }
    }

    printf("\nForeign Currency Option: \n");
        for (int i = 1; i<=6; i++){
            printf("[%d] %s (%s)\n", i, Currencies[i-1].Name, Currencies[i-1].Code);
        }
        validChoice = 0;
        while (validChoice == 0) {
            printf("\n");
            printf("Foreign Currency: ");
            if (scanf("[%d]", &foreignChoice) == 1) {
                if (foreignChoice >= 1 && foreignChoice <= 6) {
                    validChoice = 1;
                }
            }
            while (getchar() != '\n');
            if (validChoice == 0) {
                printf("Error: Please enter a valid rate in brackets\n\n");
            }
        }

    while(approved==0){
        printf("Exchange Rate: ");
        scanf("%f", &rate);
        if(rate<=0)
            printf("Error: Please enter a valid rate\n");
        else
            approved = 1;
    }

    Currencies[sourceChoice-1].ExchangeRate[foreignChoice-1] = rate;
}

void currencyConversion(Currencies Currencies[]){
    double convert = 0.0, sourceAmt = 0.0;
    int approved = 0, sourceChoice = 0, exchangeChoice = 0, convertAgain = 1, error = 1;
    char ch;

    while(convertAgain==1){
        printf("Foreign Currency Exchange\n");
        printf("Source Currency Option:\n");
        for (int i = 1; i<=6; i++){
            printf("[%d] %s (%s)\n", i, Currencies[i-1].Name, Currencies[i-1].Code);
        }

        printf("\n");
        printf("Source Currency: ");
        sourceChoice = getValidInput(1, 6);
        while(approved==0){
            printf("Source Amount: ");
            scanf("%lf", &sourceAmt);
            if(sourceAmt<=0)
                printf("Error: Please enter a valid amount\n");
            else
                approved = 1;
        }

        printf("\n");
        printf("Exchanged Currency Options:\n");
        for (int i = 1; i<=6; i++){
            printf("[%d] %s (%s)\n", i, Currencies[i-1].Name, Currencies[i-1].Code);
        }

        printf("\n");
        printf("Exchange Currency: ");
        exchangeChoice = getValidInput(1, 6);
        convert = sourceAmt*Currencies[sourceChoice-1].ExchangeRate[exchangeChoice-1];
        printf("Exchange Amount: %.2f\n\n", convert);

        while(error == 1){
            printf("Convert another currency (Y/N)? ");
            scanf(" %c", &ch);
                if (ch == 'N'){
                    convertAgain = 0;
                    error = 0;
                }
                else if (ch=='Y'){
                    convertAgain = 1;
                    error = 0;
                }
                else{
                    printf("Error: Please enter a valid input\n\n");
                }
        }
    }
}

void showInterestAmount(Account RegisteredAcc[], int activeAccs){
    int approved = 0, numberDays = 0, index = 0, validAcc = 0;;
    float interest = 0.0;
    char AccName[100];
    double roundedInterest;

    printf("Show Interest Amount\n");
    printf("Account Name: ");
    scanf(" %[^\n]", AccName);
    while(validAcc == 0){
        for(int i = 0; i<activeAccs; i++){
            if(strcmp(AccName, RegisteredAcc[i].AccountName) == 0){
                index = i;
                validAcc = 1;
            }
        }
        if(validAcc == 0){
                printf("Error: Please enter a valid account\n\n");
                printf("Account Name: ");
                scanf(" %[^\n]", AccName);
        }
    }

    printf("Current Balance: %.2f\n", RegisteredAcc[index].Balance);
    printf("Currency: %s\n", RegisteredAcc[index].Currency);
    printf("Interest Rate: %d%%\n", RegisteredAcc[index].InterestRate);
    printf("\n");

    while(approved==0){
        printf("Total Number of Days: ");
        scanf("%d", &numberDays);
        if(numberDays<=0)
            printf("Error: Please enter a valid number\n");
        else
            approved = 1;
    }

    interest = RegisteredAcc[index].Balance * ((RegisteredAcc[index].InterestRate/100.0)/365);
    roundedInterest = round(interest * 100.0) / 100.0;

    printf("Day | Interest | Balance |\n");
    for(int i = 1; i<=numberDays; i++){
        RegisteredAcc[index].Balance += interest;
        printf("%d   |   %.2f   | %.2f |\n", i, roundedInterest, RegisteredAcc[index].Balance);
    }

}

int main(){
    int choice = 0, systemActive = 1, activeAccs = 0;
    char ch;
    Account RegisteredAcc[100];
    for(int i = 0; i<100; i++){
        strcpy(RegisteredAcc[i].AccountName, "");
        RegisteredAcc[i].Balance = 1000;
        strcpy(RegisteredAcc[i].Currency, "PHP");
        RegisteredAcc[i].InterestRate = 5;
    }
    Currencies Currencies[6];
    getCurrencyDetails(Currencies);

    while (systemActive == 1){
        MainMenu(&choice);
        int backToMenu = 1;
        if (choice == 1){
            printf("\n");
            registerAccount(RegisteredAcc, &activeAccs);
            printf("\n");
        }
        else if (choice == 2){
            printf("\n");
            depositAmount(RegisteredAcc, activeAccs);
            printf("\n");
        }
        else if (choice == 3){
            printf("\n");
            withdrawAmount(RegisteredAcc, activeAccs);
            printf("\n");
        }
        else if (choice == 4){
            printf("\n");
            currencyConversion(Currencies);
            printf("\n");
        }
        else if (choice == 5){
            printf("\n");
            displayExchangeRate(Currencies);
            printf("\n");
        }
        else if (choice == 6){
            printf("\n");
            showInterestAmount(RegisteredAcc, activeAccs);
            printf("\n");
        }
        
        while(backToMenu==1){
            printf("Back to the Main Menu (Y/N): ");
            scanf(" %c", &ch);

            if (ch == 'N'){
                printf("Exiting the system...\n");
                systemActive = 0;
                backToMenu = 0;
            }
            else if (ch=='Y'){
                backToMenu = 0;
            }
            else{
                printf("Error: Please enter a valid input\n\n");
            }
        }
        printf("\n");
    }
}
