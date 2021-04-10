class Board:
    num_rows = 8
    num_cols = num_rows

    def __init__(self):
        self.grid = [[False for _ in range(8)] for _ in range(8)]
        self.pos = None

    @staticmethod
    def get_pos():
        try:
            line = input("Enter the knight's starting position: ")
            line = line.split()
            if len(line) != 2:
                raise RuntimeError
            else:
                dims = (int(line[0]), int(line[1]))
                for num in dims:
                    if not 1 <= num <= Board.num_rows:
                        raise RuntimeError
                return dims
        except (ValueError, RuntimeError):
            print("Invalid dimensions!")
            return None

    def move_pos(self, pos):
        if self.pos:
            self.grid[self.pos[0]][self.pos[1]] = False
        self.pos = tuple(i - 1 for i in pos)
        self.grid[self.pos[0]][self.pos[1]] = True

    def __str__(self):
        bar = " -------------------"
        elems = [bar]
        for i in range(8, -1, -1):
            element = [f"{i}|"]
            for j in range(1, 9):
                element.append(f" _" if self.grid[i - 1][j - 1] else f" X")
            element.append(" |")
            elems.append("".join(element))
        elems.append(bar)
        elems.append("   1 2 3 4 5 6 7 8 ")
        return "\n".join(elems)


board = Board()
pos = Board.get_pos()
if pos:
    board.move_pos(pos)
    print(board)
