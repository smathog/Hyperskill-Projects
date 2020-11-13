The stage specification can be found [here](https://hyperskill.org/projects/125/stages/666/implement) on Hyperskill. The text below is taken from this page.

# Description

It looks like everything is ready for full-scale battlefield maneuvers! This time, don't seize fire until all the ships are sunk. At the end of the game, your program should print a congratulatory message to the winner: You sank the last ship. You won. Congratulations!

# Objectives

To complete this step, you should add a check that all the ships were successfully sunk. The game is supposed to go on until all ships go down. The program should print an extra message You sank a ship! when all the cells of a particular ship have been hit. Take a look at the examples below!

# Notes

So, at this point it's probably going to become necessary to create a separate ship class. I need to be able to keep track of when a ship sinks, which either would involve repetitive checking of the board (a poor design choice) or delegating to a ship class which can keep track of each ship's "HP", as it were. Fortunately, this shouldn't take much redesigning; I can split off the ShipType enum and push it into the Ship class with relatively little modification to the main code of the Board class. Somewhat more complicated is the business of modifying the Board class so that a given instance of Ship is aware of the fact that it has been hit by a shot. To do this, I will convert the background char[][] grid that stores the hidden information about each ship's location into a grid of Object[][], where the spaces formerly corresponding to 'O' will instead contain references to a given ship instance; the rest can be replaced with Character wrapper objects that serve the same role as the previous char primitives. 

I ultimately converted the displayGrid array to Character[][] as well, so that the printGrid function could take a general Object[][] argument and work with both. 