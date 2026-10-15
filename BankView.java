//********************
// Last names: Sauz
// Language: Java
// Paradigm(s): Object Oriented Programming following MVC structure
//********************

/**
 * Represents the View component of the MVC banking application.
 * Handles user input, displays transaction menus and results, and 
 * presents error messages for invalid inputs.
 */

import java.util.Scanner;

public class BankView{

    private Scanner scanner;

    public BankView(){
        scanner = new Scanner(System.in);
    }
    public void displayMainMenu(){
        System.out.println();
        System.out.println("Select Transaction: ");
        System.out.println("[1] Register Account Name");
        System.out.println("[2] Deposit Amount");
        System.out.println("[3] Withdraw Amount");
        System.out.println("[4] Currency Exchange");
        System.out.println("[5] Record Exchange Rates");
        System.out.println("[6] Show Interest Amount");
        System.out.println();
    }


    //---------------------------
    // REGISTER ACCOUNT NAME    -
    //---------------------------

    public String getAccName(){
        System.out.println();
        System.out.println("Register Account Name");
        System.out.print("Account name: ");
        return scanner.nextLine();
    }
    public void displayRegisAcc(String accName){
        System.out.println();
        System.out.println("***");
        System.out.println("Account Name = " + accName);
    }

    //---------------------------
    // DEPOSIT AMOUNT           -
    //---------------------------
    
    public String getDepositAccName(){
        System.out.println();
        System.out.println("Deposit Amount");
        System.out.print("Account Name: ");   
        return scanner.nextLine();
    }

    public double getDepositAmt(){
        System.out.println();
        return getValidDouble("Deposit Amount: ");
    
    }

    public void displayDepositResult(String accName, double depositAmt, double currentBalance){
        System.out.println();
        System.out.println("***");
        System.out.println("Account Name: " + accName);
        System.out.println("Deposit Amount = " + String.format("%.2f", depositAmt));
        System.out.println("Updated Balance = " + String.format("%.2f", currentBalance));
    }

    //---------------------------
    // WITHDRAW AMOUNT          -
    //---------------------------

    public String getWithdrawAccName(){
        System.out.println();
        System.out.println("Withdraw Amount");
        System.out.print("Account Name: ");
        return scanner.nextLine();
    }

    public double getWithdrawAmt(){
        System.out.println();
        return getValidDouble("Withdraw Amount: ");
    }

    public void displayWithdrawResult(String accName, double withdrawAmt, double currentBalance){
        System.out.println();
        System.out.println("***");
        System.out.println("Account Name = " + accName);
        System.out.println("Withdraw Amount = " + String.format("%.2f", withdrawAmt));
        System.out.println("Updated Balance = " + String.format("%.2f", currentBalance));
    }

    //---------------------------
    // CURRENCY EXCHANGE        -
    //---------------------------

    public double getSourceAmt(){
        return getValidDouble("Source Amount: ");
    }                

    public int getCurrencyChoice(String[] currencyTypes, String[] currencyCodes){
        System.out.println();
        System.out.println("Foreign Currency Exchange");
        System.out.println("Source Currency Option:");

        for (int i = 0; i < currencyTypes.length; i++){
            System.out.println( "[" + (i + 1) + "] " + currencyTypes[i] + " (" + currencyCodes[i] + ")");
        }
        System.out.println();
        System.out.print("Source Currency: ");
        while(!scanner.hasNextInt()){
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();
            System.out.print("Source Currency: ");
        }

        int currencyNumber = scanner.nextInt();
        scanner.nextLine();
        return currencyNumber;
    }

    public int getExchangedCurrency(String[] currencyTypes, String[] currencyCodes){
        System.out.println();
        System.out.println("Exchanged Currency Options:");

        for(int i = 0; i < currencyTypes.length; i++){
            System.out.println("[" + (i + 1) + "] " + currencyTypes[i] + " (" + currencyCodes[i] + ")");
        }

        System.out.println();
        System.out.print("Exchange Currency: ");

        while(!scanner.hasNextInt()){
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();
            System.out.print("Exchange Currency: ");
        }

        int exchangedCurrency = scanner.nextInt();
        scanner.nextLine();

        return exchangedCurrency;
    }

    public void displayExchangedResult(double exchangedAmount){
        System.out.println("Exchange Amount: " + String.format("%.2f", exchangedAmount));
    }

    public String getConvertAnotherCurrency(){
        System.out.println();
        System.out.print("Convert another currency (Y/N): ");
        return scanner.nextLine();
    }

    //---------------------------
    // RECORD EXCHANGE RATE     -
    //---------------------------

