The stage specification can be found [here](https://hyperskill.org/projects/53/stages/290/implement) on Hyperskill. The text below is taken from this page.

# Description

In this stage, you should combine all the previous parts into a simple playable version of the "Bulls and Cows" game. First, prompt the player to input the length of the secret code. The length will determine the difficulty level for the current game session. The program should generate a secret code of the given length. Remember that it should consist of unique numbers.

Then, the game starts and the program prompts the player to guess the code. When the player inputs a number, the program should grade it in bulls and cows. The game goes on until the code is guessed, that is, until the number of bulls is equal to the number of digits in the code. When the game ends, the program should finish its execution.

# Objective
In this stage, your program should:

1. Ask for the length of the secret code and then generate the code.
2. Wait for the user input.
3. Grade the guessing attempt in bulls and cows.
4. If the secret code has been guessed, the program stops; otherwise, return to the second step.

# Notes

For this stage, it was necessary to rewrite the functions from the previous stage so as to pass the information regarding the length of the number for a given iteration of the game; before this I had hardcoded the assumption that each number would be 4 digits long. In addition, the internal logic for the grader function needed to be fixed; I overlooked a bug on a previous stage involving guesses with duplicate digits. 