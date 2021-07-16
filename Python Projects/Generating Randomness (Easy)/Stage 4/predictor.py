from random import random
from random import randint


def get_frequency_map():
    # Get list of 0 and 1
    print("Please give the AI some dat to learn...")
    print("The current data length is 0, 100 symbols left")
    symbols = list()
    while len(symbols) < 100:
        print("Print a random string containing 0 or 1:")
        print()
        string = input()
        symbols.extend(i for i in string if i == "0" or i == "1")
        if len(symbols) < 100:
            print(f"The current data length is {len(symbols)}, {100 - len(symbols)} symbols left")
    print()
    print("Final data string: ")
    print("".join(symbols))
    print()
    # Construct map of triad followup frequency
    triple_dict = {str(i) + str(j) + str(k): {"0": 0, "1": 0} for i in range(2) for j in range(2) for k in range(2)}
    for i in range(3, len(symbols)):
        triple = symbols[i - 3] + symbols[i - 2] + symbols[i - 1]
        triple_dict[triple][symbols[i]] += 1
    return triple_dict


def predict_string(test_string, triple_dict):
    print("prediction:")
    guess_string = [str(randint(0, 1)) for _ in range(3)]
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
    return 2 * num_correct - len(test_string) + 3


freq_map = get_frequency_map()
print()
print("You have You have $1000. Every time the system successfully predicts your next press, you lose $1.")
print("Otherwise, you earn $1. Print \"enough\" to leave the game. Let's go!")
capital = 1000
while True:
    user_input = str()
    while not user_input:
        print()
        print("Print a random string containing 0 or 1:")
        user_input = input()
        if user_input == "enough":
            break
        else:
            user_input = "".join(i for i in user_input if i == "0" or i == "1")
        if user_input:
            capital -= predict_string(user_input, freq_map)
            print(f"Your capital is now ${capital}")
    if user_input == "enough":
        break
print()
print("Game over!")
