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

/**
* Stores the user's account information
*/
typedef struct AccountDetails {
    char AccountName[100];
    float Balance;
    char Currency[4];
    int InterestRate;
} Account;

/**
* Stores the six different types of currency
*/
typedef struct CurrencyDetails {
    char Name[100];
    char Code[4];
    float Rate;
} Currencies;

/* helper functions */

/**
 * This function labels and initializes the different currencies available
 * @param Currencies is the array that stores information about the currencies
 **/
void getCurrencyDetails (Currencies Currencies[]){
    strcpy(Currencies[0].Name, "Philippine Peso");
    strcpy(Currencies[0].Code, "PHP");
    Currencies[0].Rate = 1.00;

    strcpy(Currencies[1].Name, "United States Dollar");
    strcpy(Currencies[1].Code, "USD");
    Currencies[1].Rate = 62.00;

    strcpy(Currencies[2].Name, "Japanese Yen");
    strcpy(Currencies[2].Code, "JPY");
    Currencies[2].Rate = 0.40;

    strcpy(Currencies[3].Name, "British Pound Sterling");
    strcpy(Currencies[3].Code, "GBP");
    Currencies[3].Rate = 84.00;

    strcpy(Currencies[4].Name, "Euro");
    strcpy(Currencies[4].Code, "EUR");
    Currencies[4].Rate = 72.00;

    strcpy(Currencies[5].Name, "Chinese Yuan Renminni");
    strcpy(Currencies[5].Code, "CNY");
    Currencies[5].Rate = 9.00;
}

/**
 * This function asks for user input that is within the accepted range
 * @param min is the minimum value the user can input
 * @param max is the maximum value the user can input
**/
int getValidInput(int min, int max) {
    int num;

    while (1) {
        if (scanf("%d", &num) != 1) {
            while (getchar() != '\n'); 
            printf("Error: Please enter a valid integer.\n");
            printf("Choice: ");
        } 
        else if (num < min || num > max) {
            printf("Error: Invalid Input. Please enter a number between %d and %d.\n", min, max);
            printf("Choice: ");
        } 
        else {
            return num;
        }
    }
}

/**
 * This function checks whether the string follows the correct name format or not
 * @param str is the string that will be examined
**/
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

/**
 * This function displays the main menu interface and gets the player's choice 
 * @param choice is the user's inputted choice from the menu
**/
void MainMenu(int *choice){
    printf("\nSelect Transaction: \n");
    printf("[1] Register Account Name\n");
    printf("[2] Deposit Amount\n");
    printf("[3] Withdraw Amount\n");
    printf("[4] Currency Exchange\n");
    printf("[5] Record Exchange Rates\n");
    printf("[6] Show Interest Amount\n");

    printf("\n");
    printf("Choice: ");
    *choice = getValidInput(1, 6);

    printf("\n***\n");
    printf("Choice = %d\n", *choice);
}

/**
 * This function registers a new account and asks the user to input an account name with a specific format 
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
**/
void registerAccount(Account RegisteredAcc[], int *activeAccs){
    char Name[100];
    int c;
    int valid = 0, sameName = 0;

    printf("Register Account Name\n");
    while ((c = getchar()) != '\n' && c != EOF);
    while(valid==0 || sameName==1){
        sameName = 0;
        printf("Account Name: ");
        if (fgets(Name, sizeof(Name), stdin) != NULL) {
                if (getValidName(Name)) {
                    strcpy(RegisteredAcc[*activeAccs].AccountName, Name);
                    valid = 1;
                } 
                else {
                    printf("Error: Invalid account name. Please follow the format 'lastname, firstname' with letters only.\n\n");
                }
            }
        for(int i=0; i<*activeAccs; i++){
            if(strcmp(RegisteredAcc[i].AccountName, Name)==0){
                sameName = 1;
                printf("Error: Account already registered. Please enter a different name.\n\n");
            }
        }
    }

    (*activeAccs)++;

    printf("\n***\n");
    printf("Account Name = %s\n", Name);
}

/**
 * This function updates the balance in the user's account by adding the inputted deposit amount
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
**/
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
        if(validAcc == 1){
            printf("Current Balance: %.2f\n", RegisteredAcc[index].Balance);
            printf("Currency: %s\n", RegisteredAcc[index].Currency);

            printf("\n");
            while(approved==0){
                printf("Deposit Amount: ");
                scanf("%f", &amount);
                if(amount<=0)
                    printf("Error: Invalid amount. Please enter a valid positive number.\n");
                else if(amount<=RegisteredAcc[index].Balance)
                    approved = 1;
                else
                    printf("Error: Invalid Amount. Please enter an amount within the balance\n");
            }
            RegisteredAcc[index].Balance += amount;
            printf("Updated Balance: %.2f\n", RegisteredAcc[index].Balance);
        }
        else if(validAcc == 0){
            printf("Error: Account not found. Please input a registered account.\n");
            validAcc = 1;
        }
    }

    printf("\n***\n");
    printf("Account Name = %s\n", AccName);
    printf("Deposit Amount = %.2f\n", amount);
}

