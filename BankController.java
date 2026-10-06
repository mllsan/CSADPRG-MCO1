//********************
// Last names: Sauz
// Language: Java
// Paradigm(s): Object Oriented Programming following MVC structure
//********************

import java.util.Scanner;

public class BankController{

    private BankModel model;
    private BankView view;

    public BankController(BankModel model, BankView view){
        this.model = model;
        this.view = view;
    }

    public void run(){
        boolean isRunning = true;
    
        while(isRunning){
            view.displayMainMenu();
            int choice = view.getChoice();
            model.setChoice(choice);
            
            while(!model.isValidChoice()){
                view.displayInvalidChoice();
                choice = view.getChoice();
                model.setChoice(choice);
                }
                view.displaySelectedChoice(choice);
                navigationMenu(choice);
            }
        }
    
    
    private void navigationMenu(int choice){
        switch(choice){
            case 1:
                registerAccount();
                break;
            case 2:
                depositAmount();
                break;
            case 3:
                withdrawAmount();
                break;
            case 4:
                currencyExchange();
                break;
            case 5:
                recordExhangeRate();
                break;
            case 6:
                showInterestAmount();
                break;
        }
    }

    //---------------------------
    // REGISTER ACCOUNT NAME    -
    //---------------------------

    private void registerAccount(){
        String accountName = view.getAccName();
        model.setAccName(accountName);
        view.displayRegisAcc(model.getAccName());
    }

    
    //---------------------------
    // DEPOSIT AMOUNT           -
    //---------------------------

    private void depositAmount(){
        String accountName = view.getDepositAccName(model.getCurrentBalance(), model.getDefaultCurrency());
        model.setAccName(accountName);
        double depositAmt = view.getDepositAmt();
        
        while(!model.isValidAmount(depositAmt)){
            view.displayInvalidAmount();
            depositAmt = view.getDepositAmt();
        }

        model.setDepositAmt(depositAmt);
        view.displayDepositResult(model.getAccName(), model.getDepositAmt());
    }
    
    //---------------------------
    // WITHDRAW AMOUNT          -
    //---------------------------

    private void withdrawAmount(){
        String accountName = view.getWithdrawAccName(model.getCurrentBalance(), model.getDefaultCurrency());
        model.setAccName(accountName);
        double withdrawAmount = view.getWithdrawAmt();
        
        while(!model.isValidWithdraw(withdrawAmount)){
            
            if(withdrawAmount <= 0){
                view.displayInvalidAmount();
            } else {
                view.displayInsufficientBalance();
            }
            
            withdrawAmount = view.getWithdrawAmt();
        }
        
        model.setWithdrawAmt(withdrawAmount);
        view.displayWithdrawResult(model.getAccName(), model.getWithdrawAmt());
    }

    
    //---------------------------
    // CURRENCY EXCHANGE        -
    //---------------------------

    private void currencyExchange(){
        double sourceAmount = view.getSourceAmt();
        
        while(!model.isValidAmount(sourceAmount)){
            view.displayInvalidAmount();
            sourceAmount = view.getSourceAmt();
        }
        
        model.setSourceAmt(sourceAmount);
        double[] exchangedAmt = new double[6];
        for(int i = 0; i < exchangedAmt.length; i++){
            exchangedAmt[i] = model.calculateCurrExchange(i+1);
        }
        view.displayCurrencyExchange(model.getSourceAmt(), model.getCurrencyTypes(), model.getCurrencyCodes(), exchangedAmt);
    }
    
    //---------------------------
    // REGISTER EXCHANGE RATE   -
    //---------------------------

    private void recordExhangeRate(){
        int selectedCurrency = view.getSelectedForeignCurrency(model.getCurrencyTypes(), model.getCurrencyCodes());
        model.setSelectedForeignCurrency(selectedCurrency);

        if(model.isValidCurrencyChoice()){
            double exchangeRate = view.getExchangeRate();
            
            while(!model.isValidAmount(exchangeRate)){
                view.displayInvalidAmount();
                exchangeRate = view.getExchangeRate();
            }
            
            model.setExchangeRate(selectedCurrency, exchangeRate);
            view.displayRecordedExchangeRate(model.getSelectedForeignCurrency(), exchangeRate);

        } else {
            view.displayInvalidCurrencyChoice();
        }
    }   
    
    //---------------------------
    // SHOW INTEREST AMOUNT     -
    //---------------------------

    private void showInterestAmount(){
        System.out.println("TO BE IMPLEMENTED NEXT PHASE");
    }
}
