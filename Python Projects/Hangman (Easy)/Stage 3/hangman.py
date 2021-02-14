from random import choice

print("H A N G M A N")
words = ["python", "java", "kotlin", "javascript"]
word = choice(words)
guess = input("Guess the word: ")
if guess == word:
    print("You survived!")
else:
    print("You lost!")
