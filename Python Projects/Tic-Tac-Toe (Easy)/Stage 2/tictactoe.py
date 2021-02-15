def build_grid(chars):
    rows = []
    for i in range(3):
        rows.append(' '.join([elem for elem in chars[i * 3:i * 3 + 3]]))
    return "---------\n" + '\n'.join(["| " + row + " |" for row in rows]) + '\n---------'


print(build_grid(input("Enter cells: ")))
