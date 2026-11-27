today <- Sys.Date()
start_date <- floor_date(today, "month") %m-% months(1)
end_date <- ceiling_date(start_date, "month") - days(1)

days_in_month <- days_in_month(start_date)

account_opening_dates <- my_data %>%
  group_by(account_number) %>%
  summarise(
    opening_date = min(date),
    .groups = "drop"
  )

calendar_dates <- data.frame(
  date = seq(start_date, end_date, by = "day")
)


daily_balances <- my_data %>%
  arrange(account_name, account_number, date, time) %>%
  group_by(account_name, account_number, date) %>%
  summarise(
    end_of_day_balance = last(current_balance),
    .groups = "drop"
  )


account_list <- my_data %>%
  distinct(account_number, account_name) %>%
  distinct(account_number, .keep_all = TRUE)

opening_balances <- my_data %>%
  filter(date < start_date) %>%
  arrange(account_number, date, time) %>%
  group_by(account_number) %>%
  summarise(
    opening_balance = last(current_balance),
    .groups = "drop"
  )


adb_records <- account_list %>%
  crossing(calendar_dates) %>%
  left_join(
    daily_balances,
    by = c("account_number", "account_name", "date")
  ) %>%
  left_join(
    opening_balances,
    by = "account_number"
  ) %>%
  arrange(account_name, account_number, date) %>%
  group_by(account_name, account_number) %>%
  fill(end_of_day_balance, .direction = "down") %>%
  mutate(
    end_of_day_balance = coalesce(
      end_of_day_balance,
      opening_balance,
      0
    )
  ) %>%
  summarise(
    average_daily_balance = sum(end_of_day_balance)/ days_in_month,
    .groups = "drop"
  )


rp1 <- account_list %>%
  inner_join(
    account_opening_dates,
    by = "account_number"
  ) %>%
  inner_join(
    adb_records,
    by = c("account_number", "account_name")
  ) %>%
  inner_join(
    daily_balances,
    by = c("account_number", "account_name")
  ) %>%
  mutate(
    account_age = as.integer(today - opening_date),
    privilege_tier = case_when(
      average_daily_balance >= 1000000 ~ "Platinum",
      average_daily_balance >= 500000 ~ "Gold",
      TRUE ~ "Silver"
    )
  ) %>%
  filter(
    average_daily_balance > 100000
  ) %>%
  transmute(
    account_name,
    account_age,
    end_of_day_balance,
    average_daily_balance,
    privilege_tier
  ) %>%
  arrange(desc(average_daily_balance))

rp1 <- rp1 %>%
  group_by(account_name,account_age, average_daily_balance,privilege_tier ) %>%
  summarise(
    end_of_day_balance = last(
      end_of_day_balance
    ),
    .groups = "drop_last"
    
  )%>%
  transmute(
    account_name,
    account_age,
    end_of_day_balance,
    average_daily_balance,
    privilege_tier
  ) %>%
  arrange(desc(average_daily_balance))

rp2 <- my_data %>%
  arrange(account_name, date, desc(time)) %>%
  group_by(account_name) %>%
  summarise(
    current_balance = last(current_balance),
  )%>%
  mutate(
    expected_interest = (current_balance * ((1 + (0.05/365))^(30))) - current_balance
  )

rp2 <- rp2 %>%
  arrange(desc(expected_interest))

rp3 <- my_data %>%
  group_by(account_number, account_name, date)  %>%
  summarise(
    total_withdrawal = sum(amount[type == "withdrawal"],na.rm = TRUE),
    total_deposit = sum(amount[type == "deposit"],na.rm = TRUE),
    number_of_deposit = sum(type == "deposit", na.rm = TRUE),
    turnover_velocity = case_when(
      total_deposit > 0 ~ (total_withdrawal / total_deposit) * 100,
      TRUE ~ 0
    ),
    risk_level = case_when(
      total_withdrawal >= 500000 | (turnover_velocity >= 90 & turnover_velocity > 0 ) ~ "Suspicious",
      TRUE ~ "Normal"
    ),
    .groups = "drop_last"
  )

rp3 <- rp3 %>% 
  arrange(date, desc(risk_level))

rp3 <- rp3 %>% 
  ungroup() %>%
  select(-account_number)


rp4 <- my_data %>%
  arrange(date,time) %>%
  group_by(account_name) %>%
  summarise(
    current_balance = last(current_balance),
    opening_date = first(date)
  ) %>%
  mutate (
    account_age = as.integer(today - opening_date)
  ) %>%
  filter (
    current_balance > 500000
  ) %>% 
  arrange(desc(account_age), current_balance)

rp4 <- rp4 %>% head(10)

write.csv(rp1, "report1_banking_privilege_tiers.csv")
write.csv(rp2, "report2_interest_expense_projection.csv")
write.csv(rp3, "report3_suspicious_client_behavior.csv")
write.csv(rp4, "report4_top10_oldest_outstanding_accounts.csv")
write.csv(my_data, "bank_transactions.csv")

