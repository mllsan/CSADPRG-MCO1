#This is the version 3.0 of R file of MCO2 for Group 5.
#@author Iris Salvador
#@version 3.0

#loads the necessary libraries 
library(dplyr)
library(tidyr)
library(lubridate)
library(hms)
library(glue)


#------------------------------------------
#Data Related Functions
#------------------------------------------
#loads in the data from bank_transactions.csv
load_data <- function(){
  
  if (file.exists("bank_transactions.csv")) {
    my_data <- read.csv("bank_transactions.csv")
    #my_data <- validate_data(my_data)
  } else {
    my_data <- data.frame(
      account_name = character(),
      account_number = integer(),
      date = as.Date(character()),
      time = character(),
      currency = character(),
      amount = numeric(),
      type = character(),
      current_balance = numeric()
    )
  }
  
  my_data
  
}

validate_data <- function(my_data){
  
  my_data <- unique(my_data)
  
  my_data <- my_data %>%
    filter(currency == "PHP")
  
  my_data <- my_data %>%
    mutate(date = dmy(date))
  
  valid_data <- my_data %>%
    mutate(
      account_number = trimws(as.character(account_number)),
      account_name = trimws(account_name),
      date = as.Date(date, format = "%d/%m/%Y"),
      time = time,
      amount = as.numeric(amount),
      current_balance = as.numeric(current_balance),
      type = trimws(type)
    ) %>%
    filter(
      !is.na(date),
      !is.na(time),
      !is.na(amount),
      !is.na(current_balance),
      currency == "PHP",
      grepl("^[0-9]{10}$", account_number),
      is.finite(amount),
      amount > 0,
      is.finite(current_balance),
      current_balance >= 0,
      type %in% c("deposit", "withdrawal")
    )
  valid_data
  
}

make_row <-function(name,number,amt,type_of_report,new_balance){
  new_row <- data.frame( account_name = name,
                         account_number = number,
                         date = Sys.Date(),
                         time = format(Sys.time(), "%H:%M:%S"),
                         currency = "PHP",
                         amount = amt,
                         type = type_of_report,
                         current_balance = new_balance)
  new_row
}

isvalidnum <- function(input){
  input <- suppressWarnings(as.double(input))
  
  if (is.na(input) | input < 0){
    input <- 0
  }
  
  input
}



#------------------------------------------
#Format and Print functions
#------------------------------------------
#returns a numerical value num in the string format #.## 
formatdecimal <- function(num){
  formatted <- format(round(num, 2), nsmall = 2)
  formatted
}

