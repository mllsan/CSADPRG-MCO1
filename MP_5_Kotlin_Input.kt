// ********************
// Last names: Cheng
// Language: Kotlin
// Paradigm(s): procedural (imperative), some OOP
// ********************



package mco1

/** Prints a blank line followed by a *** separator. */
fun spacing(){
    println(" ")
    println("***")
}

/**
 * Asks until the user enters a number. Text that isn't a number gets
 * "Invalid input. Please enter a number"; the value itself is checked by the caller.
 * @param label the prompt text shown before the input
 * @return the number entered
 */
fun promptNumber(label: String): Double {
    while (true) {
        print("$label: ")
        val value = readln().trim().toDoubleOrNull()
        if (value != null && value.isFinite()) {
            return value
        }
        println("Invalid input. Please enter a number")
    }
}

/**
 * Checks that an amount is a real, positive money value with at most 2 decimal places.
 * @param amount the amount to check
 * @return true if valid
 */
fun isValidMoney(amount: Double): Boolean =
    amount.isFinite() && amount > 0 && amount == Math.round(amount * 100.0) / 100.0

/**
 * Asks until the user enters a valid money amount (greater than 0, at most 2 decimal places).
 * Used for deposits and withdrawals.
 * @param label the prompt text shown before the input
 * @return the valid amount entered
 */
fun promptTransactionAmount(label: String): Double {
    while (true) {
        print("$label: ")
        val value = readln().trim().toDoubleOrNull()
        if (value != null && isValidMoney(value)) {
            return value
        }
        println("Invalid amount. Please enter a positive amount with at most 2 decimal places.")
    }
}

/**
 * Asks until the user enters a positive amount with at most 2 decimal places.
 * Non-numbers get "Invalid input"; numbers that are zero, negative or have
 * more than 2 decimals get "Invalid amount".
 * @param label the prompt text shown before the input
 * @return the valid amount entered
 */
fun promptCentavoAmount(label: String): Double {
    while (true) {
        val value = promptNumber(label)
        if (value > 0 && value == Math.round(value * 100.0) / 100.0) {
            return value
        }
        println("Invalid amount. Please enter a valid positive number.")
    }
}

/**
 * Asks until the user enters a valid money amount with at most 2 decimal places that is at least the given minimum.
 * @param label the prompt text shown before the input
 * @param minimum the smallest amount accepted
 * @return the valid amount entered
 */
fun promptMinimumAmount(label: String, minimum: Double): Double {
    while (true) {
        print("$label: ")
        val value = readln().trim().toDoubleOrNull()
        if (value != null && isValidMoney(value) && value >= minimum) {
            return value
        }
        println("Invalid initial deposit. Minimum amount is Php ${String.format("%,.2f", minimum)}")
    }
}

/**
 * Asks for a main menu choice until the user enters a number from 1 to 7.
 * @return the chosen menu number
 */
fun promptMenuChoice(): Int {
    while (true) {
        print("Choice: ")
        val choice = readln().trim().toIntOrNull()
        if (choice != null && choice in 1..7) {
            return choice
        }
        println("Invalid input. Please enter a number from 1 to 7.")
    }
}

/**
 * Shows the label and asks until the user enters Y or N.
 * @param label the prompt text shown before the input, e.g. "Back to the Main Menu (Y/N)"
 * @return true if the user entered Y, false if N
 */
fun promptYesNo(label: String): Boolean {
    while (true) {
        print("$label: ")
        val input = readln().trim().uppercase()
        if (input == "Y")
            return true
        if (input == "N")
            return false
        println("Invalid input. Please enter 'Y' or 'N' only.")
        println(" ")
    }
}

/**
 * Shows the currency list under a header and asks until the user picks 1 to 6.
 * @param header the title printed above the currency list
 * @param label the prompt text shown before the input
 * @return the chosen currency number (1 to 6)
 */
fun promptCurrencyChoice(header: String, label: String): Int {
    println(header)
    for (i in CURRENCY_NAMES.indices) {
        println("[${i + 1}] ${CURRENCY_NAMES[i]}")
    }
    println(" ")
    while (true) {
        print("$label: ")
        val choice = readln().trim().toIntOrNull()
        if (choice != null && choice in 1..7) {
            return choice
        }
        println("Invalid input. Please enter a number from 1 to 7.")
    }
}
