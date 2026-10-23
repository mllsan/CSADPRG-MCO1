#prints the Main Menu Prompt
mainmenu <- function(){
	cat("\nSelect Transaction:\n[1] Register Account Name\n[2] Deposit Amount\n[3] Withdraw Amount\n[4] Currency Exchange\n[5] Record Exchange Rates\n[6] Show Interest Amount\n\n")
}

#prints the choice made by the user
choiceselection <- function(choice){
	cat("\n***\nChoice = ",choice,"\n\n")

}

#asks the user if they would like to continue making transactions
promptmainmenu <- function(){
	choice = readline("Back to the Main Menu (Y/N): ")

	if (choice == "N"){
		choice <- FALSE
	} else {
		choice <- TRUE
	}
	
	choice

}

#checks if the string input is covertable to a numerical value
#returns the numerical value if covertable, else returns 0
isvalidnum <- function(input){
	input <- suppressWarnings(as.double(input))

    if (is.na(input) | input < 0){
    	input <- 0
    }
    	
    input
}

#converts a string curName in the format "[#]" to a numerical value
#returns the numerical value if covertable, else returns 0
getcurrencynum <- function(curName){
	num <- 0
	if (curName == "[1]"){
		num <- 1
	} else if(curName == "[2]"){
		num <- 2
	} else if(curName == "[3]"){
		num <- 3
	}else if(curName == "[4]"){
		num <- 4
	}else if(curName == "[5]"){
		num <- 5
	}else if(curName == "[6]"){
		num <- 6
	}
	num
}

#finds the index of a account with an account name equal to input name in a list accounts 
#returns the index of the account if found, else returns 0
findacc <- function(name){
	index <- 0
	found <- FALSE

	for (acc in accounts){
		if(!found){
		index <- index + 1
		}

		if(name == acc[1]){
			found <- TRUE	
		}

		
	}

	if(!found){
		index <- 0
	}

	index

}

#returns a numerical value num in the string format #.## 
formatdecimal <- function(num){
	formatted <- format(round(num, 2), nsmall = 2)
	formatted
}

#function for adding an account to the system
registeracc <-function(){

    cat("Register Account Name\n")

    #User Input Account Name
    accName <- readline("Account Name: ")
    
    #Checks if the inputed Account Name is Valid or if it is already taken, repeats until the valid
    while (!grepl(", ", accName) | findacc(accName) != 0){
        cat("Invalid account name. Please follow the format 'lastname, firstname' with letters only.\n\n")
        cat("Register Account Name\n")
        accName <- readline("Account Name: ")
    }

  	cat("\n***\nAccount Name = ", accName, "\n")

    #Initializes the account 
    bal <- 1000 
    currency <- "PHP"

    account <- list (
        accName,
        bal <- 1000 ,
        currency <- "PHP"

    )

	account
}

#function for doing a deposit on an account
deposit <-function(acc){
	
	cat("Current Balance: ", unlist(acc[2]), "\n")
	cat("Currency: ", unlist(acc[3]), "\n\n")
    
	#asks for deposit amount
	depAmt <- readline("Deposit Amount: ")
	depAmt <- isvalidnum(depAmt)

	#checks if the deposit amount is valid, repeats until the valid
    while(depAmt <= 0){
        cat("Invalid amount. Please enter a valid positive number.\n\n")

        depAmt <- readline("Deposit Amount: ")
	    depAmt <- isvalidnum(depAmt)
    }

    cat("\n***\n")
  	cat("Account Name = ", unlist(acc[1]), "\n")
	cat("Deposit Amount = ", formatdecimal(depAmt), "\n")

	#updates balance
	acc[2] <- isvalidnum(acc[2]) + depAmt

	cat("Updated Balance = ", formatdecimal(unlist(acc[2])), "\n")

	#returns the new updated balance with the deposited amount
	acc[2]
}

