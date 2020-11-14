The stage specification can be found [here](https://hyperskill.org/projects/125/stages/667/implement) on Hyperskill. The text below is taken from this page.

# Description

Here is a good way to show off your new skills: offer a friend to play a computer game that you wrote yourself! Of course, it is much more fun to play Battleship with someone else: the possibility of winning or losing adds a thrill to the game!

Both players add the ships to their fields one by one (no peeking!), and then start shelling each other until one of them succeeds. To make the game fair and prevent the players from peeping at each other's fields, after each move add the message "Press Enter and pass the move to another player", which will clear the screen.

# Objectives

To complete this stage and the entire project, add a PvP component to your game. Now the player will see not only the opponent's screen but their own as well. Place the opponent's screen at the top and your field at the bottom.

# Notes

For this stage, there is a need to implement a second player's perspective. Since all the work I've done up to this point has largely been contained within the Board class, this means a need to transfer certain functionality (like the play function) to another class so as to avoid needing an even larger reworking of the structure of the program. The approach I've chosen to use is to implement a Game class to wrap around two Board objects (nominally controlled by a given player each); fortunately most of the functionality necessary within the Board class was already broken off into independent functions, so all that really needs modification will be public display of the proper grid within the Board class as well as some access modifier to Board class functions. 

After completing this, I can say that most of the difficulties in this stage came from migrating the single-player implementation into a two-player implementation, since there were little details (like the printing of the two different grids within a Board) that changed slightly between the two versions. 