/**
 * This function updates the balance in the user's account by subtracting the inputted wtihdraw amount
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
**/
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
        if(validAcc == 1){
            printf("Current Balance: %.2f\n", RegisteredAcc[index].Balance);
            printf("Currency: %s\n", RegisteredAcc[index].Currency);

            printf("\n");
            while(approved==0){
                printf("Withdraw Amount: ");
                scanf("%f", &amount);
                if(amount<=0)
                    printf("Error: Invalid amount. Please enter a valid positive number.\n");
                else if(amount<=RegisteredAcc[index].Balance)
                    approved = 1;
                else
                    printf("Error: Invalid Amount. Please enter an amount within the balance\n");
            }
            RegisteredAcc[index].Balance -= amount;
            printf("Updated Balance: %.2f\n", RegisteredAcc[index].Balance);
        }
        else if(validAcc == 0){
            printf("Error: Account not found. Please input a registered account.\n");
            validAcc = 1;
        }
    }
    printf("\n***\n");
    printf("Account Name = %s\n", AccName);
    printf("Withdraw Balance = %.2f\n", amount);
}

/**
 * This function lets the user record a new exchange rate for the selected currency they choose
 * @param Currencies is the array that stores information about the currencies
**/
void displayExchangeRate(Currencies Currencies[]){
    int approved = 0, validChoice = 0, currencyChoice = 0;
    float rate = 0.0;

    printf("Record Exchange Rate\n\n");
        for (int i = 1; i<=6; i++){
            printf("[%d] %s (%s)\n", i, Currencies[i-1].Name, Currencies[i-1].Code);
        }
        validChoice = 0;
        while (validChoice == 0) {
            printf("\n");
            printf("Select Foreign Currency: ");
            if (scanf(" [%d]", &currencyChoice) == 1 && currencyChoice >= 1 && currencyChoice <= 6) {
                validChoice = 1;
            } 
            while (getchar() != '\n');
            if (validChoice == 0) {
                printf("Error: Invalid input. Please enter a number in brackets.\n");
            }
        }

    while(approved==0){
        printf("Exchange Rate: ");
        scanf("%f", &rate);
        if(rate<=0)
            printf("Error: Invalid amount. Please enter a valid positive number.\n\n");
        else
            approved = 1;
    }

    Currencies[currencyChoice-1].Rate = rate;

    printf("\n***\n");
    printf("Select Foreign Currency = [%d]\n", currencyChoice);
    printf("Exchange Rate = %.2f\n", rate);
}

/**
 * This function allows the user to convert an amount of money from one currency to another
 * @param Currencies is the array that stores information about the currencies
**/
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
                printf("Error: Invalid amount. Please enter a valid positive number.\n");
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
        convert = sourceAmt * Currencies[sourceChoice-1].Rate / Currencies[exchangeChoice-1].Rate;
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
                    printf("\n");
                }
                else{
                    printf("Error: Invalid input. Please enter 'Y' or 'N' only.\n\n");
                }
        }
    }
}

/**
 * This function computes the interest rate based on the balance and amount of days inputted by the user,
 * then updates and displays the balance per day with the daily interest
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
**/
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
        if(validAcc == 1){
            printf("Current Balance: %.2f\n", RegisteredAcc[index].Balance);
            printf("Currency: %s\n", RegisteredAcc[index].Currency);
            printf("Interest Rate: %d%%\n", RegisteredAcc[index].InterestRate);
            printf("\n");

            while(approved==0){
                printf("Total Number of Days: ");
                scanf("%d", &numberDays);
                if(numberDays<=0 || numberDays>365)
                    printf("Error: Invalid Input. Please enter a number between 1 and 365\n\n");
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
        else if(validAcc == 0){
            printf("Error: Account not found. Please input a registered account.\n");
            validAcc = 1;
        }
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
                printf("Exiting...\n");
                systemActive = 0;
                backToMenu = 0;
            }
            else if (ch=='Y'){
                printf("\n");
                backToMenu = 0;
            }
            else{
                printf("Error: Invalid input. Please enter 'Y' or 'N' only.\n\n");
            }
        }
    }
}
