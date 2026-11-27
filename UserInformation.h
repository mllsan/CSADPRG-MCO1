/* definitions */
#define MAX_HISTORY 500

/**
* Stores a single transaction entry
*/
typedef struct TransactionDetails {
    char date[32];
    char time[32];
    float amount;
    char type[32];
    double balance;
} Transaction;

/**
* Stores the user's account information
*/
typedef struct AccountDetails {
    char AccountName[100];
    int AccountNumber;
    float Balance;
    char Currency[4];
    int InterestRate;
    Transaction History[MAX_HISTORY];
    int HistoryCount;
} Account;