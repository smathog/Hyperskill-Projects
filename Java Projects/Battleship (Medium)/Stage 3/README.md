The stage specification can be found [here](https://hyperskill.org/projects/125/stages/665/implement) on Hyperskill. The text below is taken from this page.

# Description

It seems a little odd to shoot your own ships. Let's imagine they are not ours! You can ask a friend to place the ships on the game field (or do it yourself, but it will be less exciting), and then the program will hide the ships under the fog of war. You just have to take a blind shot.

# Objectives

In this stage, you need to implement the "fog of war" feature in your game. First, place all the ships on the game field, and then hide them with the symbol ~. Take a shot like in the previous step, and after your attempt the program should print a message along with two versions of the field: one covered with the fog of war and another one open.

# Notes

At this point, there is now a need to somewhat separate the grid displayed during gameplay and the actual grid storing the locations of the various ships. Since at minimum the ship locations and the locations of shots need to be remembered, it makes some sense at this point to separate this into two different grids; one to store the locations, and another to display the visible grid to the player.

To do this is relatively straightforwards; within my existing design, I need only to convert the grid printing function into one taking a parameter specifying which of the two grids is being printed, and to modify the shot function so that the display grid is modified as well with each shot. This leaves a possible source of errors in place (what happens if somebody shoots at the same spot twice?) but I'll address that presumably in a future stage where the instructions are hopefully clear on how this ought to be handled.