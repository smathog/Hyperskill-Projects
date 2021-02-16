available_ml_water = 400
available_ml_milk = 540
available_g_beans = 120
available_cups = 9
available_cash = 550


def buy():
    global available_cups
    print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
    choice = input()
    if choice == "back":
        return
    else:
        if available_cups == 0:
            print("Sorry, not enough cups!")
            return
        if choice == "1":
            coffee_created = create_espresso()
        elif choice == "2":
            coffee_created = create_latte()
        elif choice == "3":
            coffee_created = create_cappuccino()
        if coffee_created:
            available_cups -= 1
            print("I have enough resources, making you a coffee!")


def create_espresso():
    global available_ml_water
    global available_g_beans
    global available_cash
    if available_ml_water < 250:
        print("Sorry, not enough water!")
        return False
    elif available_g_beans < 16:
        print("Sorry, not enough coffee beans!")
        return False
    else:
        available_cash += 4
        available_ml_water -= 250
        available_g_beans -= 16
        return True


def create_latte():
    global available_ml_water
    global available_ml_milk
    global available_g_beans
    global available_cash
    if available_ml_water < 350:
        print("Sorry, not enough water!")
        return False
    elif available_ml_milk < 75:
        print("Sorry, not enough milk!")
        return False
    elif available_g_beans < 20:
        print("Sorry, not enough coffee beans!")
        return False
    else:
        available_cash += 7
        available_ml_water -= 350
        available_ml_milk -= 75
        available_g_beans -= 20
        return True


def create_cappuccino():
    global available_ml_water
    global available_ml_milk
    global available_g_beans
    global available_cash
    if available_ml_water < 200:
        print("Sorry, not enough water!")
        return False
    elif available_ml_milk < 100:
        print("Sorry, not enough milk!")
        return False
    elif available_g_beans < 12:
        print("Sorry, not enough coffee beans!")
        return False
    else:
        available_cash += 6
        available_ml_water -= 200
        available_ml_milk -= 100
        available_g_beans -= 12
        return True


def fill_supplies():
    global available_ml_water
    global available_ml_milk
    global available_g_beans
    global available_cups

    print("Write how many ml of water do you want to add:")
    available_ml_water += int(input())
    print("Write how many ml of milk do you want to add:")
    available_ml_milk += int(input())
    print("Write how many grams of coffee beans do you want to add:")
    available_g_beans += int(input())
    print("Write how many disposable cups of coffee do you want to add:")
    available_cups += int(input())


def take():
    global available_cash
    temp = available_cash
    available_cash = 0
    print(f"I gave you ${temp}")
    return temp


def print_status():
    global available_ml_water
    global available_ml_milk
    global available_g_beans
    global available_cups
    global available_cash
    print("The coffee machine has: ")
    print(f"{available_ml_water} of water")
    print(f"{available_ml_milk} of milk")
    print(f"{available_g_beans} of coffee beans")
    print(f"{available_cups} of disposable cups")
    if available_cash > 0:
        print(f"${available_cash} of money")
    else:
        print("0 of money")


def run():
    while True:
        print("Write action (buy, fill, take, remaining, exit):")
        choice = input()
        print()
        if choice == "buy":
            buy()
        elif choice == "fill":
            fill_supplies()
        elif choice == "take":
            take()
        elif choice == "remaining":
            print_status()
        elif choice == "exit":
            break
        print()


run()
