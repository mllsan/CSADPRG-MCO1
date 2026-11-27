// ***********************************
// Last names: Santos
// Language: C
// Paradigm(s): Procedural Programming
// Run: gcc -Wall -std=c99 MP_5_C_BankSystem.c MP_5_C_ReportsGenerator.c -lm -o run.exe
// ***********************************

/* preprocessor directives */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <ctype.h>
#include <time.h>
#include "MP_5_C_UserInformation.h"
#include "MP_5_C_ReportsGenerator.h"

/* definitions */
#define BANK_TRANSACTIONS "bank_transactions.csv"
#define MAX_ACCOUNTS 10000
#define MAX_HISTORY 500

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
 * This function adds each transaction the user has made in the history of their account
 * @param acc is a pointer to which account the records will appended to
 * @param date is the date when the transaction was made
 * @param time is the time when the transaction was made
 * @param amount is amount that was added/deducted from the account
 * @param type is the type of transaction made (withdraw/deposit)
 * @param balance is the total amount stored in the account
 **/
void addHistoryEntry(Account *acc, char date[], char time[], float amount, char type[], double balance) {
    if (acc != NULL && acc->HistoryCount < MAX_HISTORY) {
        snprintf(acc->History[acc->HistoryCount].date, sizeof(acc->History[acc->HistoryCount].date), "%s", date);
        snprintf(acc->History[acc->HistoryCount].time, sizeof(acc->History[acc->HistoryCount].time), "%s", time);
        snprintf(acc->History[acc->HistoryCount].type, sizeof(acc->History[acc->HistoryCount].type), "%s", type);

        acc->History[acc->HistoryCount].amount = amount;
        acc->History[acc->HistoryCount].balance = balance;
        acc->HistoryCount++;
    }
}

/**
 * This function labels and initializes the different currencies available
 * @param Currencies is the array that stores information about the currencies
 **/
