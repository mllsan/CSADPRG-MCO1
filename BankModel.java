//********************
// Last names: Sauz
// Language: Java
// Paradigm(s): Object Oriented Programming following MVC structure
//********************

public class BankModel{
    
    //----------------
    // ATTRIBUTES    -
    //----------------

    private int choice;
    private String accName;

    private double currentBalance;
    private double depositAmt;
    private double withdrawAmt;

    private int selectedForeignCurrency;
    private double sourceAmt;

    // Default Values
    private static final double DEFAULT_BALANCE = 1000.00;
    private static final String DEFAULT_CURRENCY = "PHP";

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
        currentBalance = DEFAULT_BALANCE;
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

    public void setDepositAmt(double depositAmt){
        this.depositAmt = depositAmt;
        currentBalance = currentBalance + depositAmt;
    }

    public void setWithdrawAmt(double withdrawAmt){
        this.withdrawAmt = withdrawAmt;
        currentBalance = currentBalance - withdrawAmt;
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

    public double getCurrentBalance(){
        return currentBalance;
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

    //---------------------
    // VALIDATION METHODS -
    //---------------------

    public boolean isValidChoice(){
        return choice >= 1 && choice <=6;
    }

    public boolean isValidCurrencyChoice(){
        return selectedForeignCurrency >= 1 && selectedForeignCurrency <= 6;
    }

    public boolean isValidAmount(double amount){
        return amount > 0;
    }

    public boolean isValidWithdraw(double amount){
        return amount > 0 && amount <= currentBalance;
    }
    //-----------------------
    // CALCULATION METHODS  -
    //-----------------------

    public double calculateCurrExchange(int currencyChoice){
        return sourceAmt * exchangeRates[currencyChoice - 1];
    }
}