#function for doing a withdraw on an account
withdraw <- function (acc){

    cat("Current Balance: ", unlist(acc[2]), "\n")
	cat("Currency: ", unlist(acc[3]), "\n\n")
    
	#asks for withdraw amount
    witAmt <- readline("Withdraw Amount: ")
    witAmt <- isvalidnum(witAmt)

	#checks if the withdraw amount is valid, repeats until the valid
    while(witAmt <= 0 | witAmt > acc[2]){
        cat("Invalid amount. Please enter a valid positive number.\n\n")

        witAmt <- readline("Withdraw Amount: ")
        witAmt <- isvalidnum(witAmt)
    }
   
  	cat("\n***\nAccount Name = ", unlist(acc[1]), "\n")
	cat("Withdraw Amount = ", formatdecimal(witAmt), "\n")

	#updates balance
    acc[2] <- isvalidnum(acc[2]) - witAmt

	cat("Updated Balance = ",formatdecimal(unlist(acc[2])),"\n")

	#returns the new updated balance with the withdrawn amount
	acc[2]
	

}

#function for prompting the source currency exchange
sourceexchange <- function(){
	cat("Foreign Currency Exchange\n")
	cat("Source Currency Options:\n")
    cat("[1] Philippine Peso (PHP)\n")
	cat("[2] United States Dollar (USD)\n")
    cat("[3] Japanese Yen (JPY)\n")
    cat("[4] British Pound Sterling (GBP)\n")
    cat("[5] Euro (EUR)\n")
    cat("[6] Chinese Yuan Renminni (CNY)\n\n")
   	
	#gets source currency index
   	srcCurr <- readline("Source Currency: ")
	srcCurr <- isvalidnum(srcCurr)
	
	#checks if it is a valid source currency index, repeats until the valid
	while (srcCurr > 6 | srcCurr <= 0 ){

		if (srcCurr == 0){
			cat("Invalid input. Please enter a number.")
		}
		else {
			cat("Foreign Currency Exchange\n")
			cat("Source Currency Options:\n")

			cat("[1] Philippine Peso (PHP)\n")
			cat("[2] United States Dollar (USD)\n")
			cat("[3] Japanese Yen (JPY)\n")
			cat("[4] British Pound Sterling (GBP)\n")
			cat("[5] Euro (EUR)\n")
			cat("[6] Chinese Yuan Renminni (CNY)\n\n")
		}
			
		srcCurr <- readline("Source Currency: ")
		srcCurr <- isvalidnum(srcCurr)
	}

	#returns the source currency index,
	srcCurr
}

#function for prompting the foreign currency exchange
foreignexchange <- function(srcRate, amt){
	
	cat("\nExchanged Currency Options:\n")
    cat("[1] Philippine Peso (PHP)\n")
    cat("[2] United States Dollar (USD)\n")
    cat("[3] Japanese Yen (JPY)\n")
    cat("[4] British Pound Sterling (GBP)\n")
    cat("[5] Euro (EUR)\n")
    cat("[6] Chinese Yuan Renminni (CNY)\n\n")
   	
	#gets foreign currency index
   	exeCurr <- readline("Exchange Currency: ")
	exeCurr <- isvalidnum(exeCurr)

	#checks if it is a valid foreign currency index, repeats until the valid
	while (exeCurr > 6 | exeCurr <= 0 ){
		if (exeCurr == 0){
			cat("Invalid input. Please enter a number.")
		} else {
			cat("\nExchanged Currency Options:\n")
			cat("[1] Philippine Peso (PHP)\n")
			cat("[2] United States Dollar (USD)\n")
			cat("[3] Japanese Yen (JPY)\n")
			cat("[4] British Pound Sterling (GBP)\n")
			cat("[5] Euro (EUR)\n")
			cat("[6] Chinese Yuan Renminni (CNY)\n\n")
		}
			
		exeCurr <- readline("Source Currency: ")
		exeCurr <- isvalidnum(exeCurr)
	}

	#calculates the exchanged amount 
	exeRate <- exeRates[exeCurr] 
	exeRate <- isvalidnum(exeRate)
	exeamt <- (srcRate / exeRate) * amt

	#returns the exchanged amount 
	exeamt
}

