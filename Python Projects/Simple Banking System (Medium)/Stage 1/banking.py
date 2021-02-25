from card import CreditCard


def menu():
    card_dict = {}
    while True:
        print("1. Create an account")
        print("2. Log into account")
        print("0. Exit")
        choice = int(input())
        print()
        if choice == 0:
            print("Bye!")
            break
        elif choice == 1:
            card = CreditCard()
            print("Your card has been created")
            print("Your card number:")
            print(card.card_number)
            print("Your card PIN:")
            print(card.pin)
            card_dict[card.card_number] = card
        elif choice == 2:
            print("Enter your card number:")
            card_num = input()
            print("Enter your PIN:")
            pin = input()
            print()
            if card_num not in card_dict or card_dict[card_num].pin != pin:
                print("Wrong card number or PIN!")
            else:
                print("You have successfully logged in!")
                print()
                card_dict[card_num].card_menu()
        if choice != 0:
            print()


menu()
