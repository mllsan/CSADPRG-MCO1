/**
 * This is the Kotlin version of MCO1's Basic I/O milestone for Group 5.
 * This doesn't properly support multiple accounts.
 *
 * @author Hannah Cheng
 * @version 1.0
 */


fun RegisterAccountName(): String{
    println("Register Account Name")
    print("Account Name: ")
    var accountName: String = readln()

    println(" ")
    println("***")
    println("Account Name = $accountName")
    println(" ")
    return accountName
}

fun DepositAmount(accountName: String, balance: Double): Double {
    println("Deposit Amount")
    print("Account Name: ")
    var accountName: String = readln()

    println("Current Balance: ${String.format("%.2f", balance)}")
    println("Currency: PHP")

    print("Deposit Amount: ")
    var depositAmount: Double = readln().toDouble()

    var newBalance = balance + depositAmount

    println(" ")
    println("***")
    println("Account Name = $accountName")
    println("Deposit Amount = ${String.format("%.2f", depositAmount)}")
    println("New Balance = ${String.format("%.2f", newBalance)}")
    println(" ")

    return newBalance
}
fun WithdrawAmount(accountName: String, balance: Double): Double {
    println("Withdraw Amount")
    print("Account Name: ")
    var accountName: String = readln()

    println("Current Balance: ${String.format("%.2f", balance)}")
    println("Currency: PHP")

    print("Withdraw Amount: ")
    var withdrawAmount: Double = readln().toDouble()

    var newBalance = balance - withdrawAmount

    println(" ")
    println("***")
    println("Account Name = $accountName")
    println("Withdraw Amount = ${String.format("%.2f", withdrawAmount)}")
    println("New Balance = ${String.format("%.2f", newBalance)}")
    println(" ")

    return newBalance
}

fun CurrencyExchange(usdRate: Double, jpyRate: Double, gbpRate: Double, eurRate: Double, cnyRate: Double) {
    println("Foreign Currency Exchange")
    print("Source Amount (PHP): ")
    var sourceAmount: Double = readln().toDouble() 

    println(" ")
    println("Exchanged Currency")
    println("[1] Philippine Peso (PHP) = ${String.format("%.2f", sourceAmount)}")
    println("[2] United States Dollar (USD) = ${String.format("%.2f", sourceAmount*usdRate)}")
    println("[3] Japanese Yen (JPY) = ${String.format("%.2f", sourceAmount*jpyRate)}")
    println("[4] British Pound Sterling (GBP) = ${String.format("%.2f", sourceAmount*gbpRate)}")
    println("[5] Euro (EUR) = ${String.format("%.2f", sourceAmount*eurRate)}")
    println("[6] Chinese Yuan Renminbi (CNY) = ${String.format("%.2f", sourceAmount*cnyRate)}")

    println(" ")
    println("***")
    println("Source Currency = Philippine Peso (PHP)")
    println("Source Amount (PHP) = ${String.format("%.2f", sourceAmount)}")
    println(" ")
}

fun RecordExchangeRates(
    usdRate: Double, jpyRate: Double, gbpRate: Double, eurRate: Double, cnyRate: Double
): DoubleArray {
    println("Record Exchange Rate")
    println("[1] Philippine Peso (PHP)")
    println("[2] United States Dollar (USD)")
    println("[3] Japanese Yen (JPY)")
    println("[4] British Pound Sterling (GBP)")
    println("[5] Euro (EUR)")
    println("[6] Chinese Yuan Renminbi (CNY)")

    print("Select Foreign Currency: ")
    var selectedCurrency: Int = readln().toInt()

    print("Exchange Rate: ")
    var exchangeRate: Double = readln().toDouble()

    var updatedUsd = usdRate
    var updatedJpy = jpyRate
    var updatedGbp = gbpRate
    var updatedEur = eurRate
    var updatedCny = cnyRate

    when (selectedCurrency) {
        2 -> updatedUsd = exchangeRate
        3 -> updatedJpy = exchangeRate
        4 -> updatedGbp = exchangeRate
        5 -> updatedEur = exchangeRate
        6 -> updatedCny = exchangeRate
    }

    println(" ")
    println("***")
    println("Select Foreign Currency = [$selectedCurrency]")
    println("Exchange Rate = ${String.format("%.2f", exchangeRate)}")
    println(" ")

    return doubleArrayOf(updatedUsd, updatedJpy, updatedGbp, updatedEur, updatedCny)
}

fun ShowInterestAmount(){
    //wala pa
}

fun main() {

    //rates
    var usdRate: Double = 62.00
    var jpyRate: Double = 0.40
    var gbpRate: Double = 84.00
    var eurRate: Double = 72.00
    var cnyRate: Double = 9.00

    var accountName: String = ""
    var balance: Double = 1000.00

    var choice: Int
    var backToMain: Char = 'Y'

    while (backToMain == 'Y') {

        println("Select Transaction:")
        println("[1] Register Account Name")
        println("[2] Deposit Amount")
        println("[3] Withdraw Amount")
        println("[4] Currency Exchange")
        println("[5] Record Exchange Rates")
        println("[6] Show Interest Amount")

        choice = readln().toInt()
        println("Choice: $choice")
        println(" ")
        println("***")
        println("Choice = $choice")

        when (choice) {
            1 -> accountName = RegisterAccountName()
            2 -> balance = DepositAmount(accountName, balance)
            3 -> balance = WithdrawAmount(accountName, balance)
            4 -> CurrencyExchange(usdRate, jpyRate, gbpRate, eurRate, cnyRate)
            5 -> {
                var newRates = RecordExchangeRates(usdRate, jpyRate, gbpRate, eurRate, cnyRate)
                usdRate = newRates[0]
                jpyRate = newRates[1]
                gbpRate = newRates[2]
                eurRate = newRates[3]
                cnyRate = newRates[4]
            }
            6 -> ShowInterestAmount()

            else -> println("Invalid choice")
        }

        println("Back to the Main Menu (Y/N):") //doesnt check invalid inputs yet
        backToMain = readln().single()

    }

}