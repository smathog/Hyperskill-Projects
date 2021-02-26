import sqlite3
from card import CreditCard


def create_card(db_connect, cursor):
    card = CreditCard()
    print("Your card has been created")
    print("Your card number:")
    print(card.card_number)
    print("Your card PIN:")
    print(card.pin)
    cursor.execute("INSERT INTO card (number, pin, balance) "
                   f"VALUES({card.card_number}, {card.pin}, {card.balance});")
    db_connect.commit()


def log_into_account(db_connect, cursor):
    print("Enter your card number:")
    card_num = input()
    print("Enter your PIN:")
    pin = input()
    print()
    cursor.execute("SELECT number, pin, balance "
                   "FROM card "
                   f"WHERE number = {card_num}")
    result = cursor.fetchone()
    if result is None or result[1] != pin:
        print("Wrong card number or PIN!")
    else:
        print("You have successfully logged in!")
        print()
        card_menu(CreditCard(card_num, pin, result[2]), db_connect, cursor)


def card_menu(card, db_connect, cursor):
    while True:
        print("1. Balance")
        print("2. Add income")
        print("3. Do transfer")
        print("4. Close account")
        print("5. Log out")
        print("0. Exit")
        choice = int(input())
        print()
        if choice == 0:
            exit()
        elif choice == 1:
            print(f"Balance: {card.balance}")
        elif choice == 2:
            print("Enter income:")
            income = int(input())
            card.balance += income
            cursor.execute("UPDATE card "
                           f"SET balance = {card.balance} "
                           f"WHERE number = {card.card_number};")
            db_connect.commit()
            print("Income was added!")
        elif choice == 3:
            print("Enter card number:")
            card_num = input()
            if CreditCard.luhn_checksum(card_num) != int(card_num[-1]):
                print("You probably made a mistake in the card number. Please try again!")
            elif card_num == card.card_number:
                print("You can't transfer money to the same account!")
            else:
                cursor.execute("SELECT balance "
                               "FROM card "
                               f"WHERE number = {card_num}")
                result = cursor.fetchone()
                if result is None:
                    print("Such a card does not exist.")
                else:
                    print("Enter how much money you want to transfer:")
                    amount = int(input())
                    if amount > card.balance:
                        print("Not enough money!")
                    else:
                        card.balance -= amount
                        amount += result[0]
                        cursor.execute("UPDATE card "
                                       f"SET balance = {card.balance} "
                                       f"WHERE number = {card.card_number};")
                        cursor.execute("UPDATE card "
                                       f"SET balance = {amount} "
                                       f"WHERE number = {card_num}")
                        db_connect.commit()
        elif choice == 4:
            cursor.execute("DELETE FROM card "
                           f"WHERE number = {card.card_number};")
            db_connect.commit()
            print("The account has been closed!")
            break
        elif choice == 5:
            print("You have successfully logged out!")
            break
        print()


def menu():
    db_connect = sqlite3.connect("card.s3db")
    cursor = db_connect.cursor()
    # Check to make sure table exists, create it if it doesn't
    cursor.execute("CREATE TABLE IF NOT EXISTS "
                   "card(id INTEGER PRIMARY KEY, number TEXT, pin TEXT, balance INTEGER DEFAULT 0);")
    db_connect.commit()

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
            create_card(db_connect, cursor)
        elif choice == 2:
            log_into_account(db_connect, cursor)
        if choice != 0:
            print()


menu()
