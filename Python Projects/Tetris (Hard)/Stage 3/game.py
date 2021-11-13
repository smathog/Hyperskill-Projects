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
        grid = generate_grid(*dims)
        print()
        command = input()
        if command == "exit":
            break
        elif command == "rotate":
            sm.rotate(grid)
        elif command == "left":
            sm.shift(0, -1, grid)
        elif command == "right":
            sm.shift(0, 1, grid)
        elif command == "down":
            # Valid, but do nothing
            pass
        else:
            raise Warning("Bad input!")
        if not sm.locked:
            sm.shift(1, 0, grid)
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
        self.locked = False

    def is_locked(self) -> bool:
        return self.locked

    def rotate(self, grid: np.ndarray):
        if not self.locked:
            self.rotation_count += 1
            if not self.check_inject_safe(grid):
                self.rotation_count -= 1

    def shift(self, row_shift: int, col_shift: int, grid: np.ndarray):
        if not self.locked:
            self.row_pos += row_shift
            self.col_pos += col_shift
            if not self.check_inject_safe(grid):
                self.row_pos -= row_shift
                self.col_pos -= col_shift

    def check_inject_safe(self, grid: np.ndarray) -> bool:
        """

        :param grid: Grid to inject shape into
        :return: Whether or not shape can be injected in current configuration
        """

        # A locked shape is only locked in a safe config, so can skip checks
        if self.locked:
            return True
        else:
            grid_rows, grid_cols = grid.shape
            shape: np.ndarray = self.shape_class.rotate(self.rotation_count)
            for row, row_arr in enumerate(shape):
                row_pos = row + self.row_pos
                for col, num in enumerate(row_arr):
                    col_pos = col + self.col_pos
                    if num == 1:
                        # Check that row_pos and col_pos are valid
                        if not (0 <= row_pos < grid_rows and 0 <= col_pos < grid_cols):
                            return False
            return True

    # Shape always starts in safe configuration and checks on shift and rotate prevent
    # entering unsafe configuration
    def inject(self, grid: np.ndarray):
        grid_rows, grid_cols = grid.shape
        shape: np.ndarray = self.shape_class.rotate(self.rotation_count)
        for row, row_arr in enumerate(shape):
            row_pos = row + self.row_pos
            for col, num in enumerate(row_arr):
                col_pos = col + self.col_pos
                if num == 1:
                    grid[row_pos][col_pos] += num
                    # If any part of the actual piece hits the bottom row, lock it down
                    if not self.locked and row_pos == grid_rows - 1:
                        self.locked = True
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
