import numpy as np

from shapes import *


def main():
    choice: str = input()
    print()
    if choice == "I":
        cls = IShape
    elif choice == "S":
        cls = SShape
    elif choice == "Z":
        cls = ZShape
    elif choice == "L":
        cls = LShape
    elif choice == "J":
        cls = JShape
    elif choice == "O":
        cls = OShape
    elif choice == "T":
        cls = TShape
    else:
        raise Warning("Bad input!")

    grid = np.array([[0 for _ in range(4)] for _ in range(4)])
    print_grid(grid)
    for i in range(5):
        print()
        print_grid(cls.rotate(i))


def print_grid(arr: np.ndarray) -> None:
    for row in arr:
        print(" ".join("0" if i == 1 else "-" for i in row))


if __name__ == "__main__":
    main()
