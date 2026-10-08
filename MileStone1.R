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
	input <- suppressWarnings(as.double(depAmt))

    if (is.na(input)){
    	input <- -1
    }
    	
    input
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
    	cat("Register Account Name\n")
  		accName <- readline("Account Name: ")
  		cat("***\n")
  		cat("Account Name = ", accName, "\n")
  		
    } else if (choice == "2") {
    	cat("Deposit Account\n")
    	cat("Account Name: ", accName, "\n")
    	cat("Current Balance: ", curBal, "\n")
    	cat("Currency: ", curr, "\n")
    	
    	depAmt <- readline("Deposit Amount: ")
    	
    	depAmt <- isvalidnum(depAmt)
    	
    	cat("\n***")
  		cat("Account Name = ",accName, "\n")
		cat("Deposit Amount = ", depAmt, "\n")

    } else if (choice == "3") {
    	cat("Withdraw Account\n")
    	cat("Account Name: ",accName, "\n")
    	cat("Current Balance: ", curBal, "\n")
    	cat("Currency: ", curr, "\n")
    	
    	witAmt <- readline("Withdraw Amount: ")
    	witAmt <- isvalidnum(witAmt)
    	
  		cat("\n***")
  		cat("Account Name = ", accName, "\n")
		cat("Withdraw Amount = ", witAmt, "\n")
    	
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
		
		cat("Record Exchange Rate\n\n")
    	cat("[1] Philippine Peso (PHP)\n")
    	cat("[2] United States Dollar (USD)\n")
    	cat("[3] Japanese Yen (JPY)\n")
    	cat("[4] British Pound Sterling (GBP)\n")
    	cat("[5] Euro (EUR)\n")
    	cat("[6] Chinese Yuan Renminni (CNY)\n")

   	 	excCurr <- readline("Select Foreign Currency: ")
   	 	excRate <- readline("Exchange Rate: ")
   	 	excRate <- isvalidnum(excRate)
   	 	excCurr = getcurrencynum(excCurr)
   	 	
   	 	exeRates[excCurr-1] <- excRate
   	 	
   	 	cat("\n***\n")
  		cat("Select Foreign Currency = ", excCurr, "\n")
		cat("Exchange Rate = ", exeRates[excCurr-1], "\n")
    	
    } else if (choice == "6") {
   	
   	} else if(choice == "7") {
    	active <- FALSE
    } else {
    	cat("Not a Valid Option")
    }
    cat("\n")
}
