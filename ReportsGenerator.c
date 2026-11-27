// ***********************************
// Last names: Santos
// Language: C
// Paradigm(s): Procedural Programming
// ***********************************

#include "ReportsGenerator.h"
#include "UserInformation.h"

/**
 * This function calculates the number of days between two dates
 * @param startDay is the day of the first date
 * @param startMonth is the month of the first date
 * @param startYear is the year of the first date
 * @param endDay is the day of the second date
 * @param endMonth is the month of the second date
 * @param endYear is the year of the second date
 * @return the number of days between the two dates
 */
int countDaysBetween(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
    struct tm startTm = {0}, endTm = {0};
    time_t startTime = 0, endTime = 0;
    int dayDiff = 0;

    startTm.tm_mday = startDay;
    startTm.tm_mon = startMonth - 1;
    startTm.tm_year = startYear - 1900;
    startTm.tm_isdst = -1;

    endTm.tm_mday = endDay;
    endTm.tm_mon = endMonth - 1;
    endTm.tm_year = endYear - 1900;
    endTm.tm_isdst = -1;

    startTime = mktime(&startTm);
    endTime = mktime(&endTm);

    if (startTime != -1 && endTime != -1 && endTime >= startTime) {
        dayDiff = (difftime(endTime, startTime) / 86400.0);
    }

    return dayDiff;
}

/**
 * This function converts a time string to its total number of seconds
 * @param timeStr is the string that contains the time to be converted
 * @return the total time in seconds
 */
double parseTimeToSeconds(char timeStr[]) {
    int hour = 0, minute = 0, second = 0;
    double totalSec = 0.0;

    if (sscanf(timeStr, " %d:%d:%d", &hour, &minute, &second) == 3) {
        totalSec = (hour * 3600.0) + (minute * 60.0) + second;
    }

    return totalSec;
}

/**
 * This function compares two dates to know its order 
 * @param d1 is the of the first date
 * @param m1 is the month of the first date
 * @param y1 is the year of the first date
 * @param d2 is the day of the second date
 * @param m2 is the month of the second date
 * @param y2 is the year of the second date
 * @return whether the first date is earlier or later than the second
 */
int compareDates(int d1, int m1, int y1, int d2, int m2, int y2) {
    int cmp = 0;

    if (y1 != y2) {
        if (y1 < y2) {
            cmp = -1;
        } 
        else {
            cmp = 1;
        }
    } 
    else if (m1 != m2) {
        if (m1 < m2) {
            cmp = -1;
        } 
        else {
            cmp = 1;
        }
    } 
    else if (d1 != d2) {
        if (d1 < d2) {
            cmp = -1;
        } else {
            cmp = 1;
        }
    }

    return cmp;
}

/**
 * This function generates the privilege records based on the specific criteria
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
 * @param PrivilegeList is the array that will store the generated privilege records.
 * @return the number of records generated.
 */
