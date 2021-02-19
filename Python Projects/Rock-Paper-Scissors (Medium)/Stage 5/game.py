import random

OPTIONS_LIST = ["rock", "paper", "scissors"]
DEFEAT_DICT = {
    "scissors": {"rock"},
    "rock": {"paper"},
    "paper": {"scissors"}
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
    if computer_choice in DEFEAT_DICT[user_choice]:
        print(f"Sorry, but the computer chose {computer_choice}")
        return 0
    elif user_choice in DEFEAT_DICT[computer_choice]:
        print(f"Well done. The computer chose {computer_choice} and failed")
        return 100
    else:
        print(f"There is a draw ({computer_choice})")
        return 50


def build_defeat_dict(options):
    defeat_dict = {}
    for i in range(len(options)):
        other_options = options[i + 1:] + options[:i]
        defeats_set = {i for i in other_options[:len(other_options) // 2]}
        elem = options[i]
        defeat_dict[elem] = defeats_set
    return defeat_dict


player_name = input("Enter your name: ")
print(f"Hello, {player_name}")
options_input = input()
print("Okay, let's start")
if options_input is not "":
    OPTIONS_LIST = options_input.split(",")
    DEFEAT_DICT = build_defeat_dict(OPTIONS_LIST)
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
