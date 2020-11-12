The stage specification can be found [here](https://hyperskill.org/projects/133/stages/712/implement) on Hyperskill. The text below is taken from this page.

# Description

The theatre is getting popular, and the customers started complaining that it's not practical that the program stops after buying one ticket. Let's add a menu that will allow them to buy tickets and display the current state of the seating arrangement when needed.

# Objectives

At the start, your program should read two positive integer numbers that represent the number of rows and seats in each row. Then, it should print a menu with the following three items:

* Show the seats should print the current seating arrangement. The empty seats should be marked with an S symbol, and taken seats are marked with a B symbol.
* Buy a ticket should read the seat coordinates from the input and print the ticket price like in the previous stage. After that, the chosen seat should be marked with a B when the item Show the seats is called.
* Exit should stop the program.

# Notes

At this point, I moved much of the functionality out of the Cinema class and into an auxiliary class CinemaRoom. I chose to represent the seats as a char[][] array; while perhaps not optimally space efficient (since only the purchased seats need to be remembered), this simplified the printing and purchasing functions somewhat and was easy to implement. 