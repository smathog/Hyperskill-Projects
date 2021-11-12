import numpy as np

from typing import Union, Type, Tuple

from shapes import *


def main():
    piece_class: str = input()
    dims: Tuple[int, ...] = tuple(int(i) for i in input().split(" ")[::-1])
    sm = ShapeManager(generate_piece_class(piece_class))
    grid = generate_grid(*dims)
    print()
    print_grid(grid)
    print()
    print_grid(sm.inject(grid))
    while True:
        print()
        command = input()
        if command == "exit":
            break
        elif command == "rotate":
            sm.rotate()
        elif command == "left":
            sm.shift(0, -1)
        elif command == "right":
            sm.shift(0, 1)
        elif command == "down":
            # Valid, but do nothing
            pass
        else:
            raise Warning("Bad input!")
        sm.shift(1, 0)
        grid = generate_grid(*dims)
        print_grid(sm.inject(grid))


def print_grid(arr: np.ndarray) -> None:
    for row in arr:
        print(" ".join("0" if i == 1 else "-" for i in row))


def generate_grid(rows: int, cols: int) -> np.ndarray:
    return np.zeros((rows, cols))


def generate_piece_class(choice: str) -> Type[Union[IShape, SShape, ZShape, LShape, JShape, OShape, TShape]]:
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
    return cls


class ShapeManager:
    def __init__(self, shape_class: Type[Union[IShape, SShape, ZShape, LShape, JShape, OShape, TShape]],
                 start_pos: Tuple[int, int] = None,
                 rotation_count: int = 0):
        self.shape_class: Type[Union[IShape, SShape, ZShape, LShape, JShape, OShape, TShape]] = shape_class
        if start_pos is None:
            self.row_pos, self.col_pos = shape_class.insert
        else:
            self.row_pos, self.col_pos = start_pos
        self.rotation_count = rotation_count

    def rotate(self):
        self.rotation_count += 1

    def shift(self, row_shift: int, col_shift: int):
        self.row_pos += row_shift
        self.col_pos += col_shift

    def inject(self, grid: np.ndarray):
        grid_rows, grid_cols = grid.shape
        shape: np.ndarray = self.shape_class.rotate(self.rotation_count)
        for row, row_arr in enumerate(shape):
            row_pos = (row + self.row_pos) % grid_rows
            for col, num in enumerate(row_arr):
                col_pos = (col + self.col_pos) % grid_cols
                grid[row_pos][col_pos] += num
        return grid


def align_test():
    """
    Test function to visualize alignment of given pieces on the grid
    :return:
    """
    for i in range(5):
        grid = generate_grid(20, 10)
        sm = ShapeManager(generate_piece_class("T"), rotation_count=i)
        my_grid = sm.inject(grid)
        indices = list()
        for row, row_arr in enumerate(my_grid):
            for col, num in enumerate(row_arr):
                if num == 1:
                    indices.append(row * 10 + col)
        print_grid(my_grid)
        print(indices)


if __name__ == "__main__":
    main()
