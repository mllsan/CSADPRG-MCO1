//********************
// Last names: Sauz
// Language: Java
// Paradigm(s): Object Oriented Programming following MVC structure
//********************

/**
 * Represents the Controller component of the MVC banking application
 * Manages program navigation, processes user transactions, and
 * coordinates interactions between BankModel and BankView
 */
import java.util.Scanner;

public class BankController{

    private BankModel model;
    private BankView view;
    private boolean isRunning = true;

    public BankController(BankModel model, BankView view){
        this.model = model;
        this.view = view;
    }

    /**
     * Executes the main program loop for the bank application
     * Displays main menu, validates user selection and calls appropriate
     * transaction methods until user decides to exit the program.
     */
    public void run(){
    
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

                if(isRunning){
                    String input = view.getReturnToMenu();

                    while(!model.isValidReturnMenuChoice(input)){
                        view.displayInvalidReturnMenuChoice();
                        input = view.getReturnToMenu();
                    }

                    if(input.equals("N")){
                        isRunning = false;
                    }
                }
            
            }

            view.displayExiting();
        }
    
    
    /**
     * Directs the program to appropriate banking transaction based on user choice.
     * @param choice : selected transaction number from the main menu
     */
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
                recordExchangeRate();
                break;
            case 6:
                showInterestAmount();
                break;
        }
    }

    //---------------------------
    // REGISTER ACCOUNT NAME    -
    //---------------------------

    /**
     * Handles the registration of a new bank account
     * Validates the account name and checks for duplicate accounts
     * before saving the new account through Model.
     */
    private void registerAccount(){
        String accountName = view.getAccName();

        while(!model.isValidAccountName(accountName) || model.isAccRegistered(accountName)){
            
            if(!model.isValidAccountName(accountName)){
                view.displayInvalidAccName();
            } else {
                view.displayDuplicateAccount();
            }

            accountName = view.getAccName();
        }
        model.registerAccount(accountName);
        model.setAccName(accountName.trim());
        view.displayRegisAcc(model.getAccName());
    }

    
    //---------------------------
    // DEPOSIT AMOUNT           -
    //---------------------------

    /**
     * Processes a deposit transaction for a registered account. 
     * Verifies that account exists, validates deposit amount, updates account balance,
     * and display transaction result.
     */
    private void depositAmount(){
        String accountName = view.getDepositAccName();
       
        if(!model.isAccRegistered(accountName)){
            view.displayAccountNotFound();
        } else {
            model.setAccName(accountName.trim());
            view.displayAccountBalance(model.getCurrentBalance(), model.getDefaultCurrency());
            double depositAmt = view.getDepositAmt();

            while(!model.isValidAmount(depositAmt)){
            view.displayInvalidAmount();
            depositAmt = view.getDepositAmt();

            }
            model.setDepositAmt(depositAmt);
            view.displayDepositResult(model.getAccName(), model.getDepositAmt(), model.getCurrentBalance());

        }
       
    }
    
    //---------------------------
    // WITHDRAW AMOUNT          -
    //---------------------------

    /**
     * Processes a withdrawal transaction for a registered account.
     * Verifies account registration, checks avaiable balance, and validates 
     * withdrawal amount before updating balance.
     */
    private void withdrawAmount(){
        String accountName = view.getWithdrawAccName();
        
        if(!model.isAccRegistered(accountName)){
            view.displayAccountNotFound();
        } else {
            model.setAccName(accountName.trim());
            view.displayAccountBalance(model.getCurrentBalance(), model.getDefaultCurrency());
            
            if(model.isValidAmount(model.getCurrentBalance())){
                view.displayInvalidWithdraw();
            } else {
                double withdrawAmount = view.getWithdrawAmt();

                while(!model.isValidWithdraw(withdrawAmount)){
            
                    if(!model.isValidAmount(withdrawAmount)){
                        view.displayInvalidAmount();
                    } else {
                        view.displayInsufficientBalance();
                    }
            
                    withdrawAmount = view.getWithdrawAmt();
                }

                model.setWithdrawAmt(withdrawAmount);
                view.displayWithdrawResult(model.getAccName(), model.getWithdrawAmt(), model.getCurrentBalance());

            }
            
        }
        
    }

    
    //---------------------------
    // CURRENCY EXCHANGE        -
    //---------------------------

    /**
     * Handles the conversion of an amount between two currencies.
     * Validates the selected currencies and source amount, performs the 
     * conversion through Model, and allows repeated conversations based on Y/N.
     **/
    private void currencyExchange(){
        
        int sourceCurrency = view.getCurrencyChoice(model.getCurrencyTypes(), model.getCurrencyCodes());
        
        while(!model.isValidCurrencyChoice(sourceCurrency)){
            view.displayInvalidCurrencyChoice();
            sourceCurrency = view.getCurrencyChoice(model.getCurrencyTypes(), model.getCurrencyCodes());
        }
        
        double sourceAmount = view.getSourceAmt();

        while(!model.isValidAmount(sourceAmount)){
            view.displayInvalidAmount();
            sourceAmount = view.getSourceAmt();
        }

        boolean exchanging = true;

        while(exchanging){
            int exchangeCurrency = view.getExchangedCurrency(model.getCurrencyTypes(), model.getCurrencyCodes());

            while(!model.isValidCurrencyChoice(exchangeCurrency)){
                view.displayInvalidCurrencyChoice();
                exchangeCurrency = view.getExchangedCurrency(model.getCurrencyTypes(), model.getCurrencyCodes());
            }

            double exchangedAmt = model.calculateCurrExchange(sourceCurrency, exchangeCurrency, sourceAmount);
            view.displayExchangedResult(exchangedAmt);

            String input = view.getConvertAnotherCurrency();

            while(!model.isValidReturnMenuChoice(input)){
                view.displayInvalidReturnMenuChoice();
                input = view.getConvertAnotherCurrency();
            }

            if(input.equals("N")){
                exchanging = false;
            }
        } 
    }
    
    //---------------------------
    // RECORD EXCHANGE RATE     -
    //---------------------------

    /**
     * Handles the recording of a new currency exchange rate.
     * Validates selected foreign currency and entered exchange rate
     * before updating the corresponding rate in Model.
     */
    private void recordExchangeRate(){
        int selectedCurrency = view.getSelectedForeignCurrency(model.getCurrencyTypes(), model.getCurrencyCodes());
        model.setSelectedForeignCurrency(selectedCurrency);

        while(!model.isValidCurrencyChoice()){
            view.displayInvalidCurrencyChoice();

            selectedCurrency = view.getSelectedForeignCurrency(model.getCurrencyTypes(), model.getCurrencyCodes());
            model.setSelectedForeignCurrency(selectedCurrency);
        }

        double exchangeRate = view.getExchangeRate();

        while(!model.isValidAmount(exchangeRate)){
            view.displayInvalidAmount();
            exchangeRate = view.getExchangeRate();
        }

        model.setExchangeRate(selectedCurrency, exchangeRate);
        view.displayRecordedExchangeRate(model.getSelectedForeignCurrency(), exchangeRate);
    }   
    
    //---------------------------
    // SHOW INTEREST AMOUNT     -
    //---------------------------

    /**
     * Handles interest calculations for a registered account
     * Validates the selected account and number of days, calculates
     * the daily interest and projected balances through the Model, then displays
     * the results in a table format.
     */
    private void showInterestAmount(){
        
        String accountName = view.getInterestAccName();
        
        if(!model.isAccRegistered(accountName)){
            view.displayAccountNotFound();
        } else {

            model.setAccName(accountName);

            view.displayInterestAmt(model.getCurrentBalance(), model.getDefaultCurrency(), model.getAnnualInterestRate());
            int days = view.getInterestDays();

            while (!model.isValidInterestDays(days)) {
                view.displayInvalidDays();
                days = view.getInterestDays();
            }

            double[] interests = model.calculateInterestAmt(days);
            double[] balances = model.calculateInterestBal(days);

            view.showInterestTable(days, interests, balances);
        }

    }
}
