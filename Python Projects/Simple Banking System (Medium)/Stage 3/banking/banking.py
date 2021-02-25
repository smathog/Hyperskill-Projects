import sqlite3
from card import CreditCard


def create_card(card_dict, db_connect, cursor):
    card = CreditCard()
    print("Your card has been created")
    print("Your card number:")
    print(card.card_number)
    print("Your card PIN:")
    print(card.pin)
    card_dict[card.card_number] = card
    cursor.execute(f"INSERT INTO card (number, pin, balance)"
                   f"VALUES({card.card_number}, {card.pin}, {card.balance})")
    db_connect.commit()


def log_into_account(card_dict):
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


def menu():
    db_connect = sqlite3.connect("card.s3db")
    cursor = db_connect.cursor()
    # Check to make sure table exists, create it if it doesn't
    cursor.execute("CREATE TABLE IF NOT EXISTS "
                   "card(id INTEGER PRIMARY KEY, number TEXT, pin TEXT, balance INTEGER DEFAULT 0)")
    db_connect.commit()

    # Load dict from db
    card_dict = {}
    cursor.execute("SELECT * FROM card")
    for _, num, pin, balance in cursor.fetchall():
        temp_card = CreditCard()
        temp_card.card_number = num
        temp_card.pin = pin
        temp_card.balance = balance
        card_dict[temp_card.card_number] = temp_card

    # Main menu logic
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
            create_card(card_dict, db_connect, cursor)
        elif choice == 2:
            log_into_account(card_dict)
        if choice != 0:
            print()


menu()
