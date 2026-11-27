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
import java.io.IOException;
import java.util.ArrayList;

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

        try{
            model.loadTransactionData();
            // view.displayCSVSummary(model.getTotalRows(), model.getValidRows(), model.getInvalidRows(), model.getExcludedRows());
        
    
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
            } catch(IOException e){
            view.displayCSVError();
        }
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
            case 7:
                generateReports();
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
        
        boolean registering = true;

        while(registering){
        
            String accountName = view.getAccName();

            while(!model.isValidAccountName(accountName) || model.isAccRegistered(accountName)){
                
                if(!model.isValidAccountName(accountName)){
                    view.displayInvalidAccName();
                } else {
                    view.displayDuplicateAccount();
                }

                accountName = view.getAccName();
            }
            double initialDeposit = view.getInitialDeposit();

            while(!model.isValidInitialDeposit(initialDeposit)){
                view.displayInvalidInitialDeposit();
                initialDeposit = view.getInitialDeposit();
            }
            try{
                boolean registered = model.registerAccount(accountName, initialDeposit);
                if(registered){
                    model.setAccName(accountName.trim());

                    Account account = model.findAccount(accountName);
                    view.displayRegisAcc(account.getAccName(), account.getAccNumber(), account.getCurrentBalance());
                } else {
                    view.displayInvalidAccName();
                }
            } catch(IOException e){
                view.displayCSVSaveError();
            }
            
            String choice = view.getAnotherRegister();
            while(!model.isValidReturnMenuChoice(choice)){
                view.displayInvalidReturnMenuChoice();
                choice = view.getAnotherRegister();
            }
            if(choice.equals("N")){
                registering = false;
            }
        }
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

        boolean depositing = true;
        
        while(depositing){
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
                try {
                    model.setDepositAmt(depositAmt);
                    view.displayDepositResult(model.getAccName(), model.getDepositAmt(), model.getCurrentBalance());
                } catch(IOException e){
                    view.displayCSVSaveError();
                }
                String choice = view.getAnotherDeposit();

                while(!model.isValidReturnMenuChoice(choice)){
                    view.displayInvalidReturnMenuChoice();
                    choice = view.getAnotherDeposit();
                }
                if(choice.equals("N")){
                    depositing = false;
                }
            }
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
        
        boolean withdrawing = true;

        while(withdrawing){
        
            String accountName = view.getWithdrawAccName();
            
            if(!model.isAccRegistered(accountName)){
                view.displayAccountNotFound();
            } else {
                model.setAccName(accountName.trim());
                view.displayAccountBalance(model.getCurrentBalance(), model.getDefaultCurrency());
                
                if(!model.isValidAmount(model.getCurrentBalance())){
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

                    try {
                        model.setWithdrawAmt(withdrawAmount); 
                        view.displayWithdrawResult(model.getAccName(), model.getWithdrawAmt(), model.getCurrentBalance());
                    } catch(IOException e){
                        view.displayCSVSaveError();
                    } 
                    String choice = view.getAnotherWithdraw();

                    while(!model.isValidReturnMenuChoice(choice)){
                        view.displayInvalidReturnMenuChoice();
                        choice = view.getAnotherWithdraw();
                    }
                    if(choice.equals("N")){
                        withdrawing = false;
                    }
                }
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

    //---------------------------
    // GENERATE REPORTS         -
    //---------------------------

    private void generateReports(){
        boolean generating = true;
        boolean fileLoaded = false;

        while(generating){
            view.displayReportsMenu();
            int choice = view.getChoice();

            while(choice < 1 || choice > 2){
                view.displayInvalidChoice();
                choice = view.getChoice();
            }
            try{
                if(choice == 1){
                    model.loadTransactionData();
                    fileLoaded = true;
                    view.displayCSVSummary(model.getTotalRows(), model.getValidRows(), model.getInvalidRows(), model.getExcludedRows());
                } else {
                    if(!fileLoaded){
                        model.loadTransactionData();
                        fileLoaded = true;
                    }
                    view.displayGeneratingReport();

                    ArrayList<String[]> report1 = model.generateReport1();
                    ArrayList<String[]> report2 = model.generateReport2();
                    ArrayList<String[]> report3 = model.generateReport3();
                    ArrayList<String[]> report4 = model.generateReport4();

                    String summary = model.generateSummary(report2);

                    view.displayReportsSaved();

                    view.displayReport1(report1);
                    view.displayReport2(report2);
                    view.displayReport3(report3);
                    view.displayReport4(report4);
                    view.displaySummary(summary);

                    generating = false;
                }
            } catch(IOException e){
                view.displayCSVError();
                generating = false;
            }
        }
    }
}
