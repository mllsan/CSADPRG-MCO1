mainmenu <- function(){
	cat("Select Transaction:\n[1] Register Account Name\n[2] Deposit Amount\n[3] Withdraw Amount\n[4] Currency Exchange\n[5] Record Exchange Rates\n[6] Show Interest Amount\n\n")
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
	choice = readline("Back to the Main Menu (Y/N):")

	if (choice == "N"){
		choice <- FALSE
	} else {
		choice <- TRUE
	}
	
	choice

}

registeracc <-function(){
	cat("Register Account Name\n")
  	accName <- readline("Account Name: ")
  	cat("***\n")
  	cat("Account Name = ", accName, "\n")
	accName
}

deposit <-function(){
	cat("Deposit Account\n")
    cat("Account Name: ", accName, "\n")
    cat("Current Balance: ", curBal, "\n")
    cat("Currency: ", curr, "\n")
    	
    depAmt <- readline("Deposit Amount: ")
	depAmt <- isvalidnum(depAmt)

    cat("\n***")
  	cat("Account Name = ",accName, "\n")
	cat("Deposit Amount = ", depAmt, "\n")

	depAmt
}

withdraw <- function (curBal){
	cat("Withdraw Account\n")
    cat("Account Name: ",accName, "\n")
    cat("Current Balance: ", curBal, "\n")
	cat("Currency: ", curr, "\n")
    	
    witAmt <- readline("Withdraw Amount: ")
    witAmt <- isvalidnum(witAmt)
    	
  	cat("\n***")
  	cat("Account Name = ", accName, "\n")
	cat("Withdraw Amount = ", witAmt, "\n")

	if (witAmt > curBal | witAmt <= 0 ){
		witAmt <- 0
		cat("\nInvalid Input\n")
	}

	witAmt

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

active <- TRUE

while (active){
	choice <- 0
	accName <- ""
	curBal <- 1000
	curr <- "PHP"
	exeRates = c(1,62,0.40,84,72,9)
	
 	mainmenu()
 	
    choice = readline("Choice: ")
       
    choiceselection(choice)
       
    if(choice == "1"){
  		accName <- registeracc()

    } else if (choice == "2") {
		curBal <- curBal + deposit()
		cat("Updated Balance: ", curBal)

    } else if (choice == "3") {
		curBal <- curBal - withdraw(curBal)
		cat("Updated Balance: ", curBal)
    	
    } else if (choice == "4") {

    	cat("Foreign Currency Exchange\n")

    	cat("[1] Philippine Peso (PHP) = ", (exeRates[0] * curBal) , "\n"  )
    	cat("[2] United States Dollar (USD) = ", (exeRates[1] * curBal) , "\n"  )
    	cat("[3] Japanese Yen (JPY) = ", (exeRates[2] * curBal), "\n")
    	cat("[4] British Pound Sterling (GBP) = ", (exeRates[3] * curBal) , "\n" )
    	cat("[5] Euro (EUR) = ", (exeRates[4] * curBal) , "\n")
    	cat("[6] Chinese Yuan Renminni (CNY) = ", (exeRates[5] * curBal) , "\n")
   	 	cat("")
   	 	
   	 	excCurr <- readline("Select Foreign Currency: ")
   	 	excRate <- readline("Exchange Rate: ")
   	 	excRate <- isvalidnum(excRate)
   	 	
   	 	cat("")
  		cat("***")
  		cat("Select Foreign Currency = ", excCurr)
		cat("Exchange Rate = ",excRate)

    } else if (choice == "5") {
   	 	excCurr <- recordexchangeindex()
		excRate <- recordexchangerate()
   	 	exeRates[excCurr] <- excRate
   	 	
   	 	cat("\n***\n")
  		cat("Select Foreign Currency = ", excCurr, "\n")
		cat("Exchange Rate = ", exeRates[excCurr], "\n")
    	
    } else if (choice == "6") {
   	
   	} else {
    	cat("Not a Valid Option")
    }
	active <- promptmainmenu()
    cat("\n")
}
