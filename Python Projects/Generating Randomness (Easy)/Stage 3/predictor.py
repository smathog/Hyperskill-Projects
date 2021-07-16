from random import random
from random import randint

# Get list of 0 and 1
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
# Construct map of triad followup frequency
triple_dict = {str(i) + str(j) + str(k): {"0": 0, "1": 0} for i in range(2) for j in range(2) for k in range(2)}
for i in range(3, len(symbols)):
    triple = symbols[i - 3] + symbols[i - 2] + symbols[i - 1]
    triple_dict[triple][symbols[i]] += 1
# Predict a test string
print()
print("Please enter a test string containing 0 or 1:")
print()
test_string = input()
print("prediction:")
guess_string = [str(randint(0, 1)) for i in range(3)]
for i in range(3, len(test_string)):
    triad = test_string[i - 3] + test_string[i - 2] + test_string[i - 1]
    if triple_dict[triad]["0"] > triple_dict[triad]["1"]:
        guess_string.append("0")
    elif triple_dict[triad]["0"] < triple_dict[triad]["1"]:
        guess_string.append("1")
    else:
        if random() <= 0.5:
            guess_string.append("0")
        else:
            guess_string.append("1")
print("".join(guess_string))
print()
num_correct = sum(1 for i, j in list(zip(test_string, guess_string))[3:] if i == j)
print(f"Computer guessed right {num_correct} out of {len(test_string) - 3} " +
      "symbols ({:.2f} %)".format(100 * num_correct / (len(test_string) - 3)))
