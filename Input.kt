/**
 * This is the file containing input-related functions for the Kotlin version of MCO1 for Group 5.
 *
 * @author Hannah Cheng
 * @version 2.1
 */


package mco1

/** Prints a blank line followed by a *** separator. */
fun spacing(){
    println(" ")
    println("***")
}

/**
 * Asks until the user enters a number greater than 0.
 * @param label the prompt text shown before the input
 * @return the valid positive amount entered
 */
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

/**
 * Asks for a main menu choice until the user enters a number from 1 to 6.
 * @return the chosen menu number
 */
fun promptMenuChoice(): Int {
    while (true) {
        print("Choice: ")
        val choice = readln().trim().toIntOrNull()
        if (choice != null && choice in 1..6) {
            return choice
        }
        println("Invalid input. Please enter a number.")
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
        if (choice != null && choice in 1..6) {
            return choice
        }
        println("Invalid input. Please enter a number from 1 to 6.")
    }
}
