package tictactoe;

import java.util.Scanner;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {
    private Board b;
    private final Scanner scanner;
    private Computer playerXComputer;
    private Computer playerOComputer;
    private static final HashSet<String> validPlayers;

    static {
        validPlayers = Stream.of("user", "easy", "medium")
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Game() {
        scanner = new Scanner(System.in);
    }

    private boolean setup() throws IllegalArgumentException {
        b = new Board(scanner, false);
        //Parse commands, see if valid
        System.out.print("Input command: ");
        String[] commands = scanner.nextLine().split(" ");
        if (commands.length == 1 && commands[0].equals("exit"))
            return false;
        if (commands.length != 3)
            throw new IllegalArgumentException("Bad parameters!");
        for (int i = 0; i < 3; ++i) {
            if (i == 0 && !commands[0].equals("start"))
                throw new IllegalArgumentException("Bad parameters!");
            else if (i > 0)
                if (!validPlayers.contains(commands[i]))
                    throw new IllegalArgumentException("Bad parameters!");
        }

        //Setup players
        if (commands[1].equals("user")) {
            playerXComputer = null;
        } else {
            if (commands[1].equals("easy"))
                playerXComputer = new EasyComputer(b);
            else
                playerXComputer = new MediumComputer(b);
        }
        if (commands[2].equals("user")) {
            playerOComputer = null;
        } else {
            if (commands[2].equals("easy"))
                playerOComputer = new EasyComputer(b);
            else
                playerOComputer = new MediumComputer(b);
        }

        //Init game
        return true;
    }

    public void play() {
        outerLoop:
        while (true) {
            //Setup Loop
            while (true) {
                try {
                    if (setup())
                        break;
                    else
                        break outerLoop;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            //Loop for single round of play
            b.printBoard();
            while (true) {
                try {
                    Computer activeComputer = null;
                    if (b.getxToMove() && playerXComputer != null) {
                        System.out.println("Making move level \"" + playerXComputer.level() + "\"");
                        activeComputer = playerXComputer;
                    }
                    else if (!b.getxToMove() && playerOComputer != null) {
                        System.out.println("Making move level \"" + playerOComputer.level() + "\"");
                        activeComputer = playerOComputer;
                    }
                    if (b.makeMove(activeComputer == null ? b.getCoordsFromHuman() : activeComputer.computeMove()))
                        break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println();
        }
    }
}
