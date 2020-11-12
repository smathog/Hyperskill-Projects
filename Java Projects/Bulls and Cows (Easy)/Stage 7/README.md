The stage specification can be found [here](https://hyperskill.org/projects/53/stages/293/implement) on Hyperskill. The text below is taken from this page.

# Description

There are a lot of error possibilities. What if someone enters an answer of the wrong length? Or the number of possible symbols is less than the length of the code? What if the answer contains invalid symbols? The game may crash before the secret number was guessed!

Let's handle errors like this. At this point, you can implement this without the try catch construction. Use the following rule of thumb: if you can avoid the exception-based logic, avoid it! If you use exceptions in normal situations, how would you deal with unusual (exceptional) situations? Now it may not seem that important, but when you need to find errors in more complex programs, this makes a difference.

# Objective

In this stage, your program should:

1. Handle incorrect input.
2. Print an error message that contains the word error. After that, don't ask for the numbers again, just finish the program.

# Notes

For this stage, the only place I chose to use exception-based logic was for detecting invalid number input for the length or number of symbols of the code String, since the Integer.parseInt() method throws a NumberFormatException. For the other cases, simple checks were enough (although I did introduce a separate checking function for the answers to detect if an invalid symbol was prsent; although this would have been also doable with Regex and the String contains method.)