import numpy as np

from abc import ABC
from typing import List, Tuple


class Shape(ABC):
    grid: np.ndarray
    shifts: List[Tuple[int, int]]
    insert: Tuple[int, int]

    @classmethod
    def rotate(cls, num_rotations: int) -> np.ndarray:
        num_rotations = num_rotations % 4
        arr = np.rot90(cls.grid, k=num_rotations)
        for index, shift in enumerate(cls.shifts[num_rotations]):
            arr = np.roll(arr, shift, axis=index)
        return arr


class IShape(Shape):
    grid: np.ndarray = np.array([[0, 1, 0, 0],
                                 [0, 1, 0, 0],
                                 [0, 1, 0, 0],
                                 [0, 1, 0, 0]])
    shifts = [(0, 0), (-2, 0), (0, -1), (-1, 0)]
    insert = (0, 3)


class SShape(Shape):
    grid: np.ndarray = np.array([[0, 0, 0, 0],
                                 [0, 1, 1, 0],
                                 [1, 1, 0, 0],
                                 [0, 0, 0, 0]])
    shifts = [(0, 0), (0, 0), (0, -1), (1, 0)]
    insert = (-1, 3)


class ZShape(Shape):
    grid: np.ndarray = np.array([[0, 0, 0, 0],
                                 [1, 1, 0, 0],
                                 [0, 1, 1, 0],
                                 [0, 0, 0, 0]])
    shifts = [(0, 0), (0, -1), (0, -1), (1, -1)]
    insert = (-1, 4)


class LShape(Shape):
    grid: np.ndarray = np.array([[0, 1, 0, 0],
                                 [0, 1, 0, 0],
                                 [0, 1, 1, 0],
                                 [0, 0, 0, 0]])

    shifts = [(0, 0), (-1, 0), (-1, 0), (-1, 0)]
    insert = (0, 3)


class JShape(Shape):
    grid: np.ndarray = np.array([[0, 0, 1, 0],
                                 [0, 0, 1, 0],
                                 [0, 1, 1, 0],
                                 [0, 0, 0, 0]])
    shifts = [(0, 0), (-1, 0), (-1, 0), (-1, 0)]
    insert = (0, 3)


class OShape(Shape):
    grid: np.ndarray = np.array([[1, 1],
                                 [1, 1]])
    shifts = [(0, 0), (0, 0), (0, 0), (0, 0)]
    insert = (0, 4)


class TShape(Shape):
    grid: np.ndarray = np.array([[0, 1, 0, 0],
                                 [0, 1, 1, 0],
                                 [0, 1, 0, 0],
                                 [0, 0, 0, 0]])
    shifts = [(0, 0), (-1, 0), (-1, 0), (-1, 0)]
    insert = (0, 3)
