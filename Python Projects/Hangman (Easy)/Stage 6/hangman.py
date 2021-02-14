from random import choice


def hangman():
    print("H A N G M A N")
    words = ["python", "java", "kotlin", "javascript"]
    word = choice(words)
    current_progress = "-" * len(word)
    guessed_letters = set()
    mistakes_left = 8
    while mistakes_left > 0:
        mistakes_left, current_progress = hangman_round(word, current_progress, mistakes_left, guessed_letters)
        if current_progress == word:
            break
    if mistakes_left == 0:
        print("You lost!")
    else:
        print()
        print(word)
        print("You guessed the word!")
        print("You survived!")


def hangman_round(word, current_progress, mistakes_left, guessed_letters):
    print()
    print(current_progress)
    letter = input("Input a letter: ")
    if letter not in word:
        print("That letter doesn't appear in the word")
        guessed_letters.add(letter)
        mistakes_left -= 1
        return mistakes_left, current_progress
    elif letter in guessed_letters:
        print("No improvements")
        mistakes_left -= 1
        return mistakes_left, current_progress
    else:
        guessed_letters.add(letter)
        temp = ""
        for c in word:
            if c == letter or c in current_progress:
                temp += c
            else:
                temp += "-"
        return mistakes_left, temp


hangman()