int buildPrivilegeRecords(struct AccountDetails *RegisteredAcc, int activeAccs, PrivilegeRecord PrivilegeList[]) {
    int recordCount = 0;
    time_t now = time(NULL);
    struct tm *localTime = localtime(&now);
    int curYear = 2026, curMonth = 11, curDay = 27, lastMonth = 0, lastYear = 0, daysInLastMonth = 0;

    if (localTime != NULL) {
        curYear = localTime->tm_year + 1900;
        curMonth = localTime->tm_mon + 1;
        curDay = localTime->tm_mday;
    }

    if (curMonth == 1) {
        lastMonth = 12;
        lastYear = curYear - 1;
    } 
    else {
        lastMonth = curMonth - 1;
        lastYear = curYear;
    }

    daysInLastMonth = countDaysBetween(1, lastMonth, lastYear, 1, curMonth, curYear);
    if (daysInLastMonth <= 0) {
        daysInLastMonth = 30;
    }

    for (int i = 0; i < activeAccs; i++) {
        int day = 0, month = 0, year = 0, openDay = 0, openMonth = 0, openYear = 0, openDateFound = 0;
        double totalDailyBalance = 0.0, aveDailyBal = 0.0;

        for (int h = 0; h < RegisteredAcc[i].HistoryCount; h++) {
            Transaction tx = RegisteredAcc[i].History[h];

            if (sscanf(tx.date, " %d/%d/%d", &day, &month, &year) == 3) {
                if (!openDateFound || compareDates(openDay, openMonth, openYear, day, month, year) > 0){
                    openDay = day;
                    openMonth = month;
                    openYear = year;
                    openDateFound = 1;
                }
            }
        }

        for (int d = 1; d <= daysInLastMonth; d++) {
            double endOfDayBalance = 0.0, bestTimeSec = -1.0;
            int bestDay = 0, bestMonth = 0, bestYear = 0, bestTxFound = 0;

            for (int h = 0; h < RegisteredAcc[i].HistoryCount; h++) {
                Transaction tx = RegisteredAcc[i].History[h];

                if (sscanf(tx.date, " %d/%d/%d", &day, &month, &year) == 3) {
                    if (compareDates(day, month, year, d, lastMonth, lastYear) <= 0) {
                        double txTimeSec = parseTimeToSeconds(tx.time);
                        int newerTx = 0;

                        if (!bestTxFound) {
                            newerTx = 1;
                        } 
                        else if (compareDates(day, month, year, bestDay, bestMonth, bestYear) > 0) {
                            newerTx = 1;
                        } 
                        else if (day == bestDay && month == bestMonth && year == bestYear &&
                                   txTimeSec >= bestTimeSec) {
                            newerTx = 1;
                        }

                        if (newerTx) {
                            endOfDayBalance = tx.balance;
                            bestDay = day;
                            bestMonth = month;
                            bestYear = year;
                            bestTimeSec = txTimeSec;
                            bestTxFound = 1;
                        }
                    }
                }
            }

            totalDailyBalance += endOfDayBalance;
        }

        aveDailyBal = totalDailyBalance / daysInLastMonth;

        if (aveDailyBal >= 100000.0 && openDateFound) {
            PrivilegeRecord newRecord;
            snprintf(newRecord.AccountName, sizeof(newRecord.AccountName), "%s", RegisteredAcc[i].AccountName);

            newRecord.AccountAge = countDaysBetween(openDay, openMonth, openYear, curDay, curMonth, curYear);
            newRecord.CurrentBalance = RegisteredAcc[i].Balance;
            newRecord.AverageDailyBalance = (float)aveDailyBal;

            if (aveDailyBal >= 1000000.0) {
                snprintf(newRecord.PrivilegeTier, sizeof(newRecord.PrivilegeTier), "Platinum");
            } 
            else if (aveDailyBal >= 500000.0) {
                snprintf(newRecord.PrivilegeTier, sizeof(newRecord.PrivilegeTier), "Gold");
            } 
            else {
                snprintf(newRecord.PrivilegeTier, sizeof(newRecord.PrivilegeTier), "Silver");
            }

            PrivilegeList[recordCount] = newRecord;
            recordCount++;
        }
    }

    for (int i = 0; i < recordCount - 1; i++) {
        for (int j = 0; j < recordCount - 1 - i; j++) {
            if (PrivilegeList[j].AverageDailyBalance < PrivilegeList[j + 1].AverageDailyBalance) {
                PrivilegeRecord temp = PrivilegeList[j];
                PrivilegeList[j] = PrivilegeList[j + 1];
                PrivilegeList[j + 1] = temp;
            }
        }
    }

    return recordCount;
}

/**
 * This function saves the privilege report records to a file
 * @param PrivilegeList is the array that will store the generated privilege records
 * @param recordCount is the number of privilege records
 * @param filename is the name of file where the data will be saved
 */
void savePrivilegeRecords(PrivilegeRecord PrivilegeList[], int recordCount, char *filename) {
    FILE *fOut = fopen(filename, "w");

    if (fOut != NULL) {
        fprintf(fOut, "AccountName,AccountAge,CurrentBalance,AverageDailyBalance,PrivilegeTier\n");
        
        for (int i = 0; i < recordCount; i++) {
            fprintf(fOut, "%s,%d,%.2f,%.2f,%s\n",
                    PrivilegeList[i].AccountName,
                    PrivilegeList[i].AccountAge,
                    PrivilegeList[i].CurrentBalance,
                    PrivilegeList[i].AverageDailyBalance,
                    PrivilegeList[i].PrivilegeTier);
        }

        fclose(fOut);
    } 
    else {
        printf("Error: Could not open %s for writing.\n", filename);
    }
}