#function for prompting the record currency change
recordexchangeindex <- function(){
	cat("Record Exchange Rate\n\n")
    cat("[1] Philippine Peso (PHP)\n")
	cat("[2] United States Dollar (USD)\n")
    cat("[3] Japanese Yen (JPY)\n")
    cat("[4] British Pound Sterling (GBP)\n")
    cat("[5] Euro (EUR)\n")
    cat("[6] Chinese Yuan Renminni (CNY)\n")

	#gets the exchange rate index
   	excCurr <- readline("Select Foreign Currency: ")
   	excCurr <- getcurrencynum(excCurr)

	#returns the exchange rate index
	excCurr
}

#function for getting new exchange rate value
recordexchangerate <- function(){
	excRate <- readline("Exchange Rate: ")
   	excRate <- isvalidnum(excRate)

	#returns the exchange rate value
	excRate
}

#function showing interest and balance per day
showinterestamount <- function(rate, day, balance){
	balance <- isvalidnum(balance)
	rate <- (isvalidnum(rate) / 365) * 0.01
	day <- isvalidnum(day)

	cat("Day\t| Interest\t| Balance\t|\n")
	for (x in 1:day){
		interest <- round(balance * rate, 2)
		balance <- balance + interest
		cat(x,"\t| ", interest, "\t| ", balance,  "\t|\n")
		
	}
	

}


active <- TRUE
choice <- 0
exeRates = c(1.00, 62.00, 0.40, 84.00, 72.00, 9.00)
count = 0
accounts <- list()

while (active){
	
 	mainmenu()
 	
    choice = readline("Choice: ")
       
    choiceselection(choice)
       
    if(choice == "1"){
		count <- count + 1
  		accounts[[count]] <- registeracc()

    } else if (choice == "2") {
		
		cat("Deposit Account\n")
		accName <- readline("Account Name: ")
		index <- findacc(accName)

		if(index != 0 ){
			accounts[[index]][2] <- deposit(accounts[[index]])
		} else {
			cat("Account not found. Please input a registered account.\n")
		}

    } else if (choice == "3") {
		cat("Withdraw Account\n")
		accName <- readline("Account Name: ")
		index <- findacc(accName)

		if(index != 0 ){
			accounts[[index]][2] <- withdraw(accounts[[index]])
		}

    	
    } else if (choice == "4") {
		repeat_exchange <- 1

		srcCurr <- sourceexchange()

		amt <- readline("Source Amount: ")
		amt <- isvalidnum(amt)

		while(amt <= 0){
			cat("Invalid amount. Please enter a valid positive number.\n")
			amt <- readline("Source Amount: ")
			amt <- isvalidnum(amt)
    	}

   	 	srcRate <- exeRates[srcCurr] 
   	 	srcRate <- isvalidnum(srcRate)


		while (repeat_exchange == 1){
			exeamt <- foreignexchange(srcRate, amt)
			cat("Exchange Amount: ", formatdecimal(exeamt))

			repeat_exchange <- readline("Convert another currency (Y/N): ")

			if(repeat_exchange == "N"){
				repeat_exchange <- 0
			}

		}
   	 	

    } else if (choice == "5") {
   	 	excCurr <- recordexchangeindex()
		excRate <- recordexchangerate()
   	 	exeRates[excCurr] <- excRate
   	 	
   	 	cat("\n***\n")
  		cat("Select Foreign Currency = ", excCurr, "\n")
		cat("Exchange Rate = ", exeRates[excCurr], "\n")
    	
    } else if (choice == "6") {
		cat("Deposit Account\n")
		accName <- readline("Account Name: ")
		index <- findacc(accName)
		acc <- accounts[[index]]

    	cat("Account Name: ", unlist(acc[1]), "\n")
    	cat("Current Balance: ", unlist(acc[2]), "\n")
    	cat("Currency: ", unlist(acc[3]), "\n")

		rate <- readline("Interest Rate: ")
		day <- readline("Total Number of Days: ")
		showinterestamount(rate, day, acc[2])
   	} else {
    	cat("\n\nInvalid Choice.\n\n")
    }

	cat("\n")
	active <- promptmainmenu()

	if (!active){
		cat("\nExiting...\n")
	}

    cat("\n")
}
