/**
 * This is the file containing the primary 6 app functions for the Kotlin version of MCO1 for Group 5.
 *
 * @author Hannah Cheng
 * @version 2.1
 */

package mco1


val CURRENCY_NAMES = arrayOf(
    "Philippine Peso (PHP)",
    "United States Dollar (USD)",
    "Japanese Yen (JPY)",
    "British Pound Sterling (GBP)",
    "Euro (EUR)",
    "Chinese Yuan Renminni (CNY)"
)

/**
 * Registers a new account under a valid name that isn't already taken.
 * @param accounts the list of all registered accounts
 */
fun RegisterAccountName(accounts: ArrayList<Account>){
    println("Register Account Name")
    var accountName: String = promptValidName()

    if (findAccount(accounts, accountName) != null) {
        spacing()
        println("Account already registered. Please enter a different name.")
        println(" ")
        return
    }

    accounts.add(Account(accountName))
    spacing()
    println("Account Name = $accountName")
    println(" ")
}

/**
 * Adds a user-entered amount to a registered account's balance.
 * @param accounts the list of all registered accounts
 */
fun DepositAmount(accounts: ArrayList<Account>) {
    println("Deposit Amount")
    val accountName = promptValidName()
    val account = findAccount(accounts, accountName)

    if (account == null) {
        println("Account not found. Please input a registered account.")
        println(" ")
        return
    }

    println("Current Balance: ${String.format("%.2f", account.balance)}")
    println("Currency: PHP")

    val depositAmount = promptPositiveAmount("Deposit Amount")
    account.balance += depositAmount

    spacing()
    println("Account Name = ${account.name}")
    println("")
    println("Deposit Amount = ${String.format("%.2f", depositAmount)}")
    println("New Balance = ${String.format("%.2f", account.balance)}")
    println(" ")
}

/**
 * Subtracts a user-entered amount from a registered account's balance if funds are enough.
 * @param accounts the list of all registered accounts
 */
fun WithdrawAmount(accounts: ArrayList<Account>){
    println("Withdraw Amount")
    val accountName = promptValidName()
    val account = findAccount(accounts, accountName)

    if (account == null) {
        println("Account not found. Please input a registered account.")
        println(" ")
        return
    }

    println("Current Balance: ${String.format("%.2f", account.balance)}")
    println("Currency: PHP")

    val withdrawAmount = promptPositiveAmount("Withdraw Amount")

    if (withdrawAmount > account.balance) {
        spacing()
        println("Invalid withdrawal amount. Amount exceeds your current balance.")
        println(" ")
        return
    }

    account.balance -= withdrawAmount

    spacing()
    println("Account Name = ${account.name}")
    println("Withdraw Amount = ${String.format("%.2f", withdrawAmount)}")
    println("New Balance = ${String.format("%.2f", account.balance)}")
    println(" ")
}

/**
 * Converts an amount from one currency to another (via PHP) and repeats for other targets on request.
 * Each rate is the PHP value of one unit of that currency.
 * @param usdRate PHP value of 1 US Dollar
 * @param jpyRate PHP value of 1 Japanese Yen
 * @param gbpRate PHP value of 1 British Pound Sterling
 * @param eurRate PHP value of 1 Euro
 * @param cnyRate PHP value of 1 Chinese Yuan Renminbi
 */
fun CurrencyExchange(usdRate: Double, jpyRate: Double, gbpRate: Double, eurRate: Double, cnyRate: Double) {

    val phpPerUnit = doubleArrayOf(1.0, usdRate, jpyRate, gbpRate, eurRate, cnyRate)

    println("Foreign Currency Exchange")
    val source = promptCurrencyChoice("Source Currency Option:", "Source Currency")
    val sourceAmount = promptPositiveAmount("Source Amount")

    var convertAgain: Boolean
    do {
        println(" ")
        val target = promptCurrencyChoice("Exchanged Currency Options:", "Exchange Currency")

        val exchangedAmount = sourceAmount * phpPerUnit[source - 1] / phpPerUnit[target - 1]

        println("Exchange Amount: ${String.format("%.2f", exchangedAmount)}")
        println(" ")

        convertAgain = promptYesNo("Convert another currency (Y/N)")
    } while (convertAgain)
    println(" ")
}


/**
 * Updates one foreign currency's exchange rate from user input.
 * @param usdRate current PHP value of 1 US Dollar
 * @param jpyRate current PHP value of 1 Japanese Yen
 * @param gbpRate current PHP value of 1 British Pound Sterling
 * @param eurRate current PHP value of 1 Euro
 * @param cnyRate current PHP value of 1 Chinese Yuan Renminbi
 * @return the rates after the update, in order: USD, JPY, GBP, EUR, CNY
 */
fun RecordExchangeRates(
    usdRate: Double, jpyRate: Double, gbpRate: Double, eurRate: Double, cnyRate: Double
): DoubleArray {

    for (i in CURRENCY_NAMES.indices) {
        println("[${i + 1}] ${CURRENCY_NAMES[i]}")
    }

    var selectedCurrency: Int
    while (true) {
        print("Select Foreign Currency: ")
        val input = readln().trim()
        val inner = input.removePrefix("[").removeSuffix("]")

        // check if start with [ or ]
        if (!input.startsWith("[") || !input.endsWith("]") || inner.isEmpty() || !inner.all { it.isDigit() }) {
            println("Invalid input. Please enter a number in brackets.")
            continue
        }

        val choice = inner.toIntOrNull()
        if (choice != null && choice in 1..6) {
            selectedCurrency = choice
            break
        }
        println("Invalid Currency Choice.")
    }

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

    spacing()
    println("Select Foreign Currency = [$selectedCurrency]")
    println("Exchange Rate = ${String.format("%.2f", exchangeRate)}")
    println(" ")

    return doubleArrayOf(updatedUsd, updatedJpy, updatedGbp, updatedEur, updatedCny)
}


/**
 * Shows a day-by-day table of interest earned at 5% a year; does not change the account balance.
 * @param accounts the list of all registered accounts
 */
fun ShowInterestAmount(accounts: ArrayList<Account>){
    println("Show Interest Amount")
    val accountName = promptValidName()
    val account = findAccount(accounts, accountName)

    if (account == null) {
        println("Account not found. Please input a registered account.")
        println(" ")
        return
    }

    val annualRate = 0.05

    println("Current Balance: ${String.format("%.2f", account.balance)}")
    println("Currency: PHP")
    println("Interest Rate: 5%")
    println(" ")

    var totalDays: Int
    while (true) {
        print("Total Number of Days: ")
        val days = readln().trim().toIntOrNull()
        if (days != null && days > 0 && days < 365) {
            totalDays = days
            break
        }
        println("Invalid number of days. Please enter a number between 1 and 365.")
    }

    val dailyInterest = Math.round(account.balance * annualRate / 365 * 100) / 100.0
    var runningBalance = account.balance

    println(" ")
    println("Day | Interest | Balance |")
    for (day in 1..totalDays) {
        runningBalance += dailyInterest
        val dayText = day.toString().padEnd(3)
        val interestText = String.format("%.2f", dailyInterest).padEnd(8)
        val balanceText = String.format("%.2f", runningBalance).padEnd(7)
        println("$dayText | $interestText | $balanceText |")
    }
    println(" ")
}