//********************
// Last names: Sauz
// Language: Java
// Paradigm(s): Object Oriented Programming following MVC structure
//********************

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
    
    public String getDepositAccName(double defaultBalance, String defaultCurrency){
        System.out.println();
        System.out.println("Deposit Amount");
        System.out.print("Account Name: ");   
        String accName = scanner.nextLine();
        System.out.println("Current Balance: " + defaultBalance);
        System.out.println("Currency: " + defaultCurrency);
        return accName;
    }

    public double getDepositAmt(){
        System.out.println();
        return getValidDouble("Deposit Amount: ");
    
    }

    public void displayDepositResult(String accName, double depositAmt){
        System.out.println();
        System.out.println("***");
        System.out.println("Account Name: " + accName);
        System.out.println("Deposit Amount = " + String.format("%.2f", depositAmt));
    }

    //---------------------------
    // WITHDRAW AMOUNT          -
    //---------------------------

    public String getWithdrawAccName(double defaultBalance, String defaultCurrency){
        System.out.println();
        System.out.println("Withdraw Amount");
        System.out.print("Account Name: ");
        String accName = scanner.nextLine();
        System.out.println("Current Balance: " + String.format("%.2f", defaultBalance));
        System.out.println("Currency: " + defaultCurrency);
        return accName;
    }

    public double getWithdrawAmt(){
        System.out.println();
        return getValidDouble("Withdraw Amount: ");
    }

    public void displayWithdrawResult(String accName, double withdrawAmt){
        System.out.println();
        System.out.println("***");
        System.out.println("Account Name = " + accName);
        System.out.println("Withdraw Amount = " + String.format("%.2f", withdrawAmt));
    }

    //---------------------------
    // CURRENCY EXCHANGE        -
    //---------------------------

    public double getSourceAmt(){
        System.out.println();
        System.out.println("Foreign Currency Exchange");
        return getValidDouble("Source Amount (PHP): ");
    }

    public void displayCurrencyExchange(double sourceAmt, String[] currencyTypes, String[] currencyCodes, double[] exchangedAmt){
        System.out.println();
        System.out.println("Exchanged Currency");

        for(int i = 0; i< currencyTypes.length; i++){
            System.out.println("[" + (i + 1) + "] " + currencyTypes[i] + " (" + currencyCodes[i] + ") = " + String.format("%.2f", exchangedAmt[i]));
        }
        System.out.println();
        System.out.println("***");
        System.out.println("Source Currency = Philippine Peso (PHP)");
        System.out.println("Source Amount (PHP) = " + String.format("%.2f", sourceAmt));
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
                int selectedCurrency = Integer.parseInt(input.substring(1, input.length()-1));
                return selectedCurrency;
            }
            System.out.println("Invalid input. Please enter a number in brackets.");
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
        System.out.println("Invalid amount. Amount must be greater than 0.");
    }

    public void displayInvalidCurrencyChoice(){
        System.out.println();
        System.out.println("Invalid Currency Choice.");
    }

    public void displayInvalidWithdraw(){
        System.out.println("Invalid withdrawal amount. Please recheck your balance.");
    }

    public void displayInsufficientBalance(){
        System.out.println("Invalid withdrawal amount. Amount exceeds your current balance.");
    }

}
