ML_WATER_PER_CUP_COFFEE = 200
ML_MILK_PER_CUP_COFFEE = 50
G_BEANS_PER_CUP_COFFEE = 15

available_ml_water = 0
available_ml_milk = 0
available_g_beans = 0


def get_amount_supplies():
    global available_ml_water
    global available_ml_milk
    global available_g_beans

    print("Write how many ml of water the coffee machine has: ")
    available_ml_water = int(input())
    print("Write how many ml of milk the coffee machine has: ")
    available_ml_milk = int(input())
    print("Write how many grams of coffee beans the coffee machine has: ")
    available_g_beans = int(input())


def can_make_amount(num_cups):
    possible_cups = min([available_ml_water // ML_WATER_PER_CUP_COFFEE,
                         available_ml_milk // ML_MILK_PER_CUP_COFFEE,
                         available_g_beans // G_BEANS_PER_CUP_COFFEE])
    if possible_cups < num_cups:
        print(f"No, I can only make {possible_cups} of coffee")
    elif possible_cups == num_cups:
        print("Yes, I can make that amount of coffee")
    else:
        print(f"Yes, I can make that amount of coffee (and even {possible_cups - num_cups} more than that)")


get_amount_supplies()
print("Write how many cups of coffee you will need: ")
num = int(input())
can_make_amount(num)
