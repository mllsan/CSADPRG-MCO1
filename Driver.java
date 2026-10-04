//********************
// Last names: Sauz
// Language: Java
// Paradigm(s): Object Oriented Programming following MVC structure
//********************

public class Driver{
    public static void main(String[] args){
        BankModel model = new BankModel();
        BankView view = new BankView();
        BankController controller = new BankController(model,view);
        controller.run();
    }
}