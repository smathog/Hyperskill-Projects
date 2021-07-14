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
