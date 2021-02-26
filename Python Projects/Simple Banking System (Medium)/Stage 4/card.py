import random


class CreditCard:
    def __init__(self, card_num=None, card_pin=None, card_balance=0):
        self.card_number = CreditCard.generate_card_num() if card_num is None else card_num
        self.pin = CreditCard.generate_card_pin() if card_pin is None else card_pin
        self.balance = card_balance

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

    @staticmethod
    def generate_card_num():
        num = [4, 0, 0, 0, 0, 0]
        for i in range(10):
            num.append(random.randint(0, 9))
        # Get luhn checksum to replace final digit
        num[-1] = CreditCard.luhn_checksum(num)
        return "".join(str(i) for i in num)

    @staticmethod
    def generate_card_pin():
        pin_list = []
        for i in range(4):
            pin_list.append(random.randint(0, 9))
        return "".join(str(i) for i in pin_list)
