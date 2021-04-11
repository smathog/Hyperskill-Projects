class Board:
    @staticmethod
    def create():
        try:
            line = input("Enter your board dimensions: ")
            line = line.split()
            if len(line) != 2:
                raise RuntimeError
            dims = (int(line[1]), int(line[0]))
            for num in dims:
                if num < 0:
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

    def get_pos(self):
        try:
            line = input("Enter the knight's starting position: ")
            line = line.split()
            if len(line) != 2:
                raise RuntimeError
            else:
                dims = (int(line[1]), int(line[0]))
                for num, limit in zip(dims, (self.num_rows, self.num_cols)):
                    if not 1 <= num <= limit:
                        raise RuntimeError
                return dims
        except (ValueError, RuntimeError):
            print("Invalid dimensions!")
            return None

    def move_pos(self):
        pos = self.get_pos()
        if pos:
            if self.pos:
                self.grid[self.pos[0]][self.pos[1]] = False
            self.pos = tuple(i - 1 for i in pos)
            self.grid[self.pos[0]][self.pos[1]] = True
            return True
        else:
            return False

    def __str__(self):
        bar = " " + ("-" * (self.num_cols * (self.placeholder_length + 1) + 3))
        placeholder = "_" * self.placeholder_length
        elems = [bar]
        for i in range(self.num_rows, 0, -1):
            element = [f"{i}|"]
            for j in range(1, self.num_cols + 1):
                element.append(" " * (self.placeholder_length - 1) + 'X' if self.grid[i - 1][j - 1] else placeholder)
            element.append("|")
            elems.append(" ".join(element))
        elems.append(bar)
        elems.append("   " +
                     " ".join((" " * self.placeholder_length)[:-1] + str(i) for i in range(1, self.num_cols + 1)))
        return "\n".join(elems)


board = Board.create()
while not board:
    board = Board.create()
while not board.move_pos():
    pass
print(board)
