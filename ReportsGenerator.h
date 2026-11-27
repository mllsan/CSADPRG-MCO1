// ***********************************
// Last names: Santos
// Language: C
// Paradigm(s): Procedural Programming
// ***********************************

#ifndef REPORTS_H
#define REPORTS_H

/* preprocessor directives */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>

/* definitions */
#define PRIVILEGE_REPORT "report1_banking_privilege_tiers.csv"
#define INTEREST_REPORT "report2_interest_expense_projection.csv"
#define SUSPICIOUS_REPORT "report3_suspicious_client_behavior.csv"
#define BALANCE_CHANGE_REPORT "report4_first_last_balance_change.csv"
#define SUMMARY_JSON "summary.json"
#define MAX_SUSPICIOUS_RECORDS 10000

struct TransactionDetails;
struct AccountDetails;

/**
* Stores a single record entry for report 1
*/
typedef struct PrivilegeRecord {
    char AccountName[100];
    int AccountAge;
    float CurrentBalance;
    float AverageDailyBalance;
    char PrivilegeTier[16];
} PrivilegeRecord;

/**
* Stores a single record entry for report 2
*/
typedef struct InterestProjectionRecord {
    char AccountName[100];
    double CurrentBalance;
    double ExpectedInterest;
} InterestRecord;

/**
* Stores a single record entry for report 3
*/
typedef struct SuspiciousBehaviorRecord {
    char AccountName[100];
    char Date[32];
    int day;
    int month;
    int year;
    double TotalWithdrawalAmounts;
    int NumberOfDeposits;
    double TotalDepositAmounts;
    double TurnoverVelocity;
    int hasValidVelocity;
    char RiskFlagLevel[16];
} SuspiciousRecord;

/**
* Stores a single record entry for report 4
*/
typedef struct BalanceChangeRecord {
    int AccountNumber;
    char AccountName[100];
    double OpeningBalance;
    double CurrentBalance;
    double NetChange;
    double PercentChange;
} BalanceChangeRecord;

/**
* Stores row count metrics for loading the bank transactions
*/
typedef struct DatasetSummary {
    int totalRows;
    int validRows;
    int invalidRows;
    int excludedRows;
} DatasetSummary;

/* Report 1 Function Prototypes */
int countDaysBetween(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear);
double parseTimeToSeconds(char timeStr[]);
int compareDates(int d1, int m1, int y1, int d2, int m2, int y2);
int buildPrivilegeRecords(struct AccountDetails *RegisteredAcc, int activeAccs, PrivilegeRecord PrivilegeList[]);
void savePrivilegeRecords(PrivilegeRecord PrivilegeList[], int recordCount, char *filename);
void displayPrivilegeReport(PrivilegeRecord PrivilegeList[], int recordCount);

/* Report 2 Function Prototypes */
double dailyInterestAmount(double balance, int ratePercent);
int buildInterestRecords(struct AccountDetails *RegisteredAcc, int activeAccs, InterestRecord InterestList[]);
void saveInterestRecords(InterestRecord InterestList[], int recordCount, char *filename);
void displayInterestReport(InterestRecord InterestList[], int recordCount);

/* Report 3 Function Prototypes */
double turnoverMetric(double withdrawalTotal, double depositTotal);
int suspiciousRecordOrder(SuspiciousRecord *a, SuspiciousRecord *b);
int buildSuspiciousRecords(struct AccountDetails *RegisteredAcc, int activeAccs, SuspiciousRecord SuspiciousList[], int maxRecords);
void saveSuspiciousRecords(SuspiciousRecord SuspiciousList[], int recordCount, char *filename);
void displaySuspiciousReport(SuspiciousRecord SuspiciousList[], int recordCount);

/* Report 4 Function Prototypes */
int buildBalanceChangeRecords(struct AccountDetails *RegisteredAcc, int activeAccs, BalanceChangeRecord ChangeList[]);
void saveBalanceChangeRecords(BalanceChangeRecord ChangeList[], int recordCount, char *filename);
void displayBalanceChangeReport(BalanceChangeRecord ChangeList[], int recordCount);

/* Summary State */
void saveSummaryToJson(struct AccountDetails *RegisteredAcc, int activeAccs, InterestRecord InterestList[], int intCount, char *filename);
void displaySummaryJson(struct AccountDetails *RegisteredAcc, int activeAccs, InterestRecord InterestList[], int intCount);

#endif