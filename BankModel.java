//********************
// Last names: Sauz
// Language: Java
// Paradigm(s): Object Oriented Programming following MVC structure
//********************

/**
 * Represents the Model component of the MVC banking application.
 * Manages registered bank accounts using an ArrayList and handles
 * account validation, deposits, withdrawals, currenxy exchange, and interest calculation.
 */
import java.util.ArrayList;

public class BankModel{
    
    //----------------
    // ATTRIBUTES    -
    //----------------

    private int choice;
    private String accName;

    private ArrayList<Account> accounts;

    private double depositAmt;
    private double withdrawAmt;

    private int selectedForeignCurrency;
    private double sourceAmt;
    private int sourceCurrency;

    // Default Values
    private static final double DEFAULT_BALANCE = 1000.00;
    private static final String DEFAULT_CURRENCY = "PHP";
    private static final double ANNUALINTEREST_RATE = 0.05;

    // Currency
    private String[] currencyTypes = {"Philippine Peso", "United States Dollar", "Japanese Yen", "British Pound Sterling", "Euro", "Chinese Yuan Renminni"};
    private String[] currencyCodes = {"PHP", "USD", "JPY", "GBP", "EUR", "CNY"};
    private double[] exchangeRates = {1.00, 62.00, 0.40, 84.00, 72.00, 9.00};

    //----------------
    // CONSTRUCTOR   -
    //----------------

    public BankModel(){
        choice = 0;
        accName = "";
        accounts = new ArrayList<Account>();
        depositAmt = 0.00;
        withdrawAmt = 0.00;
        selectedForeignCurrency = 0;
        sourceAmt = 0.00;
    }

    //----------------
    // SETTERS       -
    //----------------

    public void setChoice(int choice){
        this.choice = choice;
    }

    public void setAccName(String accName){
        this.accName = accName;
    }

    /**
     * Deposits an amt into the currently selected acc after amt validation
     * @param depositAmt is the amount to be added to the account balance
     */
    public void setDepositAmt(double depositAmt){
        Account account = findAccount(accName);

        if(account != null && isValidAmount(depositAmt)){
            this.depositAmt = depositAmt;

            double newBalance = account.getCurrentBalance() + depositAmt;

            newBalance = Math.round(newBalance * 100.0) / 100.0;

            account.setCurrentBalance(newBalance);        }
    }

    /**
     * Withdraws an amt from the currently selected acc if the amt
     * is valid and has sufficient balance
     * @param withdrawAmt is the amount to be deducted from the acc balance
     */
    public void setWithdrawAmt(double withdrawAmt){
        Account account = findAccount(accName);

        if(account != null && isValidWithdraw(withdrawAmt)){
            this.withdrawAmt = withdrawAmt;

            double newBalance = account.getCurrentBalance() - withdrawAmt;

            newBalance = Math.round(newBalance * 100.0) / 100.0;        

            account.setCurrentBalance(newBalance);        }
    }

    public void setSelectedForeignCurrency(int selectedForeignCurrency){
        this.selectedForeignCurrency = selectedForeignCurrency;
    }

    public void setSourceAmt(double sourceAmt){
        this.sourceAmt = sourceAmt;
    }

    public void setExchangeRate(int currencyChoice, double exchangeRate){
        exchangeRates[currencyChoice - 1] = exchangeRate;
    }
    
    public void setSourceCurrency(int sourceCurrency){
        this.sourceCurrency = sourceCurrency;
    }

    //----------------
    // GETTERS       -
    //----------------
   
    public int getChoice(){
        return choice;
    }

    public String getAccName(){
        return accName;
    }

    public double getDepositAmt(){
        return depositAmt;
    }

    public double getWithdrawAmt(){
        return withdrawAmt;
    }

    /**
     * Retrieves the current balance of the selected registered acc
     * 
     * @return the current balance of the selected acc
     * @throws IllegalStateException if no registered account is selected.
     */
    public double getCurrentBalance(){
        
        Account account = findAccount(accName);

        if(account != null){
            return account.getCurrentBalance();
        }
        
        throw new IllegalStateException("No registered account selected");
    }

    public int getSelectedForeignCurrency(){
        return selectedForeignCurrency;
    }

    public double getSourceAmt(){
        return sourceAmt;
    }

    public double getDefaultBalance(){
        return DEFAULT_BALANCE;
    }

    public String getDefaultCurrency(){
        return DEFAULT_CURRENCY;
    }

    public String[] getCurrencyTypes(){
        return currencyTypes;
    }

    public String[] getCurrencyCodes(){
        return currencyCodes;
    }

    public double[] getExchangeRates(){
        return exchangeRates;
    }

    public double getAnnualInterestRate(){
        return ANNUALINTEREST_RATE;
    }

    public int getSourceCurrency(){
        return sourceCurrency;
    }

