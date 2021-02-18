DEFEAT_DICT = {
    "scissors": "rock",
    "rock": "paper",
    "paper": "scissors"
}
choice = input()
print(f"Sorry, but the computer chose {DEFEAT_DICT[choice]}")
