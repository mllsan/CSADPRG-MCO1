/**
 * This is the Main file for the Kotlin version of MCO1 for Group 5.
 *
 * @author Hannah Cheng
 * @version 2.1
 */

package mco1

fun main() {

    //rates
    var usdRate: Double = 62.00
    var jpyRate: Double = 0.40
    var gbpRate: Double = 84.00
    var eurRate: Double = 72.00
    var cnyRate: Double = 9.00

    val accounts = ArrayList<Account>()

    var choice: Int
    var backToMain: Char = 'Y'

    while (backToMain == 'Y') {

        println(" ")
        println("Select Transaction:")
        println("[1] Register Account Name")
        println("[2] Deposit Amount")
        println("[3] Withdraw Amount")
        println("[4] Currency Exchange")
        println("[5] Record Exchange Rates")
        println("[6] Show Interest Amount")
        println(" ")
        //print("Choice: ")
        choice = promptMenuChoice()

        spacing()
        println("Choice = $choice")
        println(" ")

        when (choice) {
            1 -> RegisterAccountName(accounts)
            2 -> DepositAmount(accounts)
            3 -> WithdrawAmount(accounts)
            4 -> CurrencyExchange(usdRate, jpyRate, gbpRate, eurRate, cnyRate)
            5 -> {
                var newRates = RecordExchangeRates(usdRate, jpyRate, gbpRate, eurRate, cnyRate)
                usdRate = newRates[0]
                jpyRate = newRates[1]
                gbpRate = newRates[2]
                eurRate = newRates[3]
                cnyRate = newRates[4]
            }
            6 -> ShowInterestAmount(accounts)

            else -> println("Invalid choice")
        }

        //println("Back to the Main Menu (Y/N):")

        if (promptYesNo("Back to the Main Menu (Y/N)"))
            backToMain = 'Y'
        else
            backToMain = 'N'
        println(" ")

    }

}