    //---------------------
    // VALIDATION METHODS -
    //---------------------

    public boolean isValidChoice(){
        return choice >= 1 && choice <=6;
    }

    public boolean isValidCurrencyChoice(){
        return selectedForeignCurrency >= 1 && selectedForeignCurrency <= 6;
    }

    public boolean isValidCurrencyChoice(int currencyChoice){
        return currencyChoice >= 1 && currencyChoice <= 6;                                                                  
    }

    public boolean isValidAmount(double amount){
        return amount > 0;
    }

    public boolean isValidWithdraw(double amount){
        return isValidAmount(amount) && Math.round(amount * 100.0) <= Math.round(getCurrentBalance() * 100.0); 
    }

    public boolean isValidReturnMenuChoice(String input){
        return input.equals("Y") || input.equals("N");
    }

    public boolean isValidAccountName(String accName){
        if(accName == null){
            return false;
        }

        /*  
        *  RegEx 
        *  \p{L}+              = one or more letters
        *  ( \\p{L}+)*         = optional additional words seperated by one space
        *  ,                   = requires exactly a comma followed by one space
        *  \\p{L}+( \\p{L}+)*  = validates the first-name portion
        */
        return accName.trim().matches("\\p{L}+( \\p{L}+)*, \\p{L}+( \\p{L}+)*");
    }

    public boolean isValidInterestDays(int days){
        return days >= 1 && days <= 365 ;
    }

    //-----------------------
    // CALCULATION METHODS  -
    //-----------------------

    /**
     * Converts an amount from the source currency to the target currency via 
     * corresponding exchange rates
     * @param sourceCurrency : the index of the source currency
     * @param exchangeCurrency : the index of the target currency
     * @param sourceAmt : the amount to be converted
     * @return the converted amount in target currency
     */
    public double calculateCurrExchange(int sourceCurrency, int exchangeCurrency, double sourceAmt){
        double sourceRate = exchangeRates[sourceCurrency - 1];
        double exchangeRate = exchangeRates[exchangeCurrency - 1];
        
        return sourceAmt * sourceRate / exchangeRate;
    }

    /**
     * Calculates the daily interest based on the annual interest rate.
     * Rounds the resulting interest into two decimal places.
     * 
     * @param balance : balance used to calculate daily interest
     * @return the calculated daily interest amount
     */
    public double calculateDailyInterest(double balance){
        double interest = balance * (ANNUALINTEREST_RATE / 365);
        return Math.round(interest * 100.0) / 100.0;
    }

    /**
     * Calculates the daily interest earned over a specific number of days/
     * Uses the projected balance from the previous day for each computation.
     * 
     * @param days : the number of days for the interest computation
     * @return an array containing the calculated interest for each day
     */
    public double[] calculateInterestAmt(int days){
        double [] interests = new double[days];
        double balance = getCurrentBalance();
        for(int i = 0; i < days; i++){
            double interest = calculateDailyInterest(balance);
            interests[i] = interest;
            balance = Math.round((balance + interest) * 100.0) / 100.0;
        }
        return interests;
    }

    /**
     * Calculates the projected acc balance after each day's interest.
     * Does not modify the account's actual stored balance.
     * 
     * @param days : the number of days for interest computation
     * @return an array containing the projected balance for each day
     */
    public double[] calculateInterestBal(int days){
        double balances[] = new double[days];
        double balance = getCurrentBalance();
        for (int i = 0; i < days; i++){
            double interest = calculateDailyInterest(balance);
            balance = Math.round((balance + interest) * 100.0) / 100.0;
            balances[i] = balance;
        }
        return balances;
    }

    //---------------------
    // ACCOUNT HANDLING   -
    //---------------------

    /**
     * Searches the ArrayList for an account matching the given name.
     * Ignores capitalization and leading/trailing spaces
     * 
     * @param accName : the account name to search for
     * @return the matching Account object, or null if no account is found
     */
    public Account findAccount(String accName){
        if(accName == null){
            return null;
        }

        for(Account account : accounts){
            if(account.getAccName().equalsIgnoreCase(accName.trim())){
                return account;
            }
        }
        return null;
    }

    /**
     * Registers a new account with default balance.
     * Prevents registration of duplicate/invalid names.
     * 
     * @param accName : the name of the account to register
     * @return true if registration successful, otherwise false
     */
    public boolean registerAccount(String accName){
        if(!isValidAccountName(accName) || isAccRegistered(accName)){
            return false;
        }

        Account newAccount = new Account(accName.trim(), DEFAULT_BALANCE);
        accounts.add(newAccount);
        return true;
    }

    /**
     * Checks if an account name is valid and already registered
     * 
     * @param accName : the account name to check
     * @return true if account exists, otherwise false
     */
    public boolean isAccRegistered(String accName){
        return isValidAccountName(accName) && findAccount(accName) != null;
    }

}
