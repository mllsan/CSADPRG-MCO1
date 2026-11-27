// ********************
// Last names: Cheng
// Language: Kotlin
// Paradigm(s): procedural (imperative), some OOP
// ********************

package mco1

import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import java.util.Locale

const val CSV_PATH = "bank_transactions.csv"
const val MIN_INITIAL_DEPOSIT = 5000.0
const val MAX_ACCOUNT_NUMBER = 9999999999L

//STRICT so impossible dates are rejected (REQ-0004)
private val READ_DATE = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)
private val READ_TIME = DateTimeFormatter.ofPattern("H:mm:ss").withResolverStyle(ResolverStyle.STRICT)
private val WRITE_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private val WRITE_TIME = DateTimeFormatter.ofPattern("HH:mm:ss")
private val ACCOUNT_NUMBER_FORMAT = Regex("[0-9]{10}")

/** Highest valid account number seen in the csv file (or registered since); the next account gets this + 1. */
var latestAccountNumber = 0L

/**
 * One valid row of the csv file: financial fields are doubles, date and time are a real date-time.
 */
class TransactionRow(
    val name: String, val number: Long, val dateTime: LocalDateTime, val currency: String,
    val type: String, val amount: Double, val balance: Double
)

/**
 * The accounts rebuilt from the csv file with a count of what happened to each row.
 * @property totalRows data rows read (header excluded)
 * @property validRows valid PHP rows used to build the accounts
 * @property invalidRows rows that failed validation (wrong column count, bad date/time, missing or bad values)
 * @property excludedRows valid rows in a currency other than PHP
 */
class LoadResult(
    val accounts: ArrayList<Account>,
    val totalRows: Int, val validRows: Int, val invalidRows: Int, val excludedRows: Int
)

/**
 * Validates and cleans one csv row (already split into columns). Incomplete or invalid rows are filtered.
 * @return the cleaned row, or null if the row is invalid
 */
private fun parseRow(f: List<String>): TransactionRow? {
    if (f.size != 8) return null
    return try {
        val name = f[0].trim()
        val accNumber = f[1].trim()
        val currency = f[4].trim()
        val type = f[6].trim()

        if (name.isEmpty() || !accNumber.matches(ACCOUNT_NUMBER_FORMAT) || type.isEmpty()) return null

        val date = LocalDate.parse(f[2].trim(), READ_DATE)
        val time = LocalTime.parse(f[3].trim(), READ_TIME)
        val amount = f[5].trim().toDouble()
        val balance = f[7].trim().toDouble()

        if (!amount.isFinite() || !balance.isFinite() || amount <= 0 || balance < 0 ||
            (type != "deposit" && type != "withdrawal")
        ) return null

        TransactionRow(name, accNumber.toLong(), LocalDateTime.of(date, time), currency, type, amount, balance)
    } catch (e: Exception) {
        null
    }
}

/**
 * Reads bank_transactions.csv and rebuilds the accounts. Only valid PHP rows build accounts
 * @return the accounts and the row counts
 * @throws IOException if the csv file cannot be read or accessed
 */
fun loadAccounts(): LoadResult {
    val accounts = ArrayList<Account>()
    val byNumber = HashMap<Long, Account>()
    var total = 0
    var valid = 0
    var invalid = 0
    var excluded = 0
    latestAccountNumber = 0L

    val lines = File(CSV_PATH).readLines()   // throws if the file can't be opened

    for (line in lines.drop(1)) {            // drop(1) skips the header
        total++
        val row = parseRow(line.split(","))
        if (row == null) {
            invalid++
            continue
        }

        if (row.number > latestAccountNumber) latestAccountNumber = row.number

        if (!row.currency.equals("PHP", ignoreCase = true)) {
            excluded++
            continue
        }

        val existing = byNumber[row.number]
        if (existing == null) {
            val account = Account(row.name, row.number, row.balance)
            byNumber[row.number] = account
            accounts.add(account)
        } else {
            existing.balance = row.balance
        }
        valid++
    }
    return LoadResult(accounts, total, valid, invalid, excluded)
}

/**
 * Reads every valid PHP transaction row from the csv file (invalid and non-PHP rows are skipped).
 * Used by the reports, like the report methods in BankModel.java.
 * @throws IOException if the csv file cannot be read or accessed
 */
fun loadPhpTransactions(): List<TransactionRow> =
    File(CSV_PATH).readLines().drop(1)
        .mapNotNull { parseRow(it.split(",")) }
        .filter { it.currency.equals("PHP", ignoreCase = true) }

/**
 * Gives the next account number: the next smallest unused number
 * @return the next account number, or null if 10 digits are used up
 */
fun nextAccountNumber(): Long? =
    if (latestAccountNumber >= MAX_ACCOUNT_NUMBER) null else latestAccountNumber + 1

/**
 * Appends a transaction row to the csv file.
 * @param account the account involved
 * @param type "deposit" or "withdrawal"
 * @param amount the amount deposited or withdrawn
 * @param newBalance the balance after the transaction
 * @throws IOException if the file is missing, empty, or can't be written
 */
fun saveTransaction(account: Account, type: String, amount: Double, newBalance: Double) {
    val file = File(CSV_PATH)
    if (!file.isFile || file.length() == 0L) {
        throw IOException("Transaction CSV file not found or file is empty.")
    }
    val now = LocalDateTime.now()
    val nl = System.lineSeparator()

    val sb = StringBuilder()
    if (!endsWithNewline(file)) sb.append(nl)
    sb.append(account.name).append(',')
        .append(String.format("%010d", account.number)).append(',')
        .append(now.format(WRITE_DATE)).append(',')
        .append(now.format(WRITE_TIME)).append(',')
        .append("PHP").append(',')
        .append(String.format(Locale.US, "%.2f", amount)).append(',')
        .append(type).append(',')
        .append(String.format(Locale.US, "%.2f", newBalance))
        .append(nl)
    file.appendText(sb.toString())
}

//creates newline
private fun endsWithNewline(file: File): Boolean {
    java.io.RandomAccessFile(file, "r").use {
        it.seek(it.length() - 1)
        return it.read() == '\n'.code
    }
}
