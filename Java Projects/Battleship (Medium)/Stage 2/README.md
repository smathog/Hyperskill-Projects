The stage specification can be found [here](https://hyperskill.org/projects/125/stages/664/implement) on Hyperskill. The text below is taken from this page.

# Description

The goal of this game is to sink all the ships of your opponent. Our fleet is not ready for a big battle yet, so let's practice shooting on our field. Place all your units on the battlefield and take a shot!

In this step, you need to develop a system of shooting with accompanying messages about hits and misses.

# Objectives

Take a shot at a prepared game field. You need to indicate the coordinates of the target, and the program should then display a message about a hit or a miss. If the shell misses the target and falls in the water, this cell should be marked with an M, and a successful strike is marked by an X. After this shot, the game should be stopped.

If the player managed to hit a ship, the game should display a message You hit a ship!; otherwise, the message is You missed!

# Notes

Immediately after finishing the previous stage, I realized that I was doing things in a somewhat inefficient manner; Java enums are far more powerful than the C++ enums I am used to! By just adding a String name and int length fields (as well as getter functions) to the ShipType enum, I was able to replace my large, somewhat unwieldy switch statements with much more concise alternatives. 

For example, in the placeShip function, this:

```java
        System.out.print("Enter the coordinates of the ");
        switch (st) {
            case Carrier:
                System.out.println("Aircraft Carrier (5 cells): ");
                break;
            case Battleship:
                System.out.println("Battleship (4 cells): ");
                break;
            case Submarine:
                System.out.println("Submarine (3 cells): ");
                break;
            case Cruiser:
                System.out.println("Cruiser (3 cells): ");
                break;
            case Destroyer:
                System.out.println("Destroyer (2 cells): ");
                break;
        }
```

was replaced by 

```java
        System.out.print("Enter the coordinates of the "
            + st.getName() + " (" + st.getlength() + " cells): ");
```

and in the function validatePlacement, I replaced 

```java
        switch (st) {
            case Carrier:
                if (length != 5) {
                    System.out.println("Error! Wrong length of the Aircraft Carrier! Try again: ");
                    return false;
                }
                break;
            case Battleship:
                if (length != 4) {
                    System.out.println("Error! Wrong length of the Battleship! Try again: ");
                    return false;
                }
                break;
            case Submarine:
                if (length != 3) {
                    System.out.println("Error! Wrong length of the Submarine! Try again: ");
                    return false;
                }
                break;
            case Cruiser:
                if (length != 3) {
                    System.out.println("Error! Wrong length of the Cruiser! Try again: ");
                    return false;
                }
                break;
            case Destroyer:
                if (length != 2) {
                    System.out.println("Error! Wrong lnegth of the Destroyer! Try again: ");
                    return false;
                }
                break;
        }
```

with 

```java
        if (length != st.getlength()) {
            System.out.println("Error! Wrong length of the " + 
                    st.getName() + "! Try again: ");
            return false;
        }
```

which is, in my view, a pretty clear improvement from a readability standpoint; and incidentally would have outright eliminated a bug I had to deal with in the previous stage where I accidentally forgot a break statement in one of the switch cases. 

Beyond this, the stage was straightforwards; a simple function to validate shot coordinates and another to mark the board were sufficient to implement the stage. 