/**
 * This function diplays a preview of the privilege report records
 * @param PrivilegeList is the array that stores the generated privilege records
 * @param recordCount is the number of privilege records
 */
void displayPrivilegeReport(PrivilegeRecord PrivilegeList[], int recordCount) {
    int previewCount = recordCount;

    if (previewCount > 2) {
        previewCount = 2;
    }

    printf("\nReport 1: Banking Privilege Tiers\n");
    if (previewCount == 0) {
        printf("No qualifying records found.\n");
    } 
    else {
        printf("%-25s | %10s | %15s | %19s | %-12s\n",
               "AccountName", "AccountAge", "CurrentBalance", "AverageDailyBalance", "PrivilegeTier");
        for (int i = 0; i < previewCount; i++) {
            printf("%-25s | %10d | %15.2f | %19.2f | %-12s\n",
                   PrivilegeList[i].AccountName,
                   PrivilegeList[i].AccountAge,
                   PrivilegeList[i].CurrentBalance,
                   PrivilegeList[i].AverageDailyBalance,
                   PrivilegeList[i].PrivilegeTier);
        }
    }
    printf("\n(Full table exported to %s)\n", PRIVILEGE_REPORT);
}

/**
 * This function calcultes the daily interest the account has earned
 * @param balance is the account's balance.
 * @param ratePercent is the annual interest rate
 * @return the daily interest amount
 */
double dailyInterestAmount(double balance, int ratePercent) {
    double annualRate = ratePercent / 100.0;
    double rawInterest = 0.0, roundedInterest = 0.0;

    if (annualRate <= 0.0) {
        annualRate = 0.05;
    }

    rawInterest = balance * (annualRate / 365.0);
    roundedInterest = round(rawInterest * 100.0) / 100.0;
    return roundedInterest;
}

/**
 * This function generates the Interest Expense records based on the specific criteria
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
 * @param InterestList is the array that will store the generated interest records.
 * @return the number of records generated.
 */
int buildInterestRecords(struct AccountDetails *RegisteredAcc, int activeAccs, InterestRecord InterestList[]) {
    int recordCount = 0;

    for (int i = 0; i < activeAccs; i++) {
        double currBalance = RegisteredAcc[i].Balance;

        if (isfinite(currBalance) && currBalance >= 10000.0) {
            double projectedBal = currBalance;
            double expectedInterest = 0.0;
            InterestRecord newRecord;

            for (int day = 1; day <= 30; day++) {
                double dailyInt = dailyInterestAmount(projectedBal, RegisteredAcc[i].InterestRate);
                expectedInterest += dailyInt;
                projectedBal += dailyInt;
            }

            snprintf(newRecord.AccountName, sizeof(newRecord.AccountName), "%s", RegisteredAcc[i].AccountName);
            newRecord.CurrentBalance = currBalance;
            newRecord.ExpectedInterest = expectedInterest;

            InterestList[recordCount] = newRecord;
            recordCount++;
        }
    }

    for (int i = 0; i < recordCount - 1; i++) {
        for (int j = 0; j < recordCount - 1 - i; j++) {
            if (InterestList[j].ExpectedInterest < InterestList[j + 1].ExpectedInterest) {
                InterestRecord temp = InterestList[j];
                InterestList[j] = InterestList[j + 1];
                InterestList[j + 1] = temp;
            }
        }
    }

    return recordCount;
}

/**
 * This function saves the interest expense records to a file
 * @param InterestList is the array that will store the generated interest records
 * @param recordCount is the number of interest records
 * @param filename is the name of file where the data will be saved
 */
void saveInterestRecords(InterestRecord InterestList[], int recordCount, char *filename) {
    FILE *fOut = fopen(filename, "w");

    if (fOut != NULL) {
        fprintf(fOut, "AccountName,CurrentBalance,ExpectedInterest\n");
        for (int i = 0; i < recordCount; i++) {
            fprintf(fOut, "%s,%.2f,%.2f\n",
                    InterestList[i].AccountName,
                    InterestList[i].CurrentBalance,
                    InterestList[i].ExpectedInterest);
        }
        fclose(fOut);
    } 
    else {
        printf("Error: Could not open %s for writing.\n", filename);
    }
}

/**
 * This function diplays a preview of the interest expense report
 * @param InterestList is the array that stores the generated interest records
 * @param recordCount is the number of interest records
 */