void getCurrencyDetails(Currencies Currencies[]) {
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
 * This function gets the local time to record the date and time of a transaction
 * @param tx is a pointer to the transaction whose date and time will be updated.
 */
void setCurrentTimestamp(Transaction *tx) {
    time_t now = 0;
    struct tm *localTime = NULL;

    if (tx != NULL) {
        strcpy(tx->date, "");
        strcpy(tx->time, "");

        now = time(NULL);
        localTime = localtime(&now);

        if (localTime != NULL) {
            strftime(tx->date, sizeof(tx->date), "%d/%m/%Y", localTime);
            strftime(tx->time, sizeof(tx->time), "%H:%M:%S", localTime);
        }
    }
}

/**
 * This function asks for user input that is within the accepted range
 * @param min is the minimum value the user can input
 * @param max is the maximum value the user can input
 * @return the number if it is within the accepted range
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

int findAccountNumber(Account RegisteredAcc[], int activeAccs, int accNumber) {
    int index = -1;

    for (int i = 0; i < activeAccs; i++) {
        if (RegisteredAcc[i].AccountNumber == accNumber) {
            index = i;
        }
    }
    return index;
}

/* function implementations */
/**
 * This function reads and extracts the data from bank_transactions_csv
 * @param fp is a pointer to csv file that is being read
 * @param name stores the name of account
 * @param accNum stores the number of the account
 * @param date stores the date when the transaction was made
 * @param time stores the time when the transaction was made
 * @param amount stores the amount that was added/deducted from the account
 * @param type stores the type of transaction made (withdraw/deposit)
 * @param balance stores the total amount in the account
 **/
int readDataLine(FILE *fp, char name[], int *accNum, char date[], char time[], char currency[], float *amount, char type[], float *balance) {
    char line[1024];
    int status = -1, scanned = 0;

    if (fp == NULL) {
        status = 0;
    } 
    else if (fgets(line, sizeof(line), fp) == NULL) {
        status = 0;
    } 
    else {
        line[strcspn(line, "\r\n")] = '\0';
        scanned = sscanf(line, " %99[^,],%d,%31[^,],%31[^,],%15[^,],%f,%31[^,],%f", 
                        name, accNum, date, time, currency, amount, type, balance);

        if (scanned == 8) {
            status = 1;
        } 
        else {
            status = -1;
        }
    }

    return status;
}

/**
 * This function loads the account and transaction details and organizes it into an array
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
 * @param maxAccs is the maximum number of accounts that can be stored
 * @param latestAccNum is the pointer to track the highest account number in the system
 * @param fp is pointer to the CSV file being loaded
 * @param summary is a pointer to track the row counts.
 */
void loadData(Account RegisteredAcc[], int *activeAccs, int maxAccs, int *latestAccNum, FILE *fp, DatasetSummary *summary) {
    char name[128], date[32], time[32], currency[16], type[32];
    int accNum = 0, i = 0, found = -1, status = 0;;
    float amount = 0.0f, balance = 0.0f;

    *activeAccs = 0;
    *latestAccNum = 0;

    if (summary != NULL) {
        summary->totalRows = 0;
        summary->validRows = 0;
        summary->invalidRows = 0;
        summary->excludedRows = 0;
    }

    if (fp != NULL) {
        rewind(fp);

        if (fgets(name, sizeof(name), fp) != NULL) {
            while ((status = readDataLine(fp, name, &accNum, date, time, currency, &amount, type, &balance)) != 0) {
                if (summary != NULL) {
                    summary->totalRows++;
                }

                if (status == 1) {
                    if (accNum > *latestAccNum) {
                        *latestAccNum = accNum;
                    }

                    if (strcmp(currency, "PHP") == 0) {
                        found = -1;

                        for (i = 0; i < *activeAccs; i++) {
                            if (RegisteredAcc[i].AccountNumber == accNum) {
                                found = i;
                            }
                        }

                        if (found == -1 && *activeAccs < maxAccs) {
                            found = *activeAccs;
                            (*activeAccs)++;
                            snprintf(RegisteredAcc[found].AccountName, sizeof(RegisteredAcc[found].AccountName), "%.99s", name);
                            RegisteredAcc[found].AccountNumber = accNum;
                            strcpy(RegisteredAcc[found].Currency, currency);
                            RegisteredAcc[found].InterestRate = 5;
                            RegisteredAcc[found].HistoryCount = 0;
                        }

                        if (found != -1) {
                            addHistoryEntry(&RegisteredAcc[found], date, time, amount, type, (double)balance);
                            RegisteredAcc[found].Balance = balance;
                        }

                        if (summary != NULL) {
                            summary->validRows++;
                        }
                    } 
                    else {
                        if (summary != NULL) {
                            summary->excludedRows++;
                        }
                    }
                } 
                else {
                    if (summary != NULL) {
                        summary->invalidRows++;
                    }
                }
            }
        }

        fclose(fp);
    }
}

/**
 * This function saves the data of a transaction record to a file
 * @param account is a pointer to the account which did the transaction
 * @param tx is the transaction details to be saved
 * @param fSave is a pointer to the file where the data will be written
 */
void saveData(Account *account, Transaction tx, FILE *fSave) {
    if (fSave != NULL && account != NULL) {
        tx.balance = (double)account->Balance;

        fprintf(fSave, "%s,%d,%s,%s,%s,%.2f,%s,%.2f\n",
                account->AccountName,
                account->AccountNumber,
                tx.date,
                tx.time,
                account->Currency,
                tx.amount,
                tx.type,
                account->Balance);

        fclose(fSave);
        addHistoryEntry(account, tx.date, tx.time, tx.amount, tx.type, tx.balance);
    }
}

/**
 * This function displays the main menu interface and gets the user's choice
 * @param choice is the user's inputted choice from the menu
**/
void MainMenu(int *choice) {
    printf("\nSelect Transaction: \n");
    printf("[1] Register Account Name\n");
    printf("[2] Deposit Amount\n");
    printf("[3] Withdraw Amount\n");
    printf("[4] Currency Exchange\n");
    printf("[5] Record Exchange Rates\n");
    printf("[6] Show Interest Amount\n");
    printf("[7] Generate Reports\n");

    printf("\n");
    printf("Choice: ");
    *choice = getValidInput(1, 7);

    printf("\n***\n");
    printf("Choice = %d\n", *choice);
}

/**
 * This function registers a new account and asks the user to input an account name with a specific format
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
 * @param latestAccNum is the pointer to track the highest account number in the system
**/
void registerAccount(Account RegisteredAcc[], int *activeAccs, int *latestAccNum) {
    char Name[100];
    int c;
    int sameName = 0, approved = 0;
    float initialAmount = 0.0;
    Transaction tx;
    FILE *fSave = NULL;

    printf("Register Account Name\n");
    while ((c = getchar()) != '\n' && c != EOF);
    do {
        sameName = 0;
        printf("Account Name: ");
        scanf(" %[^\n]", Name);
        for (int i = 0; i < *activeAccs; i++) {
            if (strcmp(RegisteredAcc[i].AccountName, Name) == 0) {
                sameName = 1;
                printf("Error: Account already registered. Please enter a different name.\n\n");
                break;
            }
        }
    } while (sameName == 1);

    (*latestAccNum)++;
    RegisteredAcc[*activeAccs].AccountNumber = *latestAccNum;
    strcpy(RegisteredAcc[*activeAccs].AccountName, Name);
    strcpy(RegisteredAcc[*activeAccs].Currency, "PHP");
    RegisteredAcc[*activeAccs].InterestRate = 5;
    RegisteredAcc[*activeAccs].HistoryCount = 0;

    while (approved == 0) {
        printf("Initial Deposit: ");
        if (scanf("%f", &initialAmount) != 1) {
            while ((c = getchar()) != '\n' && c != EOF);
            printf("Error: Invalid input. Please enter a valid number.\n\n");
            continue;
        }

        if (initialAmount >= 5000) {
            RegisteredAcc[*activeAccs].Balance = initialAmount;
            approved = 1;
        } else {
            printf("Error: Invalid initial deposit. Minimum amount is Php 5000.00\n");
        }
    }

    tx.amount = initialAmount;
    strcpy(tx.type, "deposit");
    setCurrentTimestamp(&tx);
    fSave = fopen(BANK_TRANSACTIONS, "a");
    saveData(&RegisteredAcc[*activeAccs], tx, fSave);
    (*activeAccs)++;

    printf("\n***\n");
    printf("Account Name = %s\n", Name);
    printf("Account Number = %d\n", *latestAccNum);
    printf("Initial Deposit = %.2f\n", initialAmount);
}

/**
 * This function updates the balance in the user's account by adding the inputted deposit amount
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
**/
void depositAmount(Account RegisteredAcc[], int activeAccs) {
    float amount = 0.0f;
    int approved = 0, index = 0, validAcc = 0, willRepeat = 0, c = 0;
    char AccName[100], repeat = 'Y';
    Transaction tx;
    FILE *fSave = NULL;

    while (repeat == 'Y') {
        approved = 0;
        index = 0;
        validAcc = 0;
        willRepeat = 0;
        printf("Deposit Amount\n");

        while (validAcc == 0) {
            printf("Account Name: ");
            scanf(" %[^\n]", AccName);

            for (int i = 0; i < activeAccs; i++) {
                if (strcmp(AccName, RegisteredAcc[i].AccountName) == 0) {
                    index = i;
                    validAcc = 1;
                }
            }
            if (validAcc == 0) {
                printf("Error: Account not found. Please input a registered account.\n");
            }
        }

        printf("Current Balance: %.2f\n", RegisteredAcc[index].Balance);
        printf("Currency: %s\n", RegisteredAcc[index].Currency);
        printf("\n");

        while (approved == 0) {
            printf("Deposit Amount: ");

            if (scanf("%f", &amount) != 1) {
                while ((c = getchar()) != '\n' && c != EOF);
                printf("Invalid input. Please enter a number\n");
            } 
            else if (amount <= 0) {
                printf("Error: Invalid amount. Please enter a valid positive number.\n");
            } 
            else {
                approved = 1;
            }
        }

        RegisteredAcc[index].Balance += amount;
        tx.amount = amount;
        strcpy(tx.type, "deposit");
        setCurrentTimestamp(&tx);
        fSave = fopen(BANK_TRANSACTIONS, "a");
        saveData(&RegisteredAcc[index], tx, fSave);

        printf("***\n");
        printf("Account Name = %s\n", AccName);
        printf("Deposit Amount = %.2f\n", amount);
        printf("Updated Balance = %.2f\n\n", RegisteredAcc[index].Balance);

        while (willRepeat == 0) {
            printf("\nDeposit another amount? (Y/N): ");
            scanf(" %c", &repeat);

            if (repeat == 'N') {
                willRepeat = 1;
            } 
            else if (repeat == 'Y') {
                printf("\n");
                willRepeat = 1;
            } 
            else {
                printf("Error: Invalid input. Please enter 'Y' or 'N' only.\n\n");
            }
        }
    }
}

/**
 * This function updates the balance in the user's account by subtracting the inputted withdraw amount
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
**/
void withdrawAmount(Account RegisteredAcc[], int activeAccs) {
    float amount = 0.0f;
    int approved = 0, index = 0, validAcc = 0, willRepeat = 0, c = 0;
    char AccName[100], repeat = 'Y';
    Transaction tx;
    FILE *fSave = NULL;

    while (repeat == 'Y') {
        approved = 0;
        index = 0;
        validAcc = 0;
        willRepeat = 0;
        printf("Withdraw Amount\n");
        printf("Account Name: ");
        scanf(" %[^\n]", AccName);

        while (validAcc == 0) {
            for (int i = 0; i < activeAccs; i++) {
                if (strcmp(AccName, RegisteredAcc[i].AccountName) == 0) {
                    index = i;
                    validAcc = 1;
                }
            }
            if (validAcc == 1) {
                printf("Current Balance: %.2f\n", RegisteredAcc[index].Balance);
                printf("Currency: %s\n", RegisteredAcc[index].Currency);
                printf("\n");

                while (approved == 0) {
                    printf("Withdraw Amount: ");

                    if (scanf("%f", &amount) != 1) {
                        while ((c = getchar()) != '\n' && c != EOF);
                        printf("Error: Invalid input. Please enter a number\n");
                    } 
                    else if (amount <= 0) {
                        printf("Error: Invalid amount. Please enter a valid positive number.\n");
                    } 
                    else if (amount <= RegisteredAcc[index].Balance) {
                        approved = 1;
                    } 
                    else {
                        printf("Error: Invalid Amount. Please enter an amount within the balance\n");
                    }
                }

                RegisteredAcc[index].Balance -= amount;
                tx.amount = amount;
                strcpy(tx.type, "withdrawal");
                setCurrentTimestamp(&tx);
                fSave = fopen(BANK_TRANSACTIONS, "a");
                saveData(&RegisteredAcc[index], tx, fSave);
            } 
            else if (validAcc == 0) {
                printf("Error: Account not found. Please input a registered account.\n");
                validAcc = 1;
            }
        }

        printf("\n***\n");
        printf("Account Name = %s\n", AccName);
        printf("Withdraw Balance = %.2f\n", amount);
        printf("Updated Balance = %.2f\n\n", RegisteredAcc[index].Balance);

        while (willRepeat == 0) {
            printf("\nWithdraw another amount? (Y/N): ");
            scanf(" %c", &repeat);

            if (repeat == 'N') {
                willRepeat = 1;
            } 
            else if (repeat == 'Y') {
                printf("\n");
                willRepeat = 1;
            } 
            else {
                printf("Error: Invalid input. Please enter 'Y' or 'N' only.\n\n");
            }
        }
    }
}

/**
 * This function lets the user record a new exchange rate for the selected currency they choose
 * @param Currencies is the array that stores information about the currencies
**/
void displayExchangeRate(Currencies Currencies[]) {
    int approved = 0, validChoice = 0, currencyChoice = 0, c = 0;
    float rate = 0.0;

    printf("Record Exchange Rate\n\n");
    for (int i = 1; i <= 6; i++) {
        printf("[%d] %s (%s)\n", i, Currencies[i - 1].Name, Currencies[i - 1].Code);
    }
    while (validChoice == 0) {
        printf("\n");
        printf("Select Foreign Currency: ");

        if (scanf(" [%d]", &currencyChoice) == 1 && currencyChoice >= 1 && currencyChoice <= 6) {
            validChoice = 1;
        }
        while ((c = getchar()) != '\n' && c != EOF);
        if (validChoice == 0) {
            printf("Error: Invalid input. Please enter a number in brackets.\n");
        }
    }

    while (approved == 0) {
        printf("Exchange Rate: ");
        if (scanf("%f", &rate) != 1) {
            while ((c = getchar()) != '\n' && c != EOF);
            printf("Error: Invalid input. Please enter a number.\n\n");
        } 
        else if (rate <= 0) {
            printf("Error: Invalid amount. Please enter a valid positive number.\n\n");
        } 
        else {
            while ((c = getchar()) != '\n' && c != EOF);
            approved = 1;
        }
    }

    Currencies[currencyChoice - 1].Rate = rate;

    printf("\n***\n");
    printf("Select Foreign Currency = [%d]\n", currencyChoice);
    printf("Exchange Rate = %.2f\n", rate);
}

/**
 * This function allows the user to convert an amount of money from one currency to another
 * @param Currencies is the array that stores information about the currencies
**/
void currencyConversion(Currencies Currencies[]) {
    double convert = 0.0, sourceAmt = 0.0;
    int approved = 0, sourceChoice = 0, exchangeChoice = 0, convertAgain = 1, error = 1, c = 0;
    char ch;

    while (convertAgain == 1) {
        printf("Foreign Currency Exchange\n");
        printf("Source Currency Option:\n");

        for (int i = 1; i <= 6; i++) {
            printf("[%d] %s (%s)\n", i, Currencies[i - 1].Name, Currencies[i - 1].Code);
        }

        printf("\n");
        printf("Source Currency: ");
        sourceChoice = getValidInput(1, 6);
        approved = 0;

        while (approved == 0) {
            printf("Source Amount: ");

            if (scanf("%lf", &sourceAmt) != 1) {
                while ((c = getchar()) != '\n' && c != EOF);
                printf("Error: Invalid input. Please enter a number.\n");
            } 
            else if (sourceAmt <= 0) {
                printf("Error: Invalid amount. Please enter a valid positive number.\n");
            } 
            else {
                while ((c = getchar()) != '\n' && c != EOF);
                approved = 1;
            }
        }

        printf("\n");
        printf("Exchanged Currency Options:\n");

        for (int i = 1; i <= 6; i++) {
            printf("[%d] %s (%s)\n", i, Currencies[i - 1].Name, Currencies[i - 1].Code);
        }

        printf("\n");
        printf("Exchange Currency: ");
        exchangeChoice = getValidInput(1, 6);
        convert = sourceAmt * Currencies[sourceChoice - 1].Rate / Currencies[exchangeChoice - 1].Rate;
        printf("Exchange Amount: %.2f\n\n", convert);
        error = 1;

        while (error == 1) {
            printf("Convert another currency (Y/N)? ");
            scanf(" %c", &ch);
            if (ch == 'N') {
                convertAgain = 0;
                error = 0;
            } 
            else if (ch == 'Y') {
                convertAgain = 1;
                error = 0;
                printf("\n");
            } 
            else {
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
void showInterestAmount(Account RegisteredAcc[], int activeAccs) {
    int approved = 0, numberDays = 0, index = 0, validAcc = 0, c = 0;
    float interest = 0.0;
    char AccName[100];
    double roundedInterest;

    printf("Show Interest Amount\n");
    printf("Account Name: ");
    scanf(" %[^\n]", AccName);
    while (validAcc == 0) {
        for (int i = 0; i < activeAccs; i++) {
            if (strcmp(AccName, RegisteredAcc[i].AccountName) == 0) {
                index = i;
                validAcc = 1;
            }
        }
        if (validAcc == 1) {
            printf("Current Balance: %.2f\n", RegisteredAcc[index].Balance);
            printf("Currency: %s\n", RegisteredAcc[index].Currency);
            printf("Interest Rate: %d%%\n", RegisteredAcc[index].InterestRate);
            printf("\n");

            while (approved == 0) {
                printf("Total Number of Days: ");
                if (scanf("%d", &numberDays) != 1) {
                    while ((c = getchar()) != '\n' && c != EOF);
                    printf("Error: Invalid input. Please enter a number.\n\n");
                } 
                else if (numberDays <= 0 || numberDays > 365) {
                    printf("Error: Invalid Input. Please enter a number between 1 and 365\n\n");
                } 
                else {
                    while ((c = getchar()) != '\n' && c != EOF);
                    approved = 1;
                }
            }

            interest = RegisteredAcc[index].Balance * ((RegisteredAcc[index].InterestRate / 100.0) / 365);
            roundedInterest = round(interest * 100.0) / 100.0;

            printf("Day | Interest | Balance |\n");
            for (int i = 1; i <= numberDays; i++) {
                RegisteredAcc[index].Balance += interest;
                printf("%d   |   %.2f   | %.2f |\n", i, roundedInterest, RegisteredAcc[index].Balance);
            }
        } 
        else if (validAcc == 0) {
            printf("Error: Account not found. Please input a registered account.\n");
            validAcc = 1;
        }
    }
}

/**
 * This function calls the builder and display functions of the reports produced from the data
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
 * @param latestAccNum is the pointer to track the highest account number in the system
 * @param PrivilegeList is an array for storing privilege report records.
 * @param InterestList is an array for storing interest report records.
 * @param SuspiciousList is an array for storing suspicious activity records.
 * @param ChangeList is an array for storing balance change records.
 * @param summary is a pointer to the dataset summary.
 */
void generateReports(Account RegisteredAcc[], int *activeAccs, int *latestAccNum, PrivilegeRecord PrivilegeList[], 
    InterestRecord InterestList[], SuspiciousRecord SuspiciousList[], BalanceChangeRecord ChangeList[], DatasetSummary *summary) {
    int generating = 1, fileLoaded = 0;

    while (generating == 1) {
        int subChoice = 0;

        printf("Generate Reports\n");
        printf("[1] Load the file\n");
        printf("[2] Generate Reports\n");
        printf("Enter choice: ");
        subChoice = getValidInput(1, 2);

        if (subChoice == 1) {
            FILE *fp = fopen(BANK_TRANSACTIONS, "r");
            if (fp == NULL) {
                printf("Error loading %s file.\n", BANK_TRANSACTIONS);
                printf("Please ensure that the file exists and is accessible.\n");
            } 
            else {
                loadData(RegisteredAcc, activeAccs, MAX_ACCOUNTS, latestAccNum, fp, summary);
                fileLoaded = 1;
                printf("\nProcessing dataset... (%d rows loaded, %d filtered for PHP currency)\n\n", summary->totalRows, summary->validRows);
            }
        } 
        else {
            int privilegeCount = 0, interestCount = 0, susCount = 0, changeCount = 0;

            if (!fileLoaded) {
                FILE *fp = fopen(BANK_TRANSACTIONS, "r");
                
                if (fp != NULL) {
                    loadData(RegisteredAcc, activeAccs, MAX_ACCOUNTS, latestAccNum, fp, summary);
                    fileLoaded = 1;
                }
            }

            printf("\nGenerating reports...\n");

            privilegeCount = buildPrivilegeRecords(RegisteredAcc, *activeAccs, PrivilegeList);
            savePrivilegeRecords(PrivilegeList, privilegeCount, PRIVILEGE_REPORT);

            interestCount = buildInterestRecords(RegisteredAcc, *activeAccs, InterestList);
            saveInterestRecords(InterestList, interestCount, INTEREST_REPORT);

            susCount = buildSuspiciousRecords(RegisteredAcc, *activeAccs, SuspiciousList, MAX_SUSPICIOUS_RECORDS);
            saveSuspiciousRecords(SuspiciousList, susCount, SUSPICIOUS_REPORT);

            changeCount = buildBalanceChangeRecords(RegisteredAcc, *activeAccs, ChangeList);
            saveBalanceChangeRecords(ChangeList, changeCount, BALANCE_CHANGE_REPORT);

            saveSummaryToJson(RegisteredAcc, *activeAccs, InterestList, interestCount, SUMMARY_JSON);

            printf("\nOutputs saved to individual files...\n");
            displayPrivilegeReport(PrivilegeList, privilegeCount);
            displayInterestReport(InterestList, interestCount);
            displaySuspiciousReport(SuspiciousList, susCount);
            displayBalanceChangeReport(ChangeList, changeCount);
            displaySummaryJson(RegisteredAcc, *activeAccs, InterestList, interestCount);

            generating = 0;
        }
    }
}

int main() {
    int choice = 0, systemActive = 1, latestAccNum = 0;
    int activeAccs = 0;
    char ch;

    DatasetSummary summary = {0};
    Account *RegisteredAcc = (Account *)calloc(MAX_ACCOUNTS, sizeof(Account));
    PrivilegeRecord *PrivilegeList = (PrivilegeRecord *)calloc(MAX_ACCOUNTS, sizeof(PrivilegeRecord));
    InterestRecord *InterestList = (InterestRecord *)calloc(MAX_ACCOUNTS, sizeof(InterestRecord));
    SuspiciousRecord *SuspiciousList = (SuspiciousRecord *)calloc(MAX_SUSPICIOUS_RECORDS, sizeof(SuspiciousRecord));
    BalanceChangeRecord *ChangeList = (BalanceChangeRecord *)calloc(MAX_ACCOUNTS, sizeof(BalanceChangeRecord));

    Currencies Currencies[6];
    getCurrencyDetails(Currencies);

    FILE *fp = fopen(BANK_TRANSACTIONS, "r");
    if (fp == NULL) {
        printf("Error: failed to load %s file.\n", BANK_TRANSACTIONS);
    } 
    else {
        loadData(RegisteredAcc, &activeAccs, MAX_ACCOUNTS, &latestAccNum, fp, &summary);
    }

    while (systemActive == 1) {
        MainMenu(&choice);
        int backToMenu = 1;
        if (choice == 1) {
            printf("\n");
            registerAccount(RegisteredAcc, &activeAccs, &latestAccNum);
            printf("\n");
        } else if (choice == 2) {
            printf("\n");
            depositAmount(RegisteredAcc, activeAccs);
            printf("\n");
        } else if (choice == 3) {
            printf("\n");
            withdrawAmount(RegisteredAcc, activeAccs);
            printf("\n");
        } else if (choice == 4) {
            printf("\n");
            currencyConversion(Currencies);
            printf("\n");
        } else if (choice == 5) {
            printf("\n");
            displayExchangeRate(Currencies);
            printf("\n");
        } else if (choice == 6) {
            printf("\n");
            showInterestAmount(RegisteredAcc, activeAccs);
            printf("\n");
        } else if (choice == 7) {
            printf("\n");
            generateReports(RegisteredAcc, &activeAccs, &latestAccNum,
                                PrivilegeList, InterestList, SuspiciousList,
                                ChangeList, &summary);
            printf("\n");
        }

        while (backToMenu == 1) {
            printf("Back to the Main Menu (Y/N): ");
            scanf(" %c", &ch);

            if (ch == 'N') {
                printf("Exiting...\n");
                systemActive = 0;
                backToMenu = 0;
            } else if (ch == 'Y') {
                printf("\n");
                backToMenu = 0;
            } else {
                printf("Error: Invalid input. Please enter 'Y' or 'N' only.\n\n");
            }
        }
    }

    return 0;
}
