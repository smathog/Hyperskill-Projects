import random


class CreditCard:
    def __init__(self):
        num = [4, 0, 0, 0, 0, 0]
        for i in range(10):
            num.append(random.randint(0, 9))
        # Get luhn checksum to replace final digit
        num[-1] = CreditCard.luhn_checksum(num)
        self.card_number = "".join(str(i) for i in num)
        pin_list = []
        for i in range(4):
            pin_list.append(random.randint(0, 9))
        self.pin = "".join(str(i) for i in pin_list)
        self.balance = 0

    def card_menu(self):
        while True:
            print("1. Balance")
            print("2. Log out")
            print("0. Exit")
            choice = int(input())
            if choice == 0:
                exit()
            elif choice == 1:
                print()
                print(f"Balance: {self.balance}")
            elif choice == 2:
                print()
                print("You have successfully logged out!")

    @staticmethod
    def luhn_checksum(nums):
        """Returns the matching terminal digit of a luhn checksum"""
        # Step 1: Drop the terminal digit
        calc_list = [int(num) for i, num in enumerate(nums) if i != len(nums) - 1]
        # Step 2: Multiply even digits (from 0 index) by 2
        for i in range(len(calc_list)):
            if i % 2 == 0:
                calc_list[i] *= 2
        # Step 3: Subtract 9 from numbers over 9
        for i in range(len(calc_list)):
            if calc_list[i] > 9:
                calc_list[i] -= 9
        total = sum(calc_list)
        return (10 - (total % 10)) % 10
