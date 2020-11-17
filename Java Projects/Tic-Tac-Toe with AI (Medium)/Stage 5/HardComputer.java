package tictactoe;

import java.util.ArrayList;

public class HardComputer extends MediumComputer {
    public HardComputer(Board b) {
        super(b);
    }

    @Override
    public int[] computeMove() {
        //Iterate through possible moves. If any causes evaluation to indicate win
        //Immediately make move. Otherwise, observe. If any can give draw but none win,
        //take draw; otherwise just make a random move (going to lose anyways at that point).
        int[] drawCoords = null;
        Board b = getBoard();
        Board.State moverWin = b.getxToMove() ? Board.State.xWins : Board.State.oWins;
        for (int x = 1; x <= 3; ++x)
            for (int y = 1; y <= 3; ++y)
                if (b.charAt(x, y) == ' ') {
                    Board.State result = evaluateMove(new Board(b), x, y);
                    if (result == moverWin)
                        return new int[]{x, y};
                    else if (result == Board.State.Draw)
                        drawCoords = new int[]{x, y};
                }
        //At this point, no winning move available, so take draw if possible, otherwise random
        return drawCoords == null ? super.computeMove() : drawCoords;
    }

    @Override
    public String level() {
        return "hard";
    }

    //Board references the board BEFORE moving x,y
    private Board.State evaluateMove(Board board, int x, int y) {
        //If move x,y is terminal, can immediately return result
        Board.State moveResult = board.makeMove(new int[]{x, y});
        if (moveResult != Board.State.StillPlaying)
            return moveResult;

        //Otherwise, need to check for other possible remaining moves
        //Go through the board, checking available remaining move trees
        //If one of them causes other play to win, we can immediately just return that
        //Otherwise evaluate to see if best other player can do is draw
        //Otherwise, must win, so just return that!
        Board.State otherWins = board.getxToMove() ? Board.State.xWins : Board.State.oWins;
        Board.State moverWins = board.getxToMove() ? Board.State.oWins : Board.State.xWins;
        boolean otherCanDraw = false;
        for (x = 1; x <= 3; ++x)
            for (y = 1; y <= 3; ++y)
                if (board.charAt(x, y) == ' ') {
                    Board.State result = evaluateMove(new Board(board), x, y);
                    if (result == otherWins)
                        return otherWins;
                    else if (result == Board.State.Draw)
                        otherCanDraw = true;
                }
        //If reached this point, no move tree leads to otherWinning, so either draw or mover wins
        return otherCanDraw ? Board.State.Draw : moverWins;
    }
}