void displayInterestReport(InterestRecord InterestList[], int recordCount) {
    int previewCount = recordCount;

    if (previewCount > 2) {
        previewCount = 2;
    }

    printf("\nReport 2: Interest Expense Projection\n");
    if (previewCount == 0) {
        printf("No qualifying records found.\n");
    } 
    else {
        printf("%-25s | %15s | %16s\n",
               "AccountName", "CurrentBalance", "ExpectedInterest");
        for (int i = 0; i < previewCount; i++) {
            printf("%-25s | %15.2f | %16.2f\n",
                   InterestList[i].AccountName,
                   InterestList[i].CurrentBalance,
                   InterestList[i].ExpectedInterest);
        }
    }
    printf("\n(Full table exported to %s)\n", INTEREST_REPORT);
}

/**
 * This function calculates the turnover ratio of the transactions
 * @param withdrawalTotal is the total amount of withdrawals.
 * @param depositTotal is the total amount of deposits.
 * @return the turnover ratio
 */
double turnoverMetric(double withdrawalTotal, double depositTotal) {
    double ratio = 0.0;

    if (depositTotal > 0.0) {
        ratio = (withdrawalTotal / depositTotal) * 100.0;
    }

    return ratio;
}

/**
 * This function compares two suspicious records by date to determine its order
 * @param a is a pointer to the first suspicious record.
 * @param b is pointer to the second suspicious record.
 * @return a's order relative to b
 */
int suspiciousRecordOrder(SuspiciousRecord *a, SuspiciousRecord *b) {
    int cmp = 0;

    if (a->year != b->year) {
        if (a->year < b->year) {
            cmp = -1;
        } 
        else {
            cmp = 1;
        }
    } 
    else if (a->month != b->month) {
        if (a->month < b->month) {
            cmp = -1;
        } 
        else {
            cmp = 1;
        }
    } 
    else if (a->day != b->day) {
        if (a->day < b->day) {
            cmp = -1;
        } 
        else {
            cmp = 1;
        }
    } 
    else {
        cmp = strcmp(b->RiskFlagLevel, a->RiskFlagLevel);
        if (cmp == 0) {
            cmp = strcmp(a->AccountName, b->AccountName);
        }
    }

    return cmp;
}

/**
 * This function generates the suspicious transaction records based on the specific criteria
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
 * @param SuspiciousList is the array that will store the generated suspicious transaction records.
 * @param maxRecords is the maximum number of suspicious records that can be stored.
 * @return the number of suspicious records generated.
 */
