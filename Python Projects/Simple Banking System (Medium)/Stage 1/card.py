import random


class CreditCard:
    def __init__(self):
        num = [4, 0, 0, 0, 0, 0]
        for i in range(10):
            num.append(random.randint(0, 9))
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
                break
