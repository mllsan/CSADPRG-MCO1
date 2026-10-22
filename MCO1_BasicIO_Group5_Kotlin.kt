/**
 * This is the UNFINISHED Kotlin version of MCO1 for Group 5.
 *
 * @author Hannah Cheng
 * @version 2.0
 */

/**
 * TODO
 * double check if everythings the same
 * comments
 */
class Account(val name: String, var balance: Double = 1000.0)

fun spacing(){
    println(" ")
    println("***")
}

fun isValidName(input: String): Boolean {
    val parts = input.split(",")
    return parts.size == 2 && parts[0].isNotBlank() && parts[1].isNotBlank()
}


fun promptValidName(): String {
    while (true) {
        print("Account Name (Lastname, Firstname): ")
        val input = readln()
        if (isValidName(input)) {
            return input
        }
        println("Invalid account name. Please follow the format 'lastname, firstname' with letters only.")
    }
}

fun findAccount(accounts: ArrayList<Account>, name: String): Account? =
    accounts.find { it.name.equals(name, ignoreCase = true) }

fun promptPositiveAmount(label: String): Double {
    while (true) {
        print("$label: ")
        val value = readln().trim().toDoubleOrNull()
        if (value != null && value > 0) {
            return value
        }
        println("Invalid amount. Please enter a valid positive number.")
    }
}

fun promptMenuChoice(): Int {
    while (true) {
        val choice = readln().trim().toIntOrNull()
        if (choice != null && choice in 1..6) {
            return choice
        }
        println("Invalid input. Please enter a number.")
    }
}

fun promptYesNo(): Boolean {
    while (true) {
        val input = readln().trim().uppercase()
        if (input == "Y")
            return true
        if (input == "N")
            return false
        println("Invalid input. Please enter 'Y' or 'N' only.")
    }
}
//----------------------------------------------------

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
    println("Deposit Amount = ${String.format("%.2f", depositAmount)}")
    println("New Balance = ${String.format("%.2f", account.balance)}")
    println(" ")
}
fun WithdrawAmount(accounts: ArrayList<Account>){
    println("Withdraw Amount")
    var accountName = promptValidName()
    var account = findAccount(accounts, accountName)

    if (account == null) {
        spacing()
        println("Account not found. Please input a registered account.")
        println(" ")
        return
    }

    println("Current Balance: ${String.format("%.2f", account.balance)}")
    println("Currency: PHP")

    var withdrawAmount = promptPositiveAmount("Withdraw Amount")

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

val CURRENCY_NAMES = arrayOf(
    "Philippine Peso (PHP)",
    "United States Dollar (USD)",
    "Japanese Yen (JPY)",
    "British Pound Sterling (GBP)",
    "Euro (EUR)",
    "Chinese Yuan Renminbi (CNY)"
)

fun promptCurrencyChoice(label: String): Int {
    for (i in CURRENCY_NAMES.indices) {
        println("[${i + 1}] ${CURRENCY_NAMES[i]}")
    }
    while (true) {
        print("$label: ")
        val choice = readln().trim().toIntOrNull()
        if (choice != null && choice in 1..6) {
            return choice
        }
        println("Invalid input. Please enter a number from 1 to 6.")
    }
}

fun CurrencyExchange(usdRate: Double, jpyRate: Double, gbpRate: Double, eurRate: Double, cnyRate: Double) {

    val phpPerUnit = doubleArrayOf(1.0, usdRate, jpyRate, gbpRate, eurRate, cnyRate)

    println("Foreign Currency Exchange")
    val source = promptCurrencyChoice("Source Currency")
    val sourceAmount = promptPositiveAmount("Source Amount")

    var convertAgain: Boolean
    do {
        println(" ")
        var target: Int
        target = promptCurrencyChoice("Exchanged Currency")


        val exchangedAmount = sourceAmount * phpPerUnit[source - 1] / phpPerUnit[target - 1]

        println(" ")

        /*for (i in CURRENCY_NAMES.indices) {
            println("[${i + 1}] ${CURRENCY_NAMES[i]}")
        }*/

        println("Exchange Amount: ${String.format("%.2f", exchangedAmount)}")
        println(" ")

        println("Convert another currency (Y/N):")
        convertAgain = promptYesNo()
    } while (convertAgain)
}

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

fun ShowInterestAmount(accounts: ArrayList<Account>){
    println("Show Interest Amount")
    val accountName = promptValidName()
    val account = findAccount(accounts, accountName)

    if (account == null) {
        spacing()
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

    val dailyInterest = account.balance * annualRate / 365
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

        println("Select Transaction:")
        println("[1] Register Account Name")
        println("[2] Deposit Amount")
        println("[3] Withdraw Amount")
        println("[4] Currency Exchange")
        println("[5] Record Exchange Rates")
        println("[6] Show Interest Amount")

        choice = promptMenuChoice()
        println("Choice: $choice")
        spacing()
        println("Choice = $choice")

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

        println("Back to the Main Menu (Y/N):")
        backToMain = if (promptYesNo()) 'Y' else 'N'

    }

}
