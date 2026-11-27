//********************
// Last names: Sauz
// Language: Java
// Paradigm(s): Object Oriented Programming following MVC structure
//********************

/**
 * Represents an individual bank account within the Model Layer.
 * Stores the account holder's name and current balance.
 * Each Account object maintains its own balance, allowing miltiple accounts.
 */
public class Account{

    // ATTRIBUTES
    private String accName;
    private String accNumber;
    private double currentBalance;
    private LocalDate latest;
    private LocalTime time;

    // CONSTRUCTOR
    public Account(String accName, String accNumber, double currentBalance){
        this.accName = accName;
        this.accNumber = accNumber;
        this.currentBalance = currentBalance;
    }

    
    //----------------
    // GETTERS       -
    //----------------
   
    public String getAccName(){
        return accName;
    }

    public String getAccNumber(){
        return accNumber;
    }
    
    public double getCurrentBalance(){
        return currentBalance;
    }

     public LocalDate getLatestDate(){
        return latest;
    }

    public LocalTime getLatestTime(){
        return time;
    }


        
    //----------------
    // SETTER        -
    //----------------

    public void setCurrentBalance(double currentBalance){
        this.currentBalance = currentBalance;
    }

    public void setlatest(LocalDate latest){
        this.latest = latest;

    }
    public void setTime(LocalTime time){
        this.time = time;

    }
}
