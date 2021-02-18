import random

OPTIONS_LIST = ["rock", "paper", "scissors"]
DEFEAT_DICT = {
    "scissors": "rock",
    "rock": "paper",
    "paper": "scissors"
}
while True:
    user_choice = input()
    if user_choice == "!exit":
        print("Bye!")
        break
    elif user_choice not in OPTIONS_LIST:
        print("Invalid input")
        continue
    else:
        computer_choice = random.choice(OPTIONS_LIST)
        if DEFEAT_DICT[user_choice] == computer_choice:
            print(f"Sorry, but the computer chose {computer_choice}")
        elif DEFEAT_DICT[computer_choice] == user_choice:
            print(f"Well done. The computer chose {computer_choice} and failed")
        else:
            print(f"There is a draw ({computer_choice})")
