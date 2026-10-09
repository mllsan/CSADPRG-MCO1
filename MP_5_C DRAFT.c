// ***********************************
// Last names: Santos
// Language: C 
// Paradigm(s): Procedural Programming
// ***********************************

/* preprocessor directives */
#include <stdio.h>
#include <string.h>
#include <math.h>

/* definitions */
typedef struct AccountDetails {
    char AccountName[100];
    float Balance;
    char Currency[3];
    int InterestRate;
} Account;

typedef struct CurrencyDetails {
    char Name[100];
    float Rate;
    char Code[3];
} Currencies;

/* helper functions */
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

int getValidInput(int min, int max) {
    int num;

    while (1) {
        if (scanf("%d", &num) != 1) {
            while (getchar() != '\n'); 
            printf("Error: Please enter a valid integer.\nChoice: ");
            continue;
        }
        if (num < min || num > max) {
            printf("Error: Please enter a number between %d and %d.\nChoice: ", min, max);
            continue;
        }

        return num;
    }
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
    *choice = getValidInput(1, 7);

    printf("\n***\n");
    printf("Choice = %d\n", *choice);
}

void registerAccount(Account *RegisteredAcc){
    printf("Register Account Name\n");

    printf("Account Name: ");
    scanf(" %[^\n]", RegisteredAcc->AccountName);
}

void depositAmount(Account *RegisteredAcc){
    float amount = 0.0;
    int approved = 0;
    char AccName[100];

    printf("Deposit Amount\n");
    do{
        printf("Account Name: ");
        scanf(" %[^\n]", AccName);

        if(strcmp(AccName, RegisteredAcc->AccountName) != 0){
            printf("Error: Please enter a valid account\n\n");
        }
    }while(strcmp(AccName, RegisteredAcc->AccountName) != 0);

    printf("Current Balance: %.2f\n", RegisteredAcc->Balance);
    printf("Currency: %s\n", RegisteredAcc->Currency);

    printf("\n");
    while(approved==0){
        printf("Deposit Amount: ");
        scanf("%f", &amount);
        if(amount<=0)
            printf("Error: Please enter a valid amount\n");
        else
            approved = 1;
    }
    RegisteredAcc->Balance += amount;
    printf("Updated Balance: %.2f\n", RegisteredAcc->Balance);
}

void withdrawAmount(Account *RegisteredAcc){
    float amount = 0.0;
    int approved = 0;
    char AccName[100];

    printf("Withdraw Amount\n");
    do{
        printf("Account Name: ");
        scanf(" %[^\n]", AccName);

        if(strcmp(AccName, RegisteredAcc->AccountName) != 0){
            printf("Error: Please enter a valid account\n\n");
        }
    }while(strcmp(AccName, RegisteredAcc->AccountName) != 0);

    printf("Current Balance: %.2f\n", RegisteredAcc->Balance);
    printf("Currency: %s\n", RegisteredAcc->Currency);

    printf("\n");
    while(approved==0){
        printf("Withdraw Amount: ");
        scanf("%f", &amount);
        if(amount<=0)
            printf("Error: Please enter a valid amount\n");
        else if(amount<=RegisteredAcc->Balance)
            approved = 1;
        else
            printf("Error: Please enter an amount within the balance\n");
    }
    RegisteredAcc->Balance -= amount;
    printf("Updated Balance: %.2f\n", RegisteredAcc->Balance);
}

void displayExchangeRate(Currencies Currencies[]){
    int choice = 0, approved = 0, validChoice = 0;
    float rate = 0.0;

    printf("Record Exchange rate\n");
    printf("\n");
    for (int i = 1; i<=6; i++){
        printf("[%d] %s (%s)\n", i, Currencies[i-1].Name, Currencies[i-1].Code);
    }
    printf("\n");

    while (getchar() != '\n');
    while (validChoice == 0) {
        printf("Select Foreign Currency: ");
        if (scanf("[%d]", &choice) == 1) {
            if (choice >= 1 && choice <= 6) {
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

    Currencies[choice-1].Rate = rate;
    printf("\n***\n");
    printf("Select Foreign Currency = [%d]\n", choice);
    printf("Exchange Rate = %.2f\n", rate);
}

void currencyConversion(Account RegisteredAcc, Currencies Currencies[]){
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
        convert = sourceAmt*Currencies[exchangeChoice-1].Rate;
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

void showInterestAmount(Account RegisteredAcc){
    int approved = 0, numberDays = 0;
    float interest = 0.0;
    double roundedInterest;

    printf("Show Interest Amount\n");
    printf("Account Name: %s\n", RegisteredAcc.AccountName);
    printf("Current Balance: %.2f\n", RegisteredAcc.Balance);
    printf("Currency: %s\n", RegisteredAcc.Currency);
    printf("Interest Rate: %d%%\n", RegisteredAcc.InterestRate);
    printf("\n");

    while(approved==0){
        printf("Total Number of Days: ");
        scanf("%d", &numberDays);
        if(numberDays<=0)
            printf("Error: Please enter a valid number\n");
        else
            approved = 1;
    }

    interest = RegisteredAcc.Balance * ((RegisteredAcc.InterestRate/100.0)/365);
    roundedInterest = round(interest * 100.0) / 100.0;

    printf("Day | Interest | Balance |\n");
    for(int i = 1; i<=numberDays; i++){
        RegisteredAcc.Balance += interest;
        printf("%d   |   %.2f   | %.2f |\n", i, roundedInterest, RegisteredAcc.Balance);
    }

}

int main(){
    int choice = 0;
    int systemActive = 1;
    char ch;
    Account RegisteredAcc = {"", 1000, "PHP", 5};
    Currencies Currencies[6];
    getCurrencyDetails(Currencies);

    while (systemActive == 1){
        MainMenu(&choice);
        int backToMenu = 1;
        if (choice == 1){
            printf("\n");
            registerAccount(&RegisteredAcc);
            printf("\n");
        }
        else if (choice == 2){
            printf("\n");
            depositAmount(&RegisteredAcc);
            printf("\n");
        }
        else if (choice == 3){
            printf("\n");
            withdrawAmount(&RegisteredAcc);
            printf("\n");
        }
        else if (choice == 4){
            printf("\n");
            currencyConversion(RegisteredAcc, Currencies);
            printf("\n");
        }
        else if (choice == 5){
            printf("\n");
            displayExchangeRate(Currencies);
            printf("\n");
        }
        else if (choice == 6){
            printf("\n");
            showInterestAmount(RegisteredAcc);
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
