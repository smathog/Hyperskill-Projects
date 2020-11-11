The stage specification can be found [here](https://hyperskill.org/projects/53/stages/291/implement) on Hyperskill. The text below is taken from this page.

# Description

The algorithm suggested for generating the secret code in the previous stage was honestly a “reinvention of the wheel”. Java already has the tools for generating random numbers! Research some common pseudo-random generation methods such as Math.random() and Random class. Choose the method you like and use it to rewrite the secret code generation.

Nothing else is supposed to change at this stage: the program asks for the length, generates a secret code, and then receives and grades the attempts until the code is guessed. Your task here is to rewrite the code generator without breaking the existing code.

# Objective

In this stage, rewrite the secret code generator using a suitable Java method.

# Notes

I fixed a typo in the method name for the random number generator, and updated it to use the Random class as opposed to using System.nanoTime(). 