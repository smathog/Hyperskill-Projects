The stage specification can be found [here](https://hyperskill.org/projects/53/stages/292/implement) on Hyperskill. The text below is taken from this page.

# Description

Some players need a challenge, so let's make the secret code in the game harder to guess. Add support for more than 10 symbols by adding letters. Now, the secret code can contain numbers 0-9 and lowercase Latin characters a-z. The new maximum length for the code is 36. Note that the length of the secret word may not match the size of possible characters in the secret code, so you should make two separate inputs for the secret code length and for the size of possible characters.

Also, since a secret code is not a number anymore, the symbol 0 should be allowed as the first character in a secret code.

# Objective

In this step, your program should:

1. Ask for the length of the secret code.
2. Ask for the range of possible characters in the secret code.
3. Generate a secret code using numbers and characters. This time, you should also print the secret code using * characters and print which characters were used to generate the secret code.
4. Function as a fully playable game.

# Notes

Up to this point, I treated the code as an integer and used an ArrayList of Integer obects corresponding to its digits to grade a given guess against the code. However, the addition of letters makes this impossible; the logical next choice for storing the code and guesses would be to use a String object or perhaps a char array. I chose to use Strings, and rewrote my program with this in mind; this actually simplified things and probably should have been how I structured the program from the beginning.
