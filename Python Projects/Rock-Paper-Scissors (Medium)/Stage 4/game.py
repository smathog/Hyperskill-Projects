import random

OPTIONS_LIST = ["rock", "paper", "scissors"]
DEFEAT_DICT = {
    "scissors": "rock",
    "rock": "paper",
    "paper": "scissors"
}
PLAYER_SCORES = dict()


def load_player_scores():
    infile = open("rating.txt", "r")
    for line in infile:
        data = line.split()
        player = data[0]
        rating = int(data[1])
        PLAYER_SCORES[player] = rating


def play_round(user_choice):
    computer_choice = random.choice(OPTIONS_LIST)
    if DEFEAT_DICT[user_choice] == computer_choice:
        print(f"Sorry, but the computer chose {computer_choice}")
        return 0
    elif DEFEAT_DICT[computer_choice] == user_choice:
        print(f"Well done. The computer chose {computer_choice} and failed")
        return 100
    else:
        print(f"There is a draw ({computer_choice})")
        return 50


player_name = input("Enter your name: ")
print(f"Hello, {player_name}")
load_player_scores()
if player_name in PLAYER_SCORES:
    player_score = PLAYER_SCORES[player_name]
else:
    player_score = 0
while True:
    user_choice = input()
    if user_choice == "!exit":
        print("Bye!")
        break
    elif user_choice == "!rating":
        print(f"Your rating: {player_score}")
    elif user_choice not in OPTIONS_LIST:
        print("Invalid input")
        continue
    else:
        player_score += play_round(user_choice)
