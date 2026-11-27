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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BankModel{
    
    //----------------
    // ATTRIBUTES    -
    //----------------

    private int choice;
    private String accName;

    private ArrayList<Account> accounts;
    
    private static final String CSV_FILE = "bank_transactions.csv";

    private long latestAccNum = 0;
    private int totalRows = 0;
    private int validRows = 0;
    private int invalidRows = 0;
    private int excludedRows = 0;

    private double depositAmt;
    private double withdrawAmt;

    private int selectedForeignCurrency;
    private double sourceAmt;
    private int sourceCurrency;

    // Default Values
    private static final double DEFAULT_BALANCE = 1000.00;
    private static final String DEFAULT_CURRENCY = "PHP";
    private static final double ANNUALINTEREST_RATE = 0.05;
    private static final double MIN_INITIALDEPOSIT = 5000.00;

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
     * @param IOException if transaction cannot be saved
     */
    public void setDepositAmt(double depositAmt) throws IOException{
        Account account = findAccount(accName);

        if(account != null && isValidAmount(depositAmt)){

            double newBalance = account.getCurrentBalance() + depositAmt;

            newBalance = Math.round(newBalance * 100.0) / 100.0;
            saveTransactionData(account, "deposit", depositAmt, newBalance);
            this.depositAmt = depositAmt;
            account.setCurrentBalance(newBalance);        }
    }

    /**
     * Withdraws an amt from the currently selected acc if the amt
     * is valid and has sufficient balance
     * @param withdrawAmt is the amount to be deducted from the acc balance
     * @param IOException if transaction cannot be saved
     */
    public void setWithdrawAmt(double withdrawAmt) throws IOException{
        Account account = findAccount(accName);

        if(account != null && isValidWithdraw(withdrawAmt)){
        
            double newBalance = account.getCurrentBalance() - withdrawAmt;

            newBalance = Math.round(newBalance * 100.0) / 100.0;        
            saveTransactionData(account, "withdrawal", withdrawAmt, newBalance);

            this.withdrawAmt = withdrawAmt;
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

    public int getTotalRows(){
        return totalRows;
    }

    public int getValidRows(){
        return validRows;
    }

    public int getInvalidRows(){
        return invalidRows;
    }

    public int getExcludedRows(){
        return excludedRows;
    }
    //---------------------
    // VALIDATION METHODS -
    //---------------------

    public boolean isValidChoice(){
        return choice >= 1 && choice <=7;
    }

    public boolean isValidCurrencyChoice(){
        return selectedForeignCurrency >= 1 && selectedForeignCurrency <= 6;
    }

    public boolean isValidCurrencyChoice(int currencyChoice){
        return currencyChoice >= 1 && currencyChoice <= 6;                                                                  
    }

    public boolean isValidAmount(double amount){
        return amount > 0 && amount == Math.round(amount*100.0)/100.0;
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
        *  \\p{L}+             = one or more letters for first name
        *  ( \\p{L}+)+"        = requires at least one additional name, with each word separated by a space
        */
        return accName.trim().matches("\\p{L}+( \\p{L}+)+");
    }

    public boolean isValidInterestDays(int days){
        return days >= 1 && days <= 365 ;
    }

    public boolean isValidInitialDeposit(double amount){
        return Double.isFinite(amount) && amount >= MIN_INITIALDEPOSIT;
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
     * Validates minimum initial deposits and saves the 
     * account and its initial transaction to the CSV file
     * 
     * @param accName : the name of the account to register
     * @return true if registration successful, otherwise false
     */
    public boolean registerAccount(String accName, double initialDeposit) throws IOException{
        if(!isValidAccountName(accName) || isAccRegistered(accName) || !isValidInitialDeposit(initialDeposit)){
            return false;
        }

        if(latestAccNum > 9999999999L){
            return false;
        }
        
        long nextAccNumber = latestAccNum + 1;
        
        String accNumber = String.format("%010d", nextAccNumber);

        Account newAccount = new Account(accName.trim(), accNumber, initialDeposit);
        saveTransactionData(newAccount, "deposit", initialDeposit, initialDeposit);
        accounts.add(newAccount);
        latestAccNum = nextAccNumber;

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

    /**
     * Searches for registered accounts via acc number
     * @param accNum : account number to look for
     * @return matching account object, or null if not found
     */

    public Account findAccViaNum(String accNum){
        for(Account account: accounts){
            if(account.getAccNumber().equals(accNum)){
                return account;
            }
        }
        return null;
    }

    //---------------------
    // CSV FILE HANDLING  -
    //---------------------

    /**
     * Reads and validates transactions from CSV file.
     * Loads PHP accounts and their balances to the ArrayList
     * Counts valid, invalid, and excluded transaction rows
     * 
     * @throws IOException : if the CSV file cannot be read or accessed.
     */
    public void loadTransactionData() throws IOException{
        accounts.clear();

        totalRows = 0;
        validRows = 0;
        invalidRows = 0;
        excludedRows = 0;
        latestAccNum = 0;

        // Reading + Validating dates from CSV file
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern ("H:mm:ss");

        try(BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))){
            String line = reader.readLine();

            while((line = reader.readLine()) != null){
                totalRows++;
                String[] data = line.split(",", -1);

                if(data.length != 8){
                    invalidRows++;
                } else {
                    try{
                        String accountName = data[0].trim();
                        String accNumber = data[1].trim();
                        String currency = data[4].trim();
                        String type = data[6].trim();

                        if(accountName.isEmpty() || !accNumber.matches("[0-9]{10}") || type.isEmpty()){
                            invalidRows++;
                        } else {
                            LocalDate date = LocalDate.parse(data[2].trim(), dateFormat);
                            LocalTime time = LocalTime.parse(data[3].trim(), timeFormat);

                            double amount = Double.parseDouble(data[5].trim());
                            double balance = Double.parseDouble(data[7].trim());

                            if(!Double.isFinite(amount) || !Double.isFinite(balance) || amount <= 0 || balance < 0 || (!type.equals("deposit") && !type.equals("withdrawal"))){
                                invalidRows++;
                            } else {
                                long num = Long.parseLong(accNumber);
                                if(num > latestAccNum){
                                    latestAccNum = num;
                                }

                                if(!currency.equalsIgnoreCase("PHP")){
                                    excludedRows++;
                                } else {
                                    Account account = findAccViaNum(accNumber);

                                    if(account == null){
                                        account = new Account(accountName, accNumber, balance);
                                        account.setlatest(date);
                                        account.setTime(time);
                                        accounts.add(account);
                                    } else {
                                        LocalDate cur_date = account.getLatestDate();
                                        LocalTime cur_time = account.getLatestTime();

                                        if(cur_date == null || cur_time == null || date.isAfter(cur_date) || (date.isEqual(cur_date) && time.isAfter(cur_time))) {
                                            account.setCurrentBalance(balance);
                                            account.setlatest(date);
                                            account.setTime(time);
                                        }
                                    }
                                    validRows++;
                                }

                            }
                        }
                    }
                    catch(Exception e){
                        invalidRows++;
                    }
                }
            }
        }
    }

    /**
     * Saves every successful transaction to the CSV file
     * 
     * @param account : the acc involved in transaction
     * @param type : type of transaction
     * @param amount : transaction amt
     * @param balance : updated acc balance
     * @throws IOException : if csv file cannot be accessed
     */
    public void saveTransactionData(Account account, String type, double amount, double balance) throws IOException{
        File file = new File(CSV_FILE);
        
        if(!file.isFile() || file.length() == 0){
            throw new IOException("Transaction CSV file not found or file is empty.");
            }
            // Writing current date to the CSV file
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            String transaction = String.join(",", account.getAccName(), account.getAccNumber(), date, time, "PHP", String.format("%.2f", amount), type, String.format("%.2f", balance));
            
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))){
                writer.write(transaction);
                writer.newLine();
            
        }
    }

    //---------------------
    // CSV REPORTS        -
    //---------------------

    /**
     * Banking Privilege Tiers Report
     * Calculates ave daily balances for the previous month, assigns privilege tiers, and exports qualified accounts
     * @return the generated report records
     * @throws IOException if CSV file cannot be accessed
     */
    public ArrayList<String[]> generateReport1() throws IOException{
        ArrayList<String[]> transactionRecords = new ArrayList<String[]>();
        ArrayList<String[]> reportRecords = new ArrayList<String[]>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm:ss");

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(1).withDayOfMonth(1);
        int daysInMonth = startDate.lengthOfMonth();

        try(BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))){
            reader.readLine();

            String line;
            
            while((line = reader.readLine()) != null){
                String[] transactionData = line.split(",", -1);

                if(transactionData.length == 8 && transactionData[4].trim().equalsIgnoreCase("PHP")){
                    try{
                        LocalDate.parse(transactionData[2].trim(), dateFormatter);
                        LocalTime.parse(transactionData[3].trim(), timeFormatter);

                        double amount = Double.parseDouble(transactionData[5].trim());
                        double balance = Double.parseDouble(transactionData[7].trim());

                        if(transactionData[1].trim().matches("[0-9]{10}") && Double.isFinite(amount) && amount > 0 && Double.isFinite(balance) 
                            && balance >= 0 && (transactionData[6].trim().equals("deposit") || transactionData[6].trim().equals("withdrawal"))){
                                transactionRecords.add(transactionData);
                            }
                    } catch(NumberFormatException | DateTimeParseException e){
                        // skip invalid records
                    }
                }
            }
        }
        for(Account account: accounts){
            double totalDailyBalance = 0;
            LocalDate openingDate = null;

            for(String[] transactionData: transactionRecords){
                if(transactionData[1].trim().equals(account.getAccNumber())){
                    LocalDate transactionDate = LocalDate.parse(transactionData[2].trim(), dateFormatter);
                    if(openingDate == null || transactionDate.isBefore(openingDate)){
                        openingDate = transactionDate;
                    }
                }
            }

            for(int i = 0; i < daysInMonth; i++){
                LocalDate currDay = startDate.plusDays(i);
                
                double endOfDayBalance = 0;
                LocalDate latestTransactionDate = null;
                LocalTime latestTransactionTime = null;

                for(String[] transactionData : transactionRecords){

                    if(transactionData[1].trim().equals(account.getAccNumber())){

                        LocalDate transactionDate = LocalDate.parse(transactionData[2].trim(), dateFormatter);

                        if(!transactionDate.isAfter(currDay)){

                            LocalTime time = LocalTime.parse(transactionData[3].trim(), timeFormatter);

                            if(latestTransactionDate == null ||
                            transactionDate.isAfter(latestTransactionDate) ||
                            (transactionDate.equals(latestTransactionDate) && !time.isBefore(latestTransactionTime))){

                                endOfDayBalance = Double.parseDouble(transactionData[7].trim());
                                latestTransactionDate = transactionDate;
                                latestTransactionTime = time;
                            }
                        }
                    }
                }

                totalDailyBalance += endOfDayBalance;
            }

            double aveDailyBal = totalDailyBalance / daysInMonth;

            if(aveDailyBal > 100000 && openingDate != null){

                String privilegeTier;

                if(aveDailyBal >= 1000000){
                    privilegeTier = "Platinum";
                }
                else if(aveDailyBal >= 500000){
                    privilegeTier = "Gold";
                }
                else{
                    privilegeTier = "Silver";
                }

                long accountAge = today.toEpochDay() - openingDate.toEpochDay();

                String[] row = {
                    account.getAccName(), String.valueOf(accountAge), String.format("%.2f", account.getCurrentBalance()),
                    String.format("%.2f", aveDailyBal), privilegeTier
                };

                reportRecords.add(row);
            }
        }

        reportRecords.sort((firstAcc, secondAcc) -> Double.compare(
            Double.parseDouble(secondAcc[3]),
            Double.parseDouble(firstAcc[3])
        ));

        try(BufferedWriter writer = new BufferedWriter(
            new FileWriter("report1_banking_privilege_tiers.csv"))){

            writer.write("AccountName,AccountAge,CurrentBalance,AverageDailyBalance,PrivilegeTier");
            writer.newLine();

            for(String[] reportRow : reportRecords){
                writer.write(String.join(",", reportRow));
                writer.newLine();
                }
            }

    return reportRecords;
    }

    /**
     * Interest Expense Projection Report
     * Calculates expected interest over 30 days for accounts with >= Php 10000 current balance
     * @return generated report records
     * @throws IOException if CSV file cannot be accessed
     */
    public ArrayList<String[]> generateReport2() throws IOException{
        ArrayList<String[]> reportRecords = new ArrayList<String[]>();

        for(Account account: accounts){
            double currBalance = account.getCurrentBalance();

            if(Double.isFinite(currBalance) && currBalance >= 10000){
                double projectedBal = currBalance;
                double expectedInterest = 0;

                for(int i = 1; i <= 30; i++){
                    double dailyInterest = calculateDailyInterest(projectedBal);
                    expectedInterest += dailyInterest;
                    projectedBal += dailyInterest;
                }

                String[] row = {account.getAccName(), String.format("%.2f", currBalance), String.format("%.2f", expectedInterest)};
                reportRecords.add(row);
            }
        }
        reportRecords.sort((firstAcc, secondAcc) -> Double.compare(Double.parseDouble(secondAcc[2]), Double.parseDouble(firstAcc[2])));

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("report2_interest_expense_projection.csv"))){
            writer.write("AccountName,CurrentBalance,ExpectedInterest");
            writer.newLine();

            for(String[] row : reportRecords){
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
        return reportRecords;
    }

    /**
     * Suspicious Client Behavior Report
     * Group trnasactions by acc, date, calculates turnover velocity, and identifies suspicious activity
     * @return generated report records
     * @throws IOException if CSV file cannot be accessed
     */
    public ArrayList<String[]> generateReport3() throws IOException{
        ArrayList<String[]> dailyRecords = new ArrayList<String[]>();
        ArrayList<String[]> reportRecords = new ArrayList<String[]>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm:ss");

        try(BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))){
            reader.readLine();
            String line;

            while((line = reader.readLine()) != null){
                String[] transactionData = line.split(",", -1);

                if(transactionData.length == 8 && transactionData[4].trim().equalsIgnoreCase("PHP")){
                    try{
                        LocalDate.parse(transactionData[2].trim(), dateFormatter);
                        LocalTime.parse(transactionData[3].trim(), timeFormatter);

                        double amount = Double.parseDouble(transactionData[5].trim());
                        double balance = Double.parseDouble(transactionData[7].trim());

                        String transactionType = transactionData[6].trim();
                        String accNumber = transactionData[1].trim();
                        String transactionDate = transactionData[2].trim();

                        if(accNumber.matches("[0-9]{10}") && Double.isFinite(amount) && amount > 0 && Double.isFinite(balance) 
                        && balance >= 0 && (transactionType.equals("deposit") || transactionType.equals("withdrawal"))){

                            String[] dailyRecord = null;

                            for(String[] record : dailyRecords){
                                if(record[1].equals(accNumber) && record[2].equals(transactionDate)){
                                    dailyRecord = record;
                                }
                            }

                            if(dailyRecord == null){
                                dailyRecord = new String[]{transactionData[0].trim(), accNumber, transactionDate, "0", "0", "0"};
                                dailyRecords.add(dailyRecord);
                            }

                            if(transactionType.equals("deposit")){
                                int depositCount = Integer.parseInt(dailyRecord[4]);
                                double totalDeposits = Double.parseDouble(dailyRecord[5]);
                                dailyRecord[4] = String.valueOf(depositCount + 1);
                                dailyRecord[5] = String.valueOf(totalDeposits + amount);
                            } else {
                                double totalWithdrawals = Double.parseDouble(dailyRecord[3]);
                                dailyRecord[3] = String.valueOf(totalWithdrawals + amount);
                            }
                    }
                } catch(NumberFormatException | DateTimeParseException e){
                    // skip invalid records
                }
            }
        }
    }
    for(String[] dailyRecord : dailyRecords){
        double totalWithdrawals = Double.parseDouble(dailyRecord[3]);
        int numDeposit = Integer.parseInt(dailyRecord[4]);
        double totalDeposits = Double.parseDouble(dailyRecord[5]);

        double turnoverVelocity = 0;
        String velocityOutput = "N/A";

        if(totalDeposits > 0){
            turnoverVelocity = (totalWithdrawals / totalDeposits) * 100;
            velocityOutput = String.format("%.2f", turnoverVelocity);
        }
        String riskFlag;

        if(totalWithdrawals >= 500000 || (totalDeposits > 0 && turnoverVelocity >= 90)){
            riskFlag = "Suspicious";
        } else {
            riskFlag = "Normal";
        }
        
        String[] row = {dailyRecord[0], dailyRecord[2], String.format("%.2f", totalWithdrawals), String.valueOf(numDeposit), String.format("%.2f", totalDeposits), velocityOutput, riskFlag};
        reportRecords.add(row);
    }
    reportRecords.sort((firstRecord, secondRecord) -> {
        LocalDate firstDate = LocalDate.parse(firstRecord[1], dateFormatter);
        LocalDate secondDate = LocalDate.parse(secondRecord[1], dateFormatter);

        int compareDate = secondDate.compareTo(firstDate);

        if(compareDate != 0){
            return compareDate;
        }
        return secondRecord[6].compareTo(firstRecord[6]);
    });

    try(BufferedWriter writer = new BufferedWriter(new FileWriter("report3_suspicious_client_behavior.csv"))){
        writer.write("AccountName,Date,TotalWithdrawalAmounts,NumberOfDeposits,TotalDepositAmounts,TurnoverVelocity (%),RiskFlagLevel");
        writer.newLine();

        for(String[] row : reportRecords){
            writer.write(String.join(",", row));
            writer.newLine();
            }
        }
        return reportRecords;
    }

    /**
     * Generates the Most Active Bank Accounts report
     * Counts deposits and withdrawals for each account, sorts by total transactions
     * @return generated report records
     * @throws IOException if CSV file cannot be accessed
     */
    public ArrayList<String[]> generateReport4() throws IOException{
        ArrayList<String[]> transactionRecords = new ArrayList<String[]>();
        ArrayList<String[]> reportRecords = new ArrayList<String[]>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm:ss");

        try(BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))){
            reader.readLine();
            String line;

            while((line = reader.readLine()) != null){
                String[] transactionData = line.split(",", -1);

                if(transactionData.length == 8 && transactionData[4].trim().equalsIgnoreCase("PHP")){
                    try{
                        LocalDate.parse(transactionData[2].trim(), dateFormatter);
                        LocalTime.parse(transactionData[3].trim(), timeFormatter);

                        double amount = Double.parseDouble(transactionData[5].trim());
                        double balance = Double.parseDouble(transactionData[7].trim());

                        if(transactionData[1].trim().matches("[0-9]{10}") && Double.isFinite(amount) && amount > 0 && Double.isFinite(balance) && balance >= 0 &&
                        (transactionData[6].trim().equals("deposit") || transactionData[6].trim().equals("withdrawal"))){
                            transactionRecords.add(transactionData);
                        }
                    } catch(NumberFormatException | DateTimeParseException e){
                        // skip invalid records
                    }
                }

            }
        }
        for(Account account : accounts){
            int numDeposits = 0;
            int numWithdrawals = 0;

            for(String[] transactionData : transactionRecords){
                if(transactionData[1].trim().equals(account.getAccNumber())){
                    if(transactionData[6].trim().equals("deposit")){
                        numDeposits++;
                    } else if(transactionData[6].trim().equals("withdrawal")){
                        numWithdrawals++;
                    }
                }
            }
            int totalTransactions = numDeposits + numWithdrawals;

            String[] row = {account.getAccName(), account.getAccNumber(), String.valueOf(numDeposits), String.valueOf(numWithdrawals), String.valueOf(totalTransactions)};
            reportRecords.add(row);
        }
        reportRecords.sort((firstAcc, secondAcc) -> Integer.compare(Integer.parseInt(secondAcc[4]), Integer.parseInt(firstAcc[4])));

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("report4_most_active_accounts.csv"))){
            writer.write("AccountName,AccountNumber,NumberOfDeposits,NumberOfWithdrawals,TotalTransactions");
            writer.newLine();

            for(String[] row : reportRecords){
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
        return reportRecords;
    }
}