int buildSuspiciousRecords(struct AccountDetails *RegisteredAcc, int activeAccs, SuspiciousRecord SuspiciousList[], int maxRecords) {
    int recordCount = 0;

    for (int i = 0; i < activeAccs; i++) {
        for (int h = 0; h < RegisteredAcc[i].HistoryCount; h++) {
            int day = 0, month = 0, year = 0;
            Transaction tx = RegisteredAcc[i].History[h];

            if (sscanf(tx.date, " %d/%d/%d", &day, &month, &year) == 3) {
                int alreadySeen = 0;

                for (int p = 0; p < h; p++) {
                    int pDay = 0, pMonth = 0, pYear = 0;
                    if (sscanf(RegisteredAcc[i].History[p].date, " %d/%d/%d", &pDay, &pMonth, &pYear) == 3) {
                        if (pDay == day && pMonth == month && pYear == year) {
                            alreadySeen = 1;
                        }
                    }
                }

                if (!alreadySeen && recordCount < maxRecords) {
                    double withdrawalTotal = 0.0, depositTotal = 0.0, velocity = 0.0;
                    int depositCount = 0;
                    SuspiciousRecord rec;

                    for (int k = 0; k < RegisteredAcc[i].HistoryCount; k++) {
                        int kDay = 0, kMonth = 0, kYear = 0;
                        Transaction curTx = RegisteredAcc[i].History[k];

                        if (sscanf(curTx.date, " %d/%d/%d", &kDay, &kMonth, &kYear) == 3) {
                            if (kDay == day && kMonth == month && kYear == year) {
                                if (strcmp(curTx.type, "withdrawal") == 0) {
                                    withdrawalTotal += curTx.amount;
                                } 
                                else if (strcmp(curTx.type, "deposit") == 0) {
                                    depositCount++;
                                    depositTotal += curTx.amount;
                                }
                            }
                        }
                    }

                    velocity = turnoverMetric(withdrawalTotal, depositTotal);

                    snprintf(rec.AccountName, sizeof(rec.AccountName), "%s", RegisteredAcc[i].AccountName);
                    snprintf(rec.Date, sizeof(rec.Date), "%02d/%02d/%04d", day, month, year);
                    rec.day = day;
                    rec.month = month;
                    rec.year = year;
                    rec.TotalWithdrawalAmounts = withdrawalTotal;
                    rec.NumberOfDeposits = depositCount;
                    rec.TotalDepositAmounts = depositTotal;
                    rec.TurnoverVelocity = velocity;
                    rec.hasValidVelocity = (depositTotal > 0.0) ? 1 : 0;

                    if (withdrawalTotal >= 500000.0 || (depositTotal > 0.0 && velocity >= 90.0)) {
                        snprintf(rec.RiskFlagLevel, sizeof(rec.RiskFlagLevel), "Suspicious");
                    } 
                    else {
                        snprintf(rec.RiskFlagLevel, sizeof(rec.RiskFlagLevel), "Normal");
                    }

                    SuspiciousList[recordCount] = rec;
                    recordCount++;
                }
            }
        }
    }

    for (int i = 0; i < recordCount - 1; i++) {
        for (int j = 0; j < recordCount - 1 - i; j++) {
            if (suspiciousRecordOrder(&SuspiciousList[j], &SuspiciousList[j + 1]) > 0) {
                SuspiciousRecord temp = SuspiciousList[j];
                SuspiciousList[j] = SuspiciousList[j + 1];
                SuspiciousList[j + 1] = temp;
            }
        }
    }

    return recordCount;
}

/**
 * This function saves the suspicious transaction records to a file
 * @param SuspiciousList is the array that will store the generated suspicious transaction records
 * @param recordCount is the number of suspicious transaction records
 * @param filename is the name of file where the data will be saved
 */
void saveSuspiciousRecords(SuspiciousRecord SuspiciousList[], int recordCount, char *filename) {
    FILE *fOut = fopen(filename, "w");

    if (fOut != NULL) {
        fprintf(fOut, "AccountName,Date,TotalWithdrawalAmounts,NumberOfDeposits,TotalDepositAmounts,TurnoverVelocity (%%),RiskFlagLevel\n");
        
        for (int i = 0; i < recordCount; i++) {
            if (SuspiciousList[i].hasValidVelocity) {
                fprintf(fOut, "%s,%s,%.2f,%d,%.2f,%.2f,%s\n",
                        SuspiciousList[i].AccountName,
                        SuspiciousList[i].Date,
                        SuspiciousList[i].TotalWithdrawalAmounts,
                        SuspiciousList[i].NumberOfDeposits,
                        SuspiciousList[i].TotalDepositAmounts,
                        SuspiciousList[i].TurnoverVelocity,
                        SuspiciousList[i].RiskFlagLevel);
            } 
            else {
                fprintf(fOut, "%s,%s,%.2f,%d,%.2f,,%s\n",
                        SuspiciousList[i].AccountName,
                        SuspiciousList[i].Date,
                        SuspiciousList[i].TotalWithdrawalAmounts,
                        SuspiciousList[i].NumberOfDeposits,
                        SuspiciousList[i].TotalDepositAmounts,
                        SuspiciousList[i].RiskFlagLevel);
            }
        }

        fclose(fOut);
    } 

    else {
        printf("Error: Could not open %s for writing.\n", filename);
    }
}

/**
 * This function diplays a preview of the suspicious transaction report
 * @param SuspiciousList is the array that stores the generated suspicious transaction records
 * @param recordCount is the number of suspicious transaction records
 */
