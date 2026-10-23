// ********************
// Last names: Cheng
// Language: Kotlin
// Paradigm(s): procedural (imperative), some OOP
// ********************


package mco1

/**
 * A bank account with a name and a balance in PHP.
 * @property name the account name in "Lastname, Firstname" format
 * @property balance the current balance, which starts at 1000.0
 */
class Account(val name: String, var balance: Double = 1000.0)

/**
 * Looks up an account by name, ignoring upper/lower case.
 * @param accounts the list of all registered accounts
 * @param name the account name to search for
 * @return the matching account, or null if none exists
 */
fun findAccount(accounts: ArrayList<Account>, name: String): Account? =
    accounts.find { it.name.equals(name, ignoreCase = true) }

/**
 * Checks that the input is following proper formatting, which is two non-blank parts separated by a comma.
 * @param input the text the user entered
 * @return true if the format is valid
 */
fun isValidName(input: String): Boolean {
    val parts = input.split(",")
    return parts.size == 2 && parts[0].isNotBlank() && parts[1].isNotBlank()
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
            return input
        }
        println("Invalid account name. Please follow the format 'lastname, firstname' with letters only.")
        println("")
        println("Register Account Name")
    }
}
