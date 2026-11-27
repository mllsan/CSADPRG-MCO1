// ********************
// Last names: Cheng
// Language: Kotlin
// Paradigm(s): procedural (imperative), some OOP
// ********************

package mco1

import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

// paths for reports
const val REPORT1_PATH = "report1_banking_privilege_tiers.csv"
const val REPORT2_PATH = "report2_interest_expense_projection.csv"
const val REPORT3_PATH = "report3_suspicious_client_behavior.csv"
const val REPORT4_PATH = "report4_account_overview.csv"
const val SUMMARY_PATH = "summary.json"

const val MIN_AVERAGE_BALANCE = 100_000.0
const val GOLD_THRESHOLD = 500_000.0
const val PLATINUM_THRESHOLD = 1_000_000.0

const val MIN_INTEREST_BALANCE = 10_000.0
const val ANNUAL_INTEREST_RATE = 0.05
const val PROJECTION_DAYS = 30

const val SUSPICIOUS_WITHDRAWAL = 500_000.0
const val SUSPICIOUS_VELOCITY = 90.0

val CURRENCY_NAMES = arrayOf(
    "Philippine Peso (PHP)",
    "United States Dollar (USD)",
    "Japanese Yen (JPY)",
    "British Pound Sterling (GBP)",
    "Euro (EUR)",
    "Chinese Yuan Renminni (CNY)"
)

/**
 * Registers accounts, asking after each one whether to register another.
 * @param accounts the list of all registered accounts
 */
fun RegisterAccountName(accounts: ArrayList<Account>) {
    do {
        run {
            println("Register Account")
            val accountName = promptValidName()

            if (findAccount(accounts, accountName) != null) {
                spacing()
                println("Account already registered. Please enter a different name.")
                println(" ")
                return@run
            }

            val number = nextAccountNumber()
            if (number == null) {
                println("No more account numbers available.")
                println(" ")
                return@run
            }

            var initialDeposit: Double
            while (true) {
                initialDeposit = promptNumber("Initial Deposit")
                if (initialDeposit >= MIN_INITIAL_DEPOSIT)
                    break
                println("Invalid initial deposit. Minimum amount is Php 5000.00")
            }

            val account = Account(accountName, number, initialDeposit)


            try {
                saveTransaction(account, "deposit", initialDeposit, initialDeposit)
            } catch (e: IOException) {
                println("Unable to save the transaction to the CSV file.")
                println(" ")
                return@run
            }

            accounts.add(account)
            latestAccountNumber = number

            //spacing()
           // println("Account Name = $accountName")
            //println("Initial Deposit = ${String.format("%.2f", initialDeposit)}")
            println(" ")
            println("Account created successfully!")
            println(" ")
        }
    } while (promptYesNo("Register another account? (Y/N)"))
}

/**
 * Makes deposits, asking after each one whether to deposit another amount.
 * @param accounts the list of all registered accounts
 */
fun DepositAmount(accounts: ArrayList<Account>) {
    do {
        run {
            println("Deposit Amount")
            val account = findAccount(accounts, promptValidName())

            if (account == null) {
                println("Account not found. Please input a registered account.")
                println(" ")
                return@run
            }

            println("Current Balance: ${String.format("%.2f", account.balance)}")
            println("Currency: PHP")
            println(" ")
            val depositAmount = promptCentavoAmount("Deposit Amount")
            val newBalance = roundMoney(account.balance + depositAmount)

            // save first, so a failed write leaves the balance unchanged
            try {
                saveTransaction(account, "deposit", depositAmount, newBalance)
            } catch (e: IOException) {
                println("Unable to save the transaction to the CSV file.")
                println(" ")
                return@run
            }
            account.balance = newBalance

            //spacing()
            //println("Account Name = ${account.name}")
           // println("")
            //println("Deposit Amount = ${String.format("%.2f", depositAmount)}")
            println(" ")
            println("Updated Balance: ${String.format("%.2f", account.balance)}")
            println(" ")
        }
    } while (promptYesNo("Deposit another amount? (Y/N)"))
}

/**
 * Makes withdrawals, asking after each one whether to withdraw another amount.
 * @param accounts the list of all registered accounts
 */
