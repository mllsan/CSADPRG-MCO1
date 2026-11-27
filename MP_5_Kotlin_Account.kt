// ********************
// Last names: Cheng
// Language: Kotlin
// Paradigm(s): procedural (imperative), some OOP
// ********************


package mco1

/**
 * A bank account with a name, a 10-digit account number and a balance in PHP.
 * @property name the account name in "Firstname Lastname" format
 * @property number the 10-digit account number
 * @property balance the current balance, which starts at 0.0 until the initial deposit is made
 */
class Account(val name: String, val number: Long, var balance: Double = 0.0)

/**
 * Looks up an account by name, ignoring upper/lower case.
 * @param accounts the list of all registered accounts
 * @param name the account name to search for
 * @return the matching account, or null if none exists
 */
fun findAccount(accounts: ArrayList<Account>, name: String): Account? =
    accounts.find { it.name.equals(name, ignoreCase = true) }

/**
 * Checks that the input is at least two words of letters only, separated by single spaces (Firstname Lastname).
 * @param input the text the user entered
 * @return true if the format is valid
 */
fun isValidName(input: String): Boolean {
    val words = input.trim().split(" ")
    return words.size >= 2 && words.all { word -> word.isNotEmpty() && word.all { it.isLetter() } }
}

/**
 * Asks for an account name until the format is valid.
 * @return the valid account name entered
 */
fun promptValidName(): String {
    while (true) {
        print("Account Name: ")
        val input = readln()
        if (isValidName(input)) {
            return input.trim()
        }
        println("Invalid account name. Please follow the format 'firstname lastname' with letters only.")
        println("")
        println("Register Account Name")
    }
}
