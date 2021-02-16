def build_grid(chars):
    rows = []
    for i in range(3):
        row = []
        for j in range(3):
            row.append(chars[i * 3 + j])
        rows.append(row)
    return rows


def print_grid(grid):
    string_rows = [" ".join(row) for row in grid]
    print("---------\n" + "\n".join(["| " + row + " |" for row in string_rows]) + "\n---------")


def grid_to_pattern(grid):
    return "".join([char for row in grid for char in row])


def move(grid, char, row, col):
    # Check row, col are ints
    if not (isinstance(row, int) and isinstance(col, int)):
        print("You should enter numbers!")
        return False

    # Check coords are valid
    if not (1 <= row <= 3 and 1 <= col <= 3):
        print("Coordinates should be from 1 to 3!")
        return False

    # Convert from game-based coordinates to Python 2d list coordinates
    row -= 1
    col -= 1

    # Make sure we are modifying a valid non-occupied cell
    if grid[row][col] != "_":
        print("This cell is occupied! Choose another one!")
        return False
    else:
        grid[row][col] = char
        return True


def check_num_marks(chars):
    num_x = 0
    num_o = 0
    for c in chars:
        if c == 'X':
            num_x += 1
        elif c == 'O':
            num_o += 1
    return abs(num_o - num_x) <= 1


def check_game_over(chars):
    if not check_num_marks(chars):
        return "Impossible"

    x_wins = False
    o_wins = False

    # Check for three in a row on the rows
    for i in range(3):
        num_x = 0
        num_o = 0
        for j in range(3):
            c = chars[i * 3 + j]
            if 'X' == c:
                num_x += 1
            elif 'O' == c:
                num_o += 1
        if num_x == 3:
            x_wins = True
        elif num_o == 3:
            o_wins = True

    # Check for three in a row on the columns
    for i in range(3):
        num_x = 0
        num_o = 0
        for j in range(3):
            c = chars[i + 3 * j]
            if 'X' == c:
                num_x += 1
            elif 'O' == c:
                num_o += 1
        if num_x == 3:
            x_wins = True
        elif num_o == 3:
            o_wins = True

    if x_wins and o_wins:
        return "Impossible"
    elif x_wins:
        return "X wins"
    elif o_wins:
        return "O wins"

    # Check first diagonal
    if chars[0] == chars[4] == chars[8]:
        return chars[0] + " wins"

    # Check second diagonal
    elif chars[2] == chars[4] == chars[6]:
        return chars[2] + " wins"

    # Determine if draw
    else:
        num_blank = sum([1 for c in chars if c == "_"])
        if num_blank == 0:
            return "Draw"
        else:
            return "Game not finished"


def play_game():
    pattern = input("Enter cells: ")
    grid = build_grid(pattern)
    print_grid(grid)
    valid_move = False
    while not valid_move:
        row, col = [int(i) for i in input("Enter the coordinates: ").split()]
        valid_move = move(grid, "X", row, col)
        if valid_move:
            print_grid(grid)


play_game()
