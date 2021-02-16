def build_grid(chars):
    rows = []
    for i in range(3):
        rows.append(' '.join([elem for elem in chars[i * 3:i * 3 + 3]]))
    return "---------\n" + '\n'.join(["| " + row + " |" for row in rows]) + '\n---------'


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


pattern = input("Enter cells: ")
print(build_grid(pattern))
print(check_game_over(pattern))
