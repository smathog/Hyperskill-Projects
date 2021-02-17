import math
import inspect


class LoanCalculator:
    def set_principal(self):
        print("Enter the loan principal: ")
        principal = float(input())
        self.principal = principal

    def set_monthly_payment(self):
        print("Enter the monthly payment: ")
        monthly_payment = float(input())
        self.monthly_payment = monthly_payment

    def set_num_payments(self):
        print("Enter the number of periods: ")
        num_payments = int(input())
        self.num_payments = num_payments

    def set_nominal_interest_rate(self):
        print("Enter the loan interest: ")
        interest_rate = float(input()) / 100
        self.nominal_interest_rate = interest_rate / 12

    def get_fraction(self):
        numerator = self.nominal_interest_rate * (1 + self.nominal_interest_rate) ** self.num_payments
        denominator = (1 + self.nominal_interest_rate) ** self.num_payments - 1
        return numerator / denominator

    def calculate_principal(self):
        return self.monthly_payment / self.get_fraction()

    def calculate_monthly_payment(self):
        return self.principal * self.get_fraction()

    def calculate_num_payments(self):
        x = self.monthly_payment / (self.monthly_payment - self.nominal_interest_rate * self.principal)
        return math.ceil(math.log(x, 1 + self.nominal_interest_rate))

    @staticmethod
    def months_to_years(num_months):
        if num_months < 12:
            print(f"It will take {num_months} months to repay this loan!")
        else:
            num_years = num_months // 12
            num_months %= 12
            if num_months == 0:
                print(f"If will take {num_years} years to repay this loan!")
            else:
                print(f"It will take {num_years} years and {num_months} months to repay this loan!")

    def calculate(self):
        print(inspect.cleandoc('''
        What do you want to calculate?
        type "n" for number of monthly payments,
        type "a" for annuity monthly payment amount,
        type "p" for loan principal:
        '''))
        choice = input()
        if choice == "n":
            self.set_principal()
            self.set_monthly_payment()
            self.set_nominal_interest_rate()
            months = self.calculate_num_payments()
            self.months_to_years(months)
        elif choice == "a":
            self.set_principal()
            self.set_num_payments()
            self.set_nominal_interest_rate()
            self.monthly_payment = math.ceil(self.calculate_monthly_payment())
            print(f"Your monthly payment = {self.monthly_payment}!")
        elif choice == "p":
            self.set_monthly_payment()
            self.set_num_payments()
            self.set_nominal_interest_rate()
            self.principal = math.ceil(self.calculate_principal())
            print(f"Your loan principal = {self.principal}!")


loan_calculator = LoanCalculator()
loan_calculator.calculate()
