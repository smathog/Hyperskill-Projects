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

    # Error handling
    if len(letter) != 1:
        print("You should input a single letter")
        return mistakes_left, current_progress
    if not letter.isalpha() or not letter.islower():
        print("Please enter a lowercase English letter")
        return mistakes_left, current_progress

    # Primary logic
    if letter not in word and letter not in guessed_letters:
        print("That letter doesn't appear in the word")
        guessed_letters.add(letter)
        mistakes_left -= 1
        return mistakes_left, current_progress
    elif letter in guessed_letters:
        print("You've already guessed this letter")
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
