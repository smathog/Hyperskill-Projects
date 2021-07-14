symbols = list()
while len(symbols) < 100:
    print("Print a random string containing 0 or 1:")
    print()
    string = input()
    symbols.extend(i for i in string if i == "0" or i == "1")
    if len(symbols) < 100:
        print(f"Current data length is {len(symbols)}, {100 - len(symbols)} symbols left")
print()
print("Final data string: ")
print("".join(symbols))
print()
triple_dict = {str(i) + str(j) + str(k): {"0": 0, "1": 0} for i in range(2) for j in range(2) for k in range(2)}
for i in range(3, len(symbols)):
    triple = symbols[i - 3] + symbols[i - 2] + symbols[i - 1]
    triple_dict[triple][symbols[i]] += 1
for triple, nums in triple_dict.items():
    print(f"{triple}: {nums['0']},{nums['1']}")
