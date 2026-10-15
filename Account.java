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
    private double currentBalance;

    // CONSTRUCTOR
    public Account(String accName, double currentBalance){
        this.accName = accName;
        this.currentBalance = currentBalance;
    }

    
    //----------------
    // GETTERS       -
    //----------------
   
    public String getAccName(){
        return accName;
    }

    public double getCurrentBalance(){
        return currentBalance;
    }

        
    //----------------
    // SETTER        -
    //----------------

    public void setCurrentBalance(double currentBalance){
        this.currentBalance = currentBalance;
    }
}