    public int getSelectedForeignCurrency(String[] currencyTypes, String[] currencyCodes){
        System.out.println();
        System.out.println("Record Exchange Rate");
        System.out.println();
        for(int i = 0; i < currencyTypes.length; i++){
            System.out.println("[" + (i + 1) + "] " + currencyTypes[i] + " (" + currencyCodes[i] + ")");
        }
        System.out.println();

        while (true){
            System.out.print("Selected Foreign Currency: ");
            String input = scanner.nextLine();
            if(input.matches("\\[[0-9]+\\]")){
                
                try{
                    int selectedCurrency = Integer.parseInt(input.substring(1, input.length() - 1));
                    return selectedCurrency;
                }
                catch(NumberFormatException e){
                    System.out.println("invalid input. Number is too large.");
                }
            }
            else{
                System.out.println("Invalid input. Please enter a number in brackets.");
            }
        
        }
    }
    public double getExchangeRate(){
        return getValidDouble("Exchange Rate: ");
    }

    public void displayRecordedExchangeRate(int selectedForeignCurrency, double exchangeRate){
        System.out.println();
        System.out.println("***");
        System.out.println("Select Foreign Currency = [" + selectedForeignCurrency + "]");
        System.out.println("Exchange Rate = " + String.format("%.2f", exchangeRate));
    }


    //---------------------------
    // SHOW INTEREST AMOUNT     -
    //---------------------------

    public void displayInterestAmt(double currentBalance, String currency, double annualInterestRate){
        System.out.println("Current Balance: " + String.format("%.2f", currentBalance));
        System.out.println("Currency: " + currency);
        System.out.println("Interest Rate: " + String.format("%.0f", annualInterestRate * 100) + "%");
    }

    public int getInterestDays(){
        System.out.println();
        System.out.print("Total Number of Days: ");
        while(!scanner.hasNextInt()){
            System.out.println("Invalid input. Please enter a whole number.");
            scanner.nextLine();
            System.out.println("Total Number of Days: ");
        }
        int days = scanner.nextInt();
        scanner.nextLine();
        return days;
    }

    public void showInterestTable(int days, double [] interests, double[] balances){
        System.out.println();
        System.out.println("Day | Interest | Balance |");

        for (int i = 0; i < days; i++) {

            System.out.println((i + 1) + "   | " + String.format("%.2f", interests[i]) + "     | " + String.format("%.2f", balances[i]) + " |");
        }
    }

    public String getInterestAccName(){
        System.out.println();
        System.out.println("Show Interest Amount");
        System.out.print("Account Name: ");
        return scanner.nextLine();
    }

    //---------------------------
    // CHOICE & ERROR HANDLING  -
    //---------------------------

    public int getChoice(){
        System.out.print("Choice: ");
        
        while(!scanner.hasNextInt()){
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();
            System.out.print("Choice: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public void displaySelectedChoice(int choice) {
        System.out.println();
        System.out.println("***");
        System.out.println("Choice = " + choice);
    }

    private double getValidDouble(String prompt){
        System.out.print(prompt);
        while(!scanner.hasNextDouble()){
            System.out.println("Invalid input. Please enter a number");
            scanner.nextLine();
            System.out.print(prompt);
        }
        double amount = scanner.nextDouble();
        scanner.nextLine();
        return amount;
    }
    public void displayChoice(int choice){
        System.out.println();
        System.out.print("***");
        System.out.println("Choice = " + choice);
    }

    public void displayInvalidChoice(){
        System.out.println();
        System.out.println("Invalid Choice.");
    }

    public void displayInvalidAmount(){
        System.out.println("Invalid amount. Please enter a valid positive number.");
    }

    public void displayInvalidCurrencyChoice(){
        System.out.println();
        System.out.println("Invalid Currency Choice.");
    }

    public void displayInvalidWithdraw(){
        System.out.println("No available balance. Withdrawal cannot proceed.");
    }

    public void displayInsufficientBalance(){
        System.out.println("Invalid withdrawal amount. Amount exceeds your current balance.");
    }

    public void displayInvalidDays(){
        System.out.println("Invalid number of days. Please enter a positive whole number");
    }

    public String getReturnToMenu(){
        System.out.println();
        System.out.print("Back to the Main Menu (Y/N): ");
        return scanner.nextLine();
    }

    public void displayInvalidReturnMenuChoice(){
        System.out.println("Invalid input. Please enter 'Y' or 'N' only.");
    }

    public void displayInvalidAccName(){
        System.out.println("Invalid account name. Please follow the format 'lastname, firstname' with letters only.");
    }

    public void displayDuplicateAccount(){
        System.out.println("Account already registered. Please enter a different name.");
    }

    public void displayAccountNotFound(){
        System.out.println("Account not found. Please input a registered account.");
    }

    //---------------------------
    // ACCOUNT BALANCE DISPLAY  -
    //---------------------------
    
    public void displayAccountBalance(double currentBalance, String currency){
        System.out.println("Current Balance: " + String.format("%.2f", currentBalance));
        System.out.println("Currency: " + currency);
    }

    
    //---------------------------
    // TERMINATE MESSAGE        -
    //---------------------------
    
    public void displayExiting(){
        System.out.println();
        System.out.println("Exiting...");
    }
}
