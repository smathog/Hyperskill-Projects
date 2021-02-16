ML_WATER_PER_CUP_COFFEE = 200
ML_MILK_PER_CUP_COFFEE = 50
G_BEANS_PER_CUP_COFFEE = 15

print("Write how many cups of coffee you will need: ")
num_cups = int(input())
print(f"For {num_cups} cups of coffee you will need: ")
print(f"{num_cups * ML_WATER_PER_CUP_COFFEE} ml of water")
print(f"{num_cups * ML_MILK_PER_CUP_COFFEE} ml of milk")
print(f"{num_cups * G_BEANS_PER_CUP_COFFEE} g of coffee beans")
