The stage specification can be found [here](https://hyperskill.org/projects/133/stages/711/implement) on Hyperskill. The text below is taken from this page.

# Description

When choosing a ticket you are guided not only by your space preference but also by your finances. Let's implement the opportunity to check the ticket price and see the reserved seat.

# Objectives

Read two positive integer numbers that represent the number of rows and seats in each row and print the seating arrangement like in the first stage. Then, read two integer numbers from the input: a row number and a seat number in that row. These numbers represent the coordinates of the seat according to which the program should print the ticket price. The ticket price is determined by the same rules as the previous stage:

* If the total number of seats in the screen room is not more than 60, then the price of each ticket is 10 dollars.
* In a larger room, the tickets are 10 dollars for the front half of the rows and 8 dollars for the back half. Please note that the number of rows can be odd, for example, 9 rows. In this case, the first half is the first 4 rows, and the second half is the rest 5 rows.

After that, the program should print out all the seats in the screen room as shown in the example and mark the chosen seat by the B symbol. Finally, it should print the ticket price and stop. Note that in this project, the number of rows and seats won't be greater than 9.

# Notes

I implemented this stage by simply adding a function to calculate the cost for a given seat and modifying the printCinema function to take a single seat's location and print it via a 'B' character. It was tempting to simply overhaul the entire structure of the program by representing everything as independent objects but thus far the project requirements haven't really justified this; especially considering this is an easy project, going full-OO to represent a single seat in a grid seems like overkill.