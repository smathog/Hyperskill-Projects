The stage specification can be found [here](https://hyperskill.org/projects/53/stages/289/implement) on Hyperskill. The text below is taken from this page.

# Description
Using a predefined secret code doesn't make a fun game. Let's implement the ability to generate a pseudo-random secret number of a given length. If the length is greater than 10, print a warning message and do not generate a number.

We suggest you use the following algorithm to generate the numbers:

```java
long pseudoRandomNumber = System.nanoTime();
```

This code saves the nanoseconds elapsed since a certain time to the pseudoRandomNumber variable. We can assume that this is a random number. You can generate a secret code by iterating over the pseudoRandomNumber in the reverse order and adding unique digits. If the pseudoRandomNumber lacks the required amount of unique digits, call System.nanoTime() again and try to generate the secret code again until you get a satisfactory result.

# Objective
In this stage, your program should generate a pseudo-random number of a given length with unique digits and print it. If the length is greater than 10, the program should print a message containing the word Error. The secret code may contain any digits from 0 to 9 but only once. Secret code shouldn't start with a digit 0: for the first digit of the secret code, use digits from 1 to 9.

# Notes

The repeated usage of Math.abs is necessary due to the fact that System.nanoTime() can return a negative number, as well because casting from long to int can also return a negative number. 