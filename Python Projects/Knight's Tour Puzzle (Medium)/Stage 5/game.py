class Board:
    POTENTIAL_KNIGHT_SHIFTS = [(2, 1), (-2, 1), (2, -1), (-2, -1),
                               (1, 2), (-1, 2), (1, -2), (-1, -2)]

    @staticmethod
    def create():
        try:
            line = input("Enter your board dimensions: ")
            line = line.split()
            if len(line) != 2:
                raise RuntimeError
            dims = (int(line[1]), int(line[0]))
            for num in dims:
                if num <= 0:
                    raise RuntimeError
            return Board(*dims)
        except (RuntimeError, ValueError):
            print("Invalid dimensions!")
            return None

    def __init__(self, n_rows, n_cols):
        self.grid = [[False for _ in range(n_cols)] for _ in range(n_rows)]
        self.pos = None
        self.num_rows = n_rows
        self.num_cols = n_cols
        self.placeholder_length = len(str(n_rows * n_cols))
        self.move_set = None
        self.visited_pos = set()

    # Returns display-coordinates if valid, else none
    def get_pos(self, start=False, repeat=False):
        try:
            if start:
                line = input("Enter the knight's starting position: ")
            else:
                if repeat:
                    line = input("Invalid move! Enter your next move: ")
                else:
                    line = input("Enter your next move: ")
            line = line.split()
            if len(line) != 2:
                raise RuntimeError
            else:
                dims = (int(line[1]), int(line[0]))
                # Switch to grid-based coordinates
                if not self.validate_coord(*(i - 1 for i in dims)):
                    raise RuntimeError
                return dims
        except (ValueError, RuntimeError):
            print("Invalid dimensions!")
            return None

    # Validates a set of grid-based coordinates
    def validate_coord(self, row, col):
        return 0 <= row < self.num_rows and 0 <= col < self.num_cols

    def move_pos(self, first_move=False):
        pos = self.get_pos(start=first_move)
        while first_move and pos is None:
            pos = self.get_pos(start=first_move)
        while not first_move and (pos is None or pos == self.pos or tuple(i - 1 for i in pos) not in self.move_set):
            pos = self.get_pos(repeat=True)
        if self.pos:
            self.grid[self.pos[0]][self.pos[1]] = False
        self.pos = tuple(i - 1 for i in pos)
        self.grid[self.pos[0]][self.pos[1]] = True
        self.move_set = self.potential_moves()
        self.visited_pos.add(self.pos)

    def __str__(self):
        bar = " " + ("-" * (self.num_cols * (self.placeholder_length + 1) + 3))
        placeholder = "_" * self.placeholder_length
        elems = [bar]
        for i in range(self.num_rows, 0, -1):
            element = [f"{i}|"]
            for j in range(1, self.num_cols + 1):
                if self.grid[i - 1][j - 1]:
                    element.append(" " * (self.placeholder_length - 1) + 'X')
                elif (i - 1, j - 1) in self.visited_pos:
                    element.append(" " * (self.placeholder_length - 1) + '*')
                elif (i - 1, j - 1) in self.move_set:
                    element.append(" " * (self.placeholder_length - 1) + str(len(self.potential_moves((i - 1, j - 1)))))
                else:
                    element.append(placeholder)
            element.append("|")
            elems.append(" ".join(element))
        elems.append(bar)
        elems.append("   " +
                     " ".join((" " * self.placeholder_length)[:-1] + str(i) for i in range(1, self.num_cols + 1)))
        return "\n".join(elems)

    # Returns a set of potential moves based on the knight's current position (or optional argument pos)
    def potential_moves(self, pos=None):
        valid_moves = set()
        if pos:
            for shift in Board.POTENTIAL_KNIGHT_SHIFTS:
                new_pos = tuple(old + delta for old, delta in zip(pos, shift))
                if self.validate_coord(*new_pos) and new_pos != self.pos and new_pos not in self.visited_pos:
                    valid_moves.add(new_pos)
        elif self.pos:
            for shift in Board.POTENTIAL_KNIGHT_SHIFTS:
                new_pos = tuple(old + delta for old, delta in zip(self.pos, shift))
                if self.validate_coord(*new_pos) and new_pos not in self.visited_pos:
                    valid_moves.add(new_pos)
        return valid_moves


board = Board.create()
while not board:
    board = Board.create()
board.move_pos(True)
print(board)
while True:
    print()
    board.move_pos()
    print(board)
    if not board.move_set:
        print()
        if len(board.visited_pos) == board.num_cols * board.num_rows:
            print("What a great tour! Congratulations!")
        else:
            print("No more possible moves!")
            print(f"Your knight visited {len(board.visited_pos)} squares!")
        break