void displaySuspiciousReport(SuspiciousRecord SuspiciousList[], int recordCount) {
    int previewCount = recordCount;

    if (previewCount > 2) {
        previewCount = 2;
    }

    printf("\nReport 3: Suspicious Client Behavior\n");

    if (previewCount == 0) {
        printf("No qualifying records found.\n");
    } 
    else {
        printf("%-22s | %-10s | %18s | %12s | %16s | %18s | %-12s\n",
               "AccountName", "Date", "TotalWithdrawals", "NumDeposits",
               "TotalDeposits", "TurnoverVelocity%", "RiskFlagLevel");

        for (int i = 0; i < previewCount; i++) {
            if (SuspiciousList[i].hasValidVelocity) {
                printf("%-22s | %-10s | %18.2f | %12d | %16.2f | %18.2f | %-12s\n",
                       SuspiciousList[i].AccountName,
                       SuspiciousList[i].Date,
                       SuspiciousList[i].TotalWithdrawalAmounts,
                       SuspiciousList[i].NumberOfDeposits,
                       SuspiciousList[i].TotalDepositAmounts,
                       SuspiciousList[i].TurnoverVelocity,
                       SuspiciousList[i].RiskFlagLevel);
            } 
            else {
                printf("%-22s | %-10s | %18.2f | %12d | %16.2f | %18s | %-12s\n",
                       SuspiciousList[i].AccountName,
                       SuspiciousList[i].Date,
                       SuspiciousList[i].TotalWithdrawalAmounts,
                       SuspiciousList[i].NumberOfDeposits,
                       SuspiciousList[i].TotalDepositAmounts,
                       "N/A",
                       SuspiciousList[i].RiskFlagLevel);
            }
        }
    }

    printf("\n(Full table exported to %s)\n", SUSPICIOUS_REPORT);
}

/**
 * This function generates the balance change records for all registered accounts
 * @param RegisteredAcc is an array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
 * @param ChangeList is the array where the generated balance change records will be stored
 * @return the number of balance change records generated.
 */
int buildBalanceChangeRecords(struct AccountDetails *RegisteredAcc, int activeAccs, BalanceChangeRecord ChangeList[]) {
    int recordCount = 0;

    for (int i = 0; i < activeAccs; i++) {
        double openingBal = 0.0, netChange = 0.0, pctChange = 0.0;
        double currentBal = RegisteredAcc[i].Balance;
        BalanceChangeRecord rec;

        if (RegisteredAcc[i].HistoryCount > 0) {
            openingBal = RegisteredAcc[i].History[0].balance;
        } 
        else {
            openingBal = currentBal;
        }

        netChange = currentBal - openingBal;

        if (openingBal != 0.0) {
            pctChange = (netChange / openingBal) * 100.0;
        }

        rec.AccountNumber = RegisteredAcc[i].AccountNumber;
        snprintf(rec.AccountName, sizeof(rec.AccountName), "%s", RegisteredAcc[i].AccountName);
        rec.OpeningBalance = openingBal;
        rec.CurrentBalance = currentBal;
        rec.NetChange = netChange;
        rec.PercentChange = pctChange;

        ChangeList[recordCount] = rec;
        recordCount++;
    }

    for (int i = 0; i < recordCount - 1; i++) {
        for (int j = 0; j < recordCount - 1 - i; j++) {
            if (ChangeList[j].NetChange < ChangeList[j + 1].NetChange) {
                BalanceChangeRecord temp = ChangeList[j];
                ChangeList[j] = ChangeList[j + 1];
                ChangeList[j + 1] = temp;
            }
        }
    }

    return recordCount;
}

/**
 * This function saves the balance change records to a file
 * @param ChangeList is the array that will store the generated balance change records
 * @param recordCount is the number of balance change records
 * @param filename is the name of file where the data will be saved
 */
void saveBalanceChangeRecords(BalanceChangeRecord ChangeList[], int recordCount, char *filename) {
    FILE *fOut = fopen(filename, "w");

    if (fOut != NULL) {
        fprintf(fOut, "AccountNumber,AccountName,OpeningBalance,CurrentBalance,NetChange,PercentChange\n");

        for (int i = 0; i < recordCount; i++) {
            fprintf(fOut, "%d,%s,%.2f,%.2f,%.2f,%.2f\n",
                    ChangeList[i].AccountNumber,
                    ChangeList[i].AccountName,
                    ChangeList[i].OpeningBalance,
                    ChangeList[i].CurrentBalance,
                    ChangeList[i].NetChange,
                    ChangeList[i].PercentChange);
        }

        fclose(fOut);
    } 
    else {
        printf("Error: Could not open %s for writing.\n", filename);
    }
}

/**
 * This function diplays a preview of the balance change report
 * @param ChangeList is the array that stores the generated balance change records
 * @param recordCount is the number of balance change records
 */
