
source("/Users/irisrafaelasalvador/R/account_registry.R")

mainmenu <- function(){
	cat("\nSelect Transaction:\n[1] Register Account Name\n[2] Deposit Amount\n[3] Withdraw Amount\n[4] Currency Exchange\n[5] Record Exchange Rates\n[6] Show Interest Amount\n\n")
}

choiceselection <- function(choice){
	cat("\n***\nChoice = ",choice,"\n\n")

}

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

isvalidnum <- function(input){
	input <- suppressWarnings(as.double(input))

    if (is.na(input) | input < 0){
    	input <- 0
    }
    	
    input
}

promptmainmenu <- function(){
	choice = readline("Back to the Main Menu (Y/N): ")

	if (choice == "N"){
		choice <- FALSE
	} else {
		choice <- TRUE
	}
	
	choice

}

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

formatdecimal <- function(num){
	formatted <- format(round(num, 2), nsmall = 2)
	formatted
}

recordexchangeindex <- function(){
	cat("Record Exchange Rate\n\n")
    cat("[1] Philippine Peso (PHP)\n")
	cat("[2] United States Dollar (USD)\n")
    cat("[3] Japanese Yen (JPY)\n")
    cat("[4] British Pound Sterling (GBP)\n")
    cat("[5] Euro (EUR)\n")
    cat("[6] Chinese Yuan Renminni (CNY)\n")

   	excCurr <- readline("Select Foreign Currency: ")
   	excCurr <- getcurrencynum(excCurr)

	excCurr
}

recordexchangerate <- function(){
	excRate <- readline("Exchange Rate: ")
   	excRate <- isvalidnum(excRate)

	excRate
}

selectsourcecurrency <- function(){
	cat("Source Currency Option:\n\n")
    cat("[1] Philippine Peso (PHP)\n")
	cat("[2] United States Dollar (USD)\n")
    cat("[3] Japanese Yen (JPY)\n")
    cat("[4] British Pound Sterling (GBP)\n")
    cat("[5] Euro (EUR)\n")
    cat("[6] Chinese Yuan Renminni (CNY)\n")

   	srcCurr <- readline("Source Currency: ")
   	srcCurr <- isvalidnum(srcCurr)

	srcCurr
}

selectexchangedcurrency <- function(){
	cat("Source Currency Option:\n\n")
    cat("[1] Philippine Peso (PHP)\n")
	cat("[2] United States Dollar (USD)\n")
    cat("[3] Japanese Yen (JPY)\n")
    cat("[4] British Pound Sterling (GBP)\n")
    cat("[5] Euro (EUR)\n")
    cat("[6] Chinese Yuan Renminni (CNY)\n")

   	excCurr <- readline("Source Currency: ")
   	excCurr <- isvalidnum(excCurr)

	excCurr
}

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

foreignexechange <- function(srcRate, amt){
	
		cat("\nExchanged Currency Options:\n")

    	cat("[1] Philippine Peso (PHP)\n")
    	cat("[2] United States Dollar (USD)\n")
    	cat("[3] Japanese Yen (JPY)\n")
    	cat("[4] British Pound Sterling (GBP)\n")
    	cat("[5] Euro (EUR)\n")
    	cat("[6] Chinese Yuan Renminni (CNY)\n\n")
   	 	
   	 	exeCurr <- readline("Exchange Currency: ")
		exeCurr <- isvalidnum(exeCurr)

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

		exeRate <- exeRates[exeCurr] 
		exeRate <- isvalidnum(exeRate)
		exeamt <- (srcRate / exeRate) * amt

		exeamt
}


active <- TRUE
choice <- 0
exeRates = c(1.00, 62.00, 0.40, 84.00, 72.00, 9.00)
count = 1 #change this later
accounts <- list()
accounts[[count]] <- list("Salvador, Iris", 1000, "PHP")

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

    	cat("Foreign Currency Exchange\n")
		cat("Source Currency Options:\n")

    	cat("[1] Philippine Peso (PHP)\n")
    	cat("[2] United States Dollar (USD)\n")
    	cat("[3] Japanese Yen (JPY)\n")
    	cat("[4] British Pound Sterling (GBP)\n")
    	cat("[5] Euro (EUR)\n")
    	cat("[6] Chinese Yuan Renminni (CNY)\n\n")
   	 	
   	 	srcCurr <- readline("Source Currency: ")
		srcCurr <- isvalidnum(srcCurr)
		
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
			exeamt <- foreignexechange(srcRate, amt)
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
    	cat("Not a Valid Option")
    }

	cat("\n")
	active <- promptmainmenu()

	if (!active){
		cat("\nExiting...\n")
	}

    cat("\n")
}
