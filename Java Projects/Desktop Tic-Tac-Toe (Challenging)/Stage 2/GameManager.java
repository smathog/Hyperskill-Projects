package tictactoe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class GameManager {
    private Player turn;
    private Status status;
    private final ArrayList<ArrayList<Optional<Player>>> grid;

    public GameManager() {
        this.turn = Player.First;
        this.status = Status.NS;
        grid = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            var row = new ArrayList<Optional<Player>>();
            for (int j = 0; j < 3; ++j) {
                row.add(Optional.empty());
            }
            grid.add(row);
        }
    }

    public Optional<Character> attemptMove(int row, int col) {
        // We only attempt a move if the status indicates we can and the grid cell
        // in question isn't used yet
        if ((status == Status.NS || status == Status.IP) && grid.get(row).get(col).isEmpty()) {
            grid.get(row).set(col, Optional.of(turn));
            var symbol = turn.symbol;
            switch (turn) {
                case First:
                    turn = Player.Second;
                    break;
                case Second:
                    turn = Player.First;
                    break;
            }
            assessStatus();
            return Optional.of(symbol);
        } else {
            return Optional.empty();
        }
    }


    /**
     * Determine the current status of the game and update status if necessary
     */
    private void assessStatus() {
        // See if the game isn't started
        if (grid.stream()
                .flatMap(Collection::stream)
                .allMatch(Optional::isEmpty) && status != Status.NS) {
            status = Status.NS;
        } else {
            // Game has been started, determine if it is won
            for (Player player : List.of(Player.First, Player.Second)) {
                // Check rows
                boolean win = grid.stream().anyMatch(row -> row.stream().allMatch(v -> Optional.of(player).equals(v)));
                // Check cols
                if (!win) {
                    win = IntStream.range(0, 3).anyMatch(col -> IntStream.range(0, 3)
                            .mapToObj(row -> grid.get(row).get(col))
                            .allMatch(v -> Optional.of(player).equals(v)));
                }
                // Check diagonals
                if (!win) {
                    win = (IntStream.range(0, 3)
                            .mapToObj(i -> grid.get(i).get(i))
                            .allMatch(v -> Optional.of(player).equals(v))
                            ||
                            IntStream.range(0, 3)
                                    .mapToObj(i -> grid.get(i).get(2 - i))
                                    .allMatch(v -> Optional.of(player).equals(v)));
                }
                if (win) {
                    switch (player) {
                        case First:
                            status = Status.XW;
                            return;
                        case Second:
                            status = Status.OW;
                            return;
                    }
                }
            }
            // Game has not been won, determine if draw (all spaces not none)
            if (grid.stream().flatMap(Collection::stream).allMatch(Optional::isPresent)) {
                status = Status.DR;
            } else {
                status = Status.IP;
            }
        }


    }

    public String getStatus() {
        return status.label;
    }

    private enum Player {
        First('X'),
        Second('O');

        private final char symbol;

        Player(char symbol) {
            this.symbol = symbol;
        }
    }

    private enum Status {
        NS("Game is not started"),
        IP("Game in progress"),
        XW("X wins"),
        OW("O wins"),
        DR("Draw");

        private final String label;

        Status(String label) {
            this.label = label;
        }
    }
}