fun WithdrawAmount(accounts: ArrayList<Account>) {
    do {
        run {
            println("Withdraw Amount")
            val account = findAccount(accounts, promptValidName())

            if (account == null) {
                println("Account not found. Please input a registered account.")
                println(" ")
                return@run
            }

            println("Current Balance: ${String.format("%.2f", account.balance)}")
            println("Currency: PHP")
            println(" ")

            val withdrawAmount = promptCentavoAmount("Withdraw Amount")
            println(" ")

            if (Math.round(withdrawAmount * 100.0) > Math.round(account.balance * 100.0)) {
                spacing()
                println("Invalid withdrawal amount. Amount exceeds your current balance.")
                println(" ")
                return@run
            }

            val newBalance = roundMoney(account.balance - withdrawAmount)

            try {
                saveTransaction(account, "withdrawal", withdrawAmount, newBalance)
            } catch (e: IOException) {
                println("Unable to save the transaction to the CSV file.")
                println(" ")
                return@run
            }
            account.balance = newBalance

            //spacing()
            //println("Account Name = ${account.name}")
            //println("Withdraw Amount = ${String.format("%.2f", withdrawAmount)}")
            println("Updated Balance: ${String.format("%.2f", account.balance)}")
            println(" ")
        }
    } while (promptYesNo("Withdraw another amount? (Y/N)"))
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
    val sourceAmount = promptNumber("Source Amount")

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

    var exchangeRate: Double
    while (true) {
        print("Exchange Rate: ")
        val rate = readln().trim().toDoubleOrNull()
        if (rate != null && rate.isFinite() && rate > 0.0) {
            exchangeRate = rate
            break
        }
        println("Invalid input. Please enter a number")
    }

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

    println("Current Balance: ${String.format("%.2f", account.balance)}")
    println("Currency: PHP")
    println("Interest Rate: 5%")
    println(" ")

    var totalDays: Int
    while (true) {
        print("Total Number of Days: ")
        val days = readln().trim().toIntOrNull()
        if (days != null && days in 1..365) {
            totalDays = days
            break
        }
        println("Invalid number of days. Please enter a number between 1 and 365.")
    }

    val dailyInterest = roundMoney(account.balance * ANNUAL_INTEREST_RATE / 365)
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

//------------------------------------------ REPORT HELPERS

/** Formats a number with 2 decimal places. */
private fun money(value: Double): String = String.format(Locale.US, "%.2f", value)

/** Rounds a money value to 2 decimal places. */
private fun roundMoney(value: Double): Double = Math.round(value * 100.0) / 100.0

/** Writes a header and rows to a csv file. @throws IOException if the file cannot be written */
private fun writeCsv(path: String, header: String, rows: List<List<String>>) {
    val sb = StringBuilder(header).append(System.lineSeparator())
    for (row in rows) {
        sb.append(row.joinToString(",")).append(System.lineSeparator())
    }
    File(path).writeText(sb.toString())
}

/** Shows the report title, column header, first 2 rows and the export message. */
private fun printPreview(title: String, header: String, rows: List<List<String>>, path: String) {
    println(" ")
    println(title)
    println(header)
    for (row in rows.take(2)) {
        println(row.joinToString(" | "))
    }
    println(" ")
    println("(Full table exported to $path)")
}

//------------------------------------------ REPORT 1

/**
 * Report 1: Banking Privilege Tiers.
 * @return rows of AccountName, AccountAge, CurrentBalance, AverageDailyBalance, PrivilegeTier
 */
fun buildReport1(): List<List<String>> {
    val today = LocalDate.now()
    val firstOfLastMonth = today.minusMonths(1).withDayOfMonth(1)
    val daysInMonth = firstOfLastMonth.lengthOfMonth()

    val byAccount = loadPhpTransactions()
        .sortedBy { it.dateTime }
        .groupBy { it.number }

    val rows = ArrayList<Pair<Double, List<String>>>()

    for ((_, txns) in byAccount) {
        var total = 0.0
        var endOfDay = 0.0
        var idx = 0

        for (i in 0 until daysInMonth) {
            val day = firstOfLastMonth.plusDays(i.toLong())
            while (idx < txns.size && !txns[idx].dateTime.toLocalDate().isAfter(day)) {
                endOfDay = txns[idx].balance
                idx++
            }
            total += endOfDay
        }

        val average = total / daysInMonth
        if (average < MIN_AVERAGE_BALANCE) continue

        val tier = when {
            average >= PLATINUM_THRESHOLD -> "Platinum"
            average >= GOLD_THRESHOLD -> "Gold"
            else -> "Silver"
        }
        val age = ChronoUnit.DAYS.between(txns.first().dateTime.toLocalDate(), today)

        rows.add(
            average to listOf(
                txns.last().name, age.toString(),
                money(txns.last().balance), money(average), tier
            )
        )
    }
    return rows.sortedByDescending { it.first }.map { it.second }
}

//------------------------------------------ REPORT 2

/**
 * Report 2: Interest Expense Projection.
 * @return rows of AccountName, CurrentBalance, ExpectedInterest
 */
fun buildReport2(): List<List<String>> {
    val latestPerAccount = loadPhpTransactions()
        .sortedBy { it.dateTime }
        .groupBy { it.number }
        .map { (_, txns) -> txns.last() }

    val rows = ArrayList<Pair<Double, List<String>>>()

    for (last in latestPerAccount) {
        if (last.balance < MIN_INTEREST_BALANCE) continue

        var projected = last.balance
        var expected = 0.0
        repeat(PROJECTION_DAYS) {
            val interest = roundMoney(projected * (ANNUAL_INTEREST_RATE / 365))
            expected += interest
            projected = roundMoney(projected + interest)
        }

        rows.add(
            expected to listOf(last.name, money(last.balance), money(expected))
        )
    }
    return rows.sortedByDescending { it.first }.map { it.second }
}

//------------------------------------------ REPORT 3

/**
 * Report 3: Suspicious Client Behavior
 * @return rows of AccountName, Date, TotalWithdrawalAmounts, NumberOfDeposits, TotalDepositAmounts,
 *         TurnoverVelocity (%), RiskFlagLevel
 */
fun buildReport3(): List<List<String>> {
    val dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // key = account number + date; value = [totalWithdrawals, numDeposits, totalDeposits]
    val daily = LinkedHashMap<Pair<Long, LocalDate>, DoubleArray>()
    val names = HashMap<Long, String>()

    for (t in loadPhpTransactions()) {
        val totals = daily.getOrPut(t.number to t.dateTime.toLocalDate()) { DoubleArray(3) }
        names[t.number] = t.name

        if (t.type == "deposit") {
            totals[1] += 1.0
            totals[2] += t.amount
        } else {
            totals[0] += t.amount
        }
    }

    val rows = ArrayList<Triple<LocalDate, Boolean, List<String>>>()

    for ((key, totals) in daily) {
        val withdrawals = totals[0]
        val deposits = totals[2]
        val velocity = if (deposits > 0) withdrawals / deposits * 100 else null
        val suspicious = withdrawals >= SUSPICIOUS_WITHDRAWAL || (velocity != null && velocity >= SUSPICIOUS_VELOCITY)

        rows.add(
            Triple(
                key.second, suspicious,
                listOf(
                    names[key.first]!!,
                    key.second.format(dateFmt),
                    money(withdrawals),
                    totals[1].toInt().toString(),
                    money(deposits),
                    if (velocity != null) money(velocity) else "N/A",
                    if (suspicious) "Suspicious" else "Normal"
                )
            )
        )
    }

    return rows
        .sortedWith(compareBy<Triple<LocalDate, Boolean, List<String>>> { it.first }.thenByDescending { it.second })
        .map { it.third }
}


/**
 * Report 4: Account Overview
 * essentially aggregates grouped by id, sorted by id
 * @return rows of AccountName, AccountNumber, TotalTransactions, TotalDeposits, TotalWithdrawals, CurrentBalance
 */
fun buildReport4(): List<List<String>> {
    val byAccount = loadPhpTransactions()
        .sortedBy { it.dateTime }
        .groupBy { it.number }

    val rows = ArrayList<Pair<Long, List<String>>>()

    for ((number, txns) in byAccount) {
        val last = txns.last()
        val deposits = txns.filter { it.type == "deposit" }.sumOf { it.amount }
        val withdrawals = txns.filter { it.type == "withdrawal" }.sumOf { it.amount }

        rows.add(
            number to listOf(
                last.name, String.format("%010d", number), txns.size.toString(),
                money(deposits), money(withdrawals), money(last.balance)
            )
        )
    }
    return rows.sortedBy { it.first }.map { it.second }
}


//------------------------------------------ GENERATE REPORTS MENU

/**
 * Reloads the csv file into the program's account list and shows how many rows were loaded and filtered for PHP.
 * @param accounts the list of all registered accounts, replaced with the accounts read from the file
 */
private fun loadFileOption(accounts: ArrayList<Account>): Boolean {
    return try {
        val result = loadAccounts()
        accounts.clear()
        accounts.addAll(result.accounts)
        println(" ")
        println("Processing dataset... (${result.totalRows} rows loaded, ${result.validRows} filtered for PHP currency)")
        println(" ")
        true
    } catch (e: IOException) {
        println("Error loading bank_transactions.csv file.")
        println("Please ensure that the file exists and is accessible.")
        false
    }
}

/**
 * Writes summary.json
 * @param report2 the rows of Report 2
 * @param report4 the rows of Report 4
 * @throws IOException if the file cannot be written
 */
private fun writeSummary(report2: List<List<String>>, report4: List<List<String>>) {
    val transactions = loadPhpTransactions()
    val depositCount = transactions.count { it.type == "deposit" }
    val withdrawalCount = transactions.count { it.type == "withdrawal" }

    val json = "{ \n" +
            "\"total_accounts\": ${report4.size},  \n" +
            "\"total_deposits\": $depositCount,  \n" +
            "\"total_withdrawals\": $withdrawalCount,  \n" +
            "\"total_balance\": ${money(report4.sumOf { it[5].toDouble() })},  \n" +
            "\"total_interest_expense\": ${money(report2.sumOf { it[2].toDouble() })} \n" +
            "}"

    File(SUMMARY_PATH).writeText(json + System.lineSeparator())

    println(" ")
    println("Summary State ($SUMMARY_PATH):")
    println(json)
}

/** Builds the four reports, shows preview of each, and exports the tables to csv files. */
private fun generateAllReports() {
    println(" ")
    println("Generating reports...")

    try {
        val report1 = buildReport1()
        writeCsv(
            REPORT1_PATH,
            "AccountName,AccountAge,CurrentBalance,AverageDailyBalance,PrivilegeTier",
            report1
        )
        printPreview(
            "Report 1: Banking Privilege Tiers",
            "AccountName | AccountAge | CurrentBalance | AverageDailyBalance | PrivilegeTier",
            report1, REPORT1_PATH
        )

        val report2 = buildReport2()
        writeCsv(REPORT2_PATH, "AccountName,CurrentBalance,ExpectedInterest", report2)
        printPreview(
            "Report 2: Interest Expense Projection",
            "AccountName | CurrentBalance | ExpectedInterest",
            report2, REPORT2_PATH
        )

        val report3 = buildReport3()
        writeCsv(
            REPORT3_PATH,
            "AccountName,Date,TotalWithdrawalAmounts,NumberOfDeposits,TotalDepositAmounts,TurnoverVelocity (%),RiskFlagLevel",
            report3
        )
        printPreview(
            "Report 3: Suspicious Client Behavior",
            "AccountName | Date | TotalWithdrawalAmounts | NumberOfDeposits | TotalDepositAmounts | TurnoverVelocity (%) | RiskFlagLevel",
            report3, REPORT3_PATH
        )

        val report4 = buildReport4()
        writeCsv(
            REPORT4_PATH,
            "AccountName,AccountNumber,TotalTransactions,TotalDeposits,TotalWithdrawals,CurrentBalance",
            report4
        )
        printPreview(
            "Report 4: Account Overview",
            "AccountName | AccountNumber | TotalTransactions | TotalDeposits | TotalWithdrawals | CurrentBalance",
            report4, REPORT4_PATH
        )

        writeSummary(report2, report4)
    } catch (e: IOException) {
        println("Error generating reports: ${e.message}")
    }
}


/**
 * Generate Reports menu: [1] Load the file, [2] Generate Reports.
 * The menu is shown again after loading the file, and ends once the reports are generated.
 * @param accounts the list of all registered accounts
 */
fun GenerateReports(accounts: ArrayList<Account>) {
    var choice: Int
    do {
        println("Generate Reports")
        println(" ")
        println("[1] Load the file")
        println("[2] Generate Reports")

        while (true) {
            print("Enter choice: ")
            val input = readln().trim().toIntOrNull()
            if (input != null && input in 1..2) {
                choice = input
                break
            }
            println("Invalid input. Please enter a number from 1 to 2.")
        }

        when (choice) {
            1 -> loadFileOption(accounts)
            2 -> generateAllReports()
        }
        println(" ")
    } while (choice != 2)
}
