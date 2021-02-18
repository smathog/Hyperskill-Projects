import math
import argparse


class AnnuityLoanCalculator:
    def __init__(self, args):
        self.monthly_payment = args.payment
        self.principal = args.principal
        self.num_payments = args.periods
        self.nominal_interest_rate = args.interest / (12 * 100)

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
                print(f"It will take {num_years} years to repay this loan!")
            else:
                print(f"It will take {num_years} years and {num_months} months to repay this loan!")

    def calculate(self):
        if self.num_payments is None:
            self.num_payments = self.calculate_num_payments()
            self.months_to_years(self.num_payments)
        elif self.monthly_payment is None:
            self.monthly_payment = math.ceil(self.calculate_monthly_payment())
            print(f"Your monthly payment = {self.monthly_payment}!")
        elif self.principal is None:
            self.principal = math.ceil(self.calculate_principal())
            print(f"Your loan principal = {self.principal}!")

        # Calculate overpayment
        total_paid = self.num_payments * self.monthly_payment
        print(f"Overpayment = {total_paid - self.principal}")


def calculate_differentiated_payments(args):
    sum = 0
    for i in range(1, args.periods + 1):
        fraction = (args.principal * (i - 1)) / args.periods
        d_m = math.ceil(args.principal / args.periods + (args.interest / 1200) * (args.principal - fraction))
        sum += d_m
        print(f"Month {i}: payment is {d_m}")
    print()
    print(f"Overpayment = {sum - args.principal}")


def validate_args(args):
    if not (args.type == "annuity" or args.type == "diff"):
        return False
    if args.type == "diff" and args.payment is not None:
        return False
    if args.interest is None:
        return False
    all_args = [args.type, args.payment, args.principal, args.periods, args.interest]
    num_not_none = sum([1 if i is not None else 0 for i in all_args])
    num_negative = sum([1 if i is not None and i < 0 else 0 for i in all_args[1:]])
    if num_not_none != 4 or num_negative != 0:
        return False
    return True


# Setting up parser
parser = argparse.ArgumentParser()
parser.add_argument("--type")
parser.add_argument("--payment", type=float)
parser.add_argument("--principal", type=float)
parser.add_argument("--periods", type=int)
parser.add_argument("--interest", type=float)

# Validate parser
args = parser.parse_args()
if validate_args(args):
    if args.type == "annuity":
        annuity_calc = AnnuityLoanCalculator(args)
        annuity_calc.calculate()
    else:
        calculate_differentiated_payments(args)
else:
    print("Incorrect parameters")
