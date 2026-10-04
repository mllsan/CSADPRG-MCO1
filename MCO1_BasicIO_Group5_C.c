// ***********************************
// Last names: Santos
// Language: C 
// Paradigm(s): Procedural Programming
// ***********************************

/* preprocessor directives */
#include <stdio.h>
#include <string.h>

/* definitions */
typedef struct AccountDetails {
    char AccountName[100];
    float Balance;
    char Currency[3];
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
            printf("Error: Please enter a valid integer.\n>> ");
            continue;
        }
        if (num < min || num > max) {
            printf("Error: Please enter a number between %d and %d.\n>> ", min, max);
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
    printf("[7] Exit\n");

    printf("\n");
    printf(">> ");
    *choice = getValidInput(1, 7);

    printf("----------------------------\n");
    printf("Choice = %d\n", *choice);
}

void registerAccount(Account *RegisteredAcc){
    printf("Register Account Name\n");

    printf("Account Name: ");
    scanf("%s", RegisteredAcc->AccountName);

    printf("----------------------------\n");
    printf("Account Name = %s\n", RegisteredAcc->AccountName);
}

void depositAmount(Account *RegisteredAcc){
    float amount = 0.0;
    int approved = 0;

    printf("Deposit Amount\n");
    printf("Account Name: %s\n", RegisteredAcc->AccountName);
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

    printf("----------------------------\n");
    printf("Account Name = %s\n", RegisteredAcc->AccountName);
    printf("Current Balance = %.2f\n", RegisteredAcc->Balance);
    
}

void withdrawAmount(Account *RegisteredAcc){
    float amount = 0.0;
    int approved = 0;

    printf("Deposit Amount\n");
    printf("Account Name: %s\n", RegisteredAcc->AccountName);
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

    printf("----------------------------\n");
    printf("Account Name = %s\n", RegisteredAcc->AccountName);
    printf("Current Balance = %.2f\n", RegisteredAcc->Balance);
}

void displayExchangeRate(Currencies Currencies[]){
    int choice = 0;
    float rate = 0.0;
    int approved = 0;

    printf("Record Exchange rate\n");
    printf("\n");
    for (int i = 1; i<=6; i++){
        printf("[%d] %s (%s)\n", i, Currencies[i-1].Name, Currencies[i-1].Code);
    }
    printf("\n");
    printf("Select Foreign Currency: ");
    choice = getValidInput(1, 6);
    while(approved==0){
        printf("Exchange Rate: ");
        scanf("%f", &rate);
        if(rate<=0)
            printf("Error: Please enter a valid rate\n");
        else
            approved = 1;
    }

    Currencies[choice-1].Rate = rate;
    printf("----------------------------\n");
    printf("Select Foreign Currency = [%d]\n", choice);
    printf("Exchange Rate = %.2f\n", rate);
}

void currencyConversion(Account RegisteredAcc, Currencies Currencies[]){
    double convert = 0.0;
    double sourceAmt = 0.0;
    int approved = 0;

    printf("Foreign Currency Exchange\n");
    while(approved==0){
        printf("Source Amount (%s): ", RegisteredAcc.Currency);
        scanf("%lf", &sourceAmt);
        if(sourceAmt<=0)
            printf("Error: Please enter a valid amount\n");
        else
            approved = 1;
    }

    printf("\n");
    printf("Exchanged Currency\n");
    for (int i = 1; i<=6; i++){
        convert = RegisteredAcc.Balance * Currencies[i-1].Rate;
        printf("[%d] %s (%s) = %.2f\n", i, Currencies[i-1].Name, Currencies[i-1].Code, convert);
    }

    printf("\n");
    printf("----------------------------\n");
    printf("Source Currency = %s (%s)\n", Currencies[0].Name, Currencies[0].Code);
    printf("Source Amount (%s) = %f\n", RegisteredAcc.Currency, sourceAmt);
}

int main(){
    int choice = 0;
    Account RegisteredAcc = {"", 1000, "PHP"};
    Currencies Currencies[6];
    getCurrencyDetails(Currencies);

    MainMenu(&choice);
    int systemActive = 1;
    while (systemActive == 1){
        if (choice == 1){
            printf("\n");
            registerAccount(&RegisteredAcc);
            printf("\n");
            MainMenu(&choice);
        }
        else if (choice == 2){
            printf("\n");
            depositAmount(&RegisteredAcc);
            printf("\n");
            MainMenu(&choice);
        }
        else if (choice == 3){
            printf("\n");
            withdrawAmount(&RegisteredAcc);
            printf("\n");
            MainMenu(&choice);
        }
        else if (choice == 4){
            printf("\n");
            displayExchangeRate(Currencies);
            printf("\n");
            MainMenu(&choice);
        }
        else if (choice == 5){
            printf("\n");
            currencyConversion(RegisteredAcc, Currencies);
            printf("\n");
            MainMenu(&choice);
        }
        else if (choice == 6){
            printf("\n");
            printf("To be implemented...");
            printf("\n");
            MainMenu(&choice);
        }
        else if (choice == 7){
            printf("\n");
            printf("Exiting the system...\n");
            systemActive = 0;
        }
    }
}
