import math

print("Enter the loan principal: ")
principal = int(input())
print("What do you want to calculate?")
print('type "m" - for number of monthly payments,')
print('type "p" - for the monthly payment:')
choice = input()
if choice == "m":
    print("Enter the monthly payment: ")
    monthly_payment = int(input())
    months = principal // monthly_payment + (1 if principal % monthly_payment != 0 else 0)
    print()
    if months == 1:
        print(f"It will take {months} month to repay the loan")
    else:
        print(f"It will take {months} months to repay the loan")
elif choice == "p":
    print("Enter the number of months: ")
    num_months = int(input())
    print()
    if principal % num_months != 0:
        payment = int(math.ceil(principal / num_months))
        last_payment = principal - (payment * (num_months - 1))
        print(f"Your monthly payment = {payment} and the last payment = {last_payment}.")
    else:
        payment = principal // num_months
        print(f"Your monthly payment = {payment}")
