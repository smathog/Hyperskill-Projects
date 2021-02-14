from random import choice

print("H A N G M A N")
words = ["python", "java", "kotlin", "javascript"]
word = choice(words)
hint = word[0:3] + "-" * (len(word) - 3)
guess = input(f"Guess the word {hint}: ")
if guess == word:
    print("You survived!")
else:
    print("You lost!")