void displayBalanceChangeReport(BalanceChangeRecord ChangeList[], int recordCount) {
    int previewCount = recordCount;

    if (previewCount > 2) {
        previewCount = 2;
    }

    printf("\nReport 4: First-vs-Last Balance Change\n");

    if (previewCount == 0) {
        printf("No qualifying records found.\n");
    } 
    else {
        printf("%-13s | %-22s | %14s | %14s | %12s | %13s\n",
               "AccountNumber", "AccountName", "OpeningBalance",
               "CurrentBalance", "NetChange", "PercentChange");

        for (int i = 0; i < previewCount; i++) {
            printf("%-13d | %-22s | %14.2f | %14.2f | %12.2f | %13.2f\n",
                   ChangeList[i].AccountNumber,
                   ChangeList[i].AccountName,
                   ChangeList[i].OpeningBalance,
                   ChangeList[i].CurrentBalance,
                   ChangeList[i].NetChange,
                   ChangeList[i].PercentChange);
        }
    }
    printf("\n(Full table exported to %s)\n", BALANCE_CHANGE_REPORT);
}

/**
 * This function exports a summary about the bank transaction data
 * @param RegisteredAcc is the array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
 * @param InterestList is the array the contains the generated interest records
 * @param interestCount is number of generated interest records
 * @param filename is the name of the JSON file
 */
void saveSummaryToJson(struct AccountDetails *RegisteredAcc, int activeAccs, InterestRecord InterestList[], int interestCount, char *filename) {
    FILE *fOut = fopen(filename, "w");
    double totalDeposits = 0.0, totalWithdrawals = 0.0, totalBalance = 0.0, totalInterestExpense = 0.0;
    int i = 0;

    if (fOut != NULL) {
        for (i = 0; i < activeAccs; i++) {
            totalBalance += (double)RegisteredAcc[i].Balance;
        }

        for (i = 0; i < interestCount; i++) {
            totalInterestExpense += InterestList[i].ExpectedInterest;
        }

        fprintf(fOut, "{\n");
        fprintf(fOut, "  \"total_accounts\": %d,\n", activeAccs);
        fprintf(fOut, "  \"total_deposits\": %.2f,\n", totalDeposits);
        fprintf(fOut, "  \"total_withdrawals\": %.2f,\n", totalWithdrawals);
        fprintf(fOut, "  \"total_balance\": %.2f,\n", totalBalance);
        fprintf(fOut, "  \"total_interest_expense\": %.2f\n", totalInterestExpense);
        fprintf(fOut, "}\n");
        fclose(fOut);
    } 
    else {
        printf("Error: Could not open %s for writing.\n", filename);
    }
}

/**
 * This function displays the formatted summary
 * @param RegisteredAcc is the array that stores information of all the accounts in the system
 * @param activeAccs is the number of accounts currently registered in the system
 * @param InterestList is the array the contains the generated interest records
 * @param interestCount is number of generated interest records
 */
void displaySummaryJson(struct AccountDetails *RegisteredAcc, int activeAccs, InterestRecord InterestList[], int interestCount) {
    int totalDeposits = 0, totalWithdrawals = 0;
    double totalBalance = 0.0, totalInterestExpense = 0.0;
    int i = 0, h = 0;

    for (i = 0; i < activeAccs; i++) {
        totalBalance += (double)RegisteredAcc[i].Balance;

        for (h = 0; h < RegisteredAcc[i].HistoryCount; h++) {
            if (strcmp(RegisteredAcc[i].History[h].type, "deposit") == 0) {
                totalDeposits++;
            } 
            else if (strcmp(RegisteredAcc[i].History[h].type, "withdrawal") == 0) {
                totalWithdrawals++;
            }
        }
    }

    for (i = 0; i < interestCount; i++) {
        totalInterestExpense += InterestList[i].ExpectedInterest;
    }

    printf("\nSummary State (summary.json):\n"
           "{\n"
           "  \"total_accounts\": %d,\n"
           "  \"total_deposits\": %d,\n"
           "  \"total_withdrawals\": %d,\n"
           "  \"total_balance\": %.2f,\n"
           "  \"total_interest_expense\": %.2f\n"
           "}\n",
           activeAccs, totalDeposits, totalWithdrawals, totalBalance, totalInterestExpense);
}