class CoffeeMachine:
    def __init__(self):
        self.available_ml_water = 400
        self.available_ml_milk = 540
        self.available_g_beans = 120
        self.available_cups = 9
        self.available_cash = 550
        self.mode = "Menu"

    def buy(self, choice):
        if self.available_cups == 0:
            print("Sorry, not enough cups!")
            return
        if choice == "1":
            coffee_created = self.create_espresso()
        elif choice == "2":
            coffee_created = self.create_latte()
        elif choice == "3":
            coffee_created = self.create_cappuccino()
        if coffee_created:
            self.available_cups -= 1
            print("I have enough resources, making you a coffee!")

    def create_espresso(self):
        if self.available_ml_water < 250:
            print("Sorry, not enough water!")
            return False
        elif self.available_g_beans < 16:
            print("Sorry, not enough coffee beans!")
            return False
        else:
            self.available_cash += 4
            self.available_ml_water -= 250
            self.available_g_beans -= 16
            return True

    def create_latte(self):
        if self.available_ml_water < 350:
            print("Sorry, not enough water!")
            return False
        elif self.available_ml_milk < 75:
            print("Sorry, not enough milk!")
            return False
        elif self.available_g_beans < 20:
            print("Sorry, not enough coffee beans!")
            return False
        else:
            self.available_cash += 7
            self.available_ml_water -= 350
            self.available_ml_milk -= 75
            self.available_g_beans -= 20
            return True

    def create_cappuccino(self):
        if self.available_ml_water < 200:
            print("Sorry, not enough water!")
            return False
        elif self.available_ml_milk < 100:
            print("Sorry, not enough milk!")
            return False
        elif self.available_g_beans < 12:
            print("Sorry, not enough coffee beans!")
            return False
        else:
            self.available_cash += 6
            self.available_ml_water -= 200
            self.available_ml_milk -= 100
            self.available_g_beans -= 12
            return True

    def take(self):
        temp = self.available_cash
        self.available_cash = 0
        print(f"I gave you ${temp}")
        return temp

    def print_status(self):
        print("The coffee machine has: ")
        print(f"{self.available_ml_water} of water")
        print(f"{self.available_ml_milk} of milk")
        print(f"{self.available_g_beans} of coffee beans")
        print(f"{self.available_cups} of disposable cups")
        if self.available_cash > 0:
            print(f"${self.available_cash} of money")
        else:
            print("0 of money")

    def run(self, arg):
        if self.mode == "Menu":
            print("Write action (buy, fill, take, remaining, exit):")
            self.mode = "Select"
        elif self.mode == "Select":
            if arg == "buy":
                print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
                self.mode = "Buy"
            elif arg == "fill":
                print("Write how many ml of water do you want to add:")
                self.mode = "Fill_Water"
            elif arg == "exit":
                self.mode = "Exit"
            else:
                self.mode = "Menu"
                if arg == "take":
                    self.take()
                elif arg == "remaining":
                    self.print_status()
        elif self.mode == "Buy":
            self.mode = "Menu"
            if arg != "back":
                self.buy(arg)
        elif self.mode == "Fill_Water":
            self.available_ml_water += int(arg)
            print("Write how many ml of milk do you want to add:")
            self.mode = "Fill_Milk"
        elif self.mode == "Fill_Milk":
            self.available_ml_milk += int(arg)
            print("Write how many grams of coffee beans do you want to add:")
            self.mode = "Fill_Beans"
        elif self.mode == "Fill_Beans":
            self.available_g_beans += int(arg)
            print("Write how many disposable cups of coffee do you want to add:")
            self.mode = "Fill_Cups"
        elif self.mode == "Fill_Cups":
            self.available_cups += int(arg)
            self.mode = "Menu"


coffee_machine = CoffeeMachine()
while coffee_machine.mode != "Exit":
    if coffee_machine.mode == "Menu":
        coffee_machine.run(None)
    else:
        coffee_machine.run(input())
