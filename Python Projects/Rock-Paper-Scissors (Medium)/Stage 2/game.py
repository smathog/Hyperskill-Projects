import random

OPTIONS_LIST = ["rock", "paper", "scissors"]
DEFEAT_DICT = {
    "scissors": "rock",
    "rock": "paper",
    "paper": "scissors"
}
user_choice = input()
computer_choice = random.choice(OPTIONS_LIST)
if DEFEAT_DICT[user_choice] == computer_choice:
    print(f"Sorry, but the computer chose {computer_choice}")
elif DEFEAT_DICT[computer_choice] == user_choice:
    print(f"Well done. The computer chose {computer_choice} and failed")
else:
    print(f"There is a draw ({computer_choice})")