#prints the Main Menu Prompt
mainmenu <- function(){
  cat("\nSelect Transaction:\n[1] Register Account Name\n[2] Deposit Amount\n[3] Withdraw Amount\n[4] Currency Exchange\n[5] Record Exchange Rates\n[6] Show Interest Amount\n[7] Generate Reports\n\n")
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

#------------------------------------------
#Bank Transactions functions
#------------------------------------------
#function for adding an account to the system
registeracc <-function(){
  
  cat("Register Account Name\n")
  do_while <- TRUE
  accName <- " "
  
  #Checks if the inputed Account Name is valid or if it is already taken, repeats until the valid
  while (do_while){
    #User Input Account Name
    input <- readline("Account Name: ")
    
    if((input %in% accounts$account_name | !grepl(" ", input))){
      cat("Invalid account name. Please follow the format 'lastname, firstname' with letters only.\n\n")
      cat("Register Account Name\n")
    }
    else{
      do_while <- FALSE
    }
  }
  
  #asks for deposit amount
  do_while <- TRUE
  
  #checks if the deposit amount is valid, repeats until the valid
  while(do_while){
    depAmt <- readline("Initial Deposit: ")
    depAmt <- isvalidnum(depAmt)
    
    if(depAmt < 5000){
      cat("Invalid amount. Please enter a valid positive number.\n\n")
    }else {
      do_while <- FALSE
    }
  }
  
  accNum <- latest_acc_num + count
  cat("\n***\nAccount Name = ", input, "\n")
  cat("\n***\nAccount Number = ",sprintf("%010d", accNum) , "\n")
  cat("\n***\nInitial Deposit = ", depAmt, "\n")
  
  #Initializes the account 
  bal <- depAmt
  currency <- "PHP"
  
  new_row <- make_row(input,accNum, depAmt, "deposit", bal)
  new_row
}

#function for doing a deposit on an account
deposit <- function(acc){
  
  cat("Current Balance: ",  unlist(acc['current_balance']), "\n")
  cat("Currency: ", "PHP", "\n\n")
  
  do_while <- TRUE
  #checks if the deposit amount is valid, repeats until the valid
  while(do_while){
    #asks for deposit amount
    depAmt <- readline("Deposit Amount: ")
    depAmt <- isvalidnum(depAmt)
    
    if(depAmt <= 0){
      cat("Invalid amount. Please enter a valid positive number.\n\n")
    } else {
      do_while <- FALSE
    }
  }
  
  cat("\n***\n")
  cat("Account Name = ", unlist(acc['account_name']), "\n")
  cat("Deposit Amount = ", formatdecimal(depAmt), "\n")
  
  #updates balance
  acc['current_balance'] <- unlist(acc['current_balance']) + depAmt
  
  cat("Updated Balance = ", formatdecimal(unlist(acc['current_balance'])), "\n")
  
  new_row <- make_row(acc['account_name'], latest_acc_num + count, depAmt, "deposit",acc['current_balance'])
  new_row
}

withdraw <- function (acc){
  
  cat("Current Balance: ", unlist(acc[2]), "\n")
  cat("Currency: ", unlist(acc[3]), "\n\n")
  
  
  do_while <- TRUE
  #checks if the deposit amount is valid, repeats until the valid
  while(do_while){
    #asks for deposit amount
    witAmt <- readline("Withdraw Amount: ")
    witAmt <- isvalidnum(witAmt)
    
    if(witAmt <= 0 | witAmt > acc[2]){
      cat("Invalid amount. Please enter a valid positive number.\n\n")
    } else {
      do_while <- FALSE
    }
  }
  
  cat("\n***\n")
  cat("Account Name = ", unlist(acc['account_name']), "\n")
  cat("Withdraw Amount = ", formatdecimal(witAmt), "\n")
  
  #updates balance
  acc['current_balance'] <- unlist(acc['current_balance']) - witAmt
  
  cat("Updated Balance = ", formatdecimal(unlist(acc['current_balance'])), "\n")
  
  new_row <- make_row(acc['account_name'], latest_acc_num + count, witAmt, "withdrawal",acc['current_balance'])
  new_row
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


my_data <- load_data()
my_data <- validate_data(my_data)

active <- TRUE
choice <- 0
exeRates = c(1.00, 62.00, 0.40, 84.00, 72.00, 9.00)
count = 0
accounts <- my_data %>% 
  arrange(date, time) %>% 
  group_by(account_name) %>%
  summarise (
    account_number = last(account_number),
    current_balance = last(current_balance)
  )

if (nrow(my_data) > 0) {
  latest_acc_num <- max(as.numeric(my_data$account_number), na.rm = TRUE)
} else {
  latest_acc_num <- 0
}

while (active){
  
  mainmenu()
  
  choice = readline("Choice: ")
  
  choiceselection(choice)
  
  if(choice == "1"){
    do_while <- TRUE
    while (do_while) {
      count <- count + 1
      new_row <- registeracc()
      my_data <- rbind(my_data, new_row)
      
      account <- data.frame(
        account_name = as.character(new_row$account_name),
        account_number = as.character(new_row$account_number),
        current_balance = as.numeric(new_row$current_balance)
      )
      
      accounts <- rbind(accounts, account)
      
      choice = readline("Would you like to make another account? (Y/N): ")
      if (choice == "N"){
        do_while <- FALSE
      }
    }
  } else if (choice == "2") {
    do_while <- TRUE
    while (do_while) {
      cat("Deposit Account\n")
      accName <- readline("Account Name: ")
      
      if(accName %in%  accounts$account_name){
        account <- accounts %>% 
          filter(account_name == accName)
        
        new_row <- deposit(account)
        my_data <- rbind(my_data, new_row)
        accounts$current_balance[accounts$account_name == accName] <- new_row['current_balance'] 
      } else {
        cat("Account not found. Please input a registered account.\n")
      }
      
      choice = readline("Would you like to make another account? (Y/N): ")
      if (choice == "N"){
        do_while <- FALSE
      }
    }
  } else if (choice == "3") {
    do_while <- TRUE
    while (do_while) {
      cat("Withdraw Account\n")
      accName <- readline("Account Name: ")
      
      if(accName %in%  accounts$account_name){
        account <- accounts %>% 
          filter(account_name == accName)
        
        new_row <- withdraw(account)
        my_data <- rbind(my_data, new_row)
        accounts$current_balance[accounts$account_name == accName] <- new_row['current_balance'] 
      } else {
        cat("Account not found. Please input a registered account.\n")
      }
      choice = readline("Would you like to make another account? (Y/N): ")
      if (choice == "N"){
        do_while <- FALSE
      }
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
    account <- accounts %>% 
      filter(account_name == accName)
    
    cat("Account Name: ", unlist(acc['account_name']), "\n")
    cat("Current Balance: ", unlist(acc['current_balance']), "\n")
    cat("Currency: ", "PHP", "\n")
    
    rate <- readline("Interest Rate: ")
    day <- readline("Total Number of Days: ")
    showinterestamount(rate, day, unlist(acc['current_balance']))
  } else if(choice == "7"){
    
    do_while_1 <- TRUE
    while (do_while_1){
      
      do_while_2 <- TRUE
      while (do_while_2) {
        cat("Generate Reports\n[1] Load the file\n[2] Generate Reports\n")
        choice = readline("Choice: ")

        if (choice == "1" | choice == "2"  ){
          do_while_2 <- FALSE
        }
        else {
          cat("\n\nInvalid Choice.\n\n")
        }
      }
      
      if (choice == "1"){
        cat("\nProcessing dataset... (", nrow(my_data)," rows loaded, ", nrow(validate_data(my_data)) ,"filtered for PHP currency)\n\n")
      }
      else {
        source("MCO2_Report.R")
        
        cat("\n\nReport 1: Banking Privilege Tiers")
        cat("\nAccountName | AccountAge | CurrentBalance | AverageDailyBalance | PrivilegeTier\n")
        rp1 <- rp1 %>% head(2)
        output <- glue_data(rp1, "{account_name} | {account_age} | {end_of_day_balance} | {average_daily_balance} | {privilege_tier}")
        cat(output, sep = "\n")
        cat("\n(Full table exported to report1_banking_privilege_tiers.csv)\n\n")
        
        cat("Report 2: Interest Expense Projection")
        cat("\nAccountName | CurrentBalance | ExpectedInterest\n")
        rp2 <- rp2 %>% head(2)
        output <- glue_data(rp2, "{account_name} | {current_balance} | {expected_interest} ")
        cat(output, sep = "\n")
        cat("\n(Full table exported to report2_interest_expense_projection.csv)\n\n")
        
        cat("Report 3: Suspicious Client Behavior")
        cat("\nAccountName | Date | TotalWithdrawalAmounts | NumberOfDeposits | TotalDepositAmounts | TurnoverVelocity (%) | RiskFlagLevel\n")
        rp3 <- rp3 %>% head(2)
        output <- glue_data(rp3, " {account_name} | {date} | {total_withdrawal} | {total_deposit} | {number_of_deposit} | {turnover_velocity} | {risk_level}")
        cat(output, sep = "\n")
        cat("\n(Full table exported to report3_suspicious_client_behavior.csv)\n\n")
        
        cat("Report 4: Top 10 Oldest Outstanding Accounts")
        cat("\nAccountName | CurrentBalance | OpeningDate | AccountAge\n")
        rp4 <- rp4 %>% head(2)
        output <- glue_data(rp4, "{account_name} | {current_balance} | {opening_date} | {account_age}")
        cat(output, sep = "\n")
        cat("\n(Full table exported to report4_top10_oldest_outstanding_accounts.csv)\n\n")
        do_while_1 <- FALSE
        do_while_2 <- FALSE
      }
    }  
  } else {
    cat("\n\nInvalid Choice.\n\n")
    cat("")
  }
  
  
  cat("\n")
  active <- promptmainmenu()
  
  if (!active){
    cat("\nExiting...\n")
  }
  
  cat("\n")
}
