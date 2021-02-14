from random import choice


def hangman():
    print("H A N G M A N")
    words = ["python", "java", "kotlin", "javascript"]
    word = choice(words)
    current_progress = "-" * len(word)
    for i in range(8):
        current_progress = hangman_round(word, current_progress)
    print()
    print("Thanks for playing!")
    print("We'll see how well you did in the next stage")


def hangman_round(word, current_progress):
    print()
    print(current_progress)
    letter = input("Input a letter: ")
    if letter not in word:
        print("That letter doesn't appear in the word")
        return current_progress
    else:
        temp = ""
        for c in word:
            if c == letter or c in current_progress:
                temp += c
            else:
                temp += "-"
        return temp


hangman()