from copy import deepcopy


class Matrix:
    def __init__(self, fields):
        self.rows = []
        for row in fields:
            self.rows.append(deepcopy(row))
        self.n_rows = len(self.rows)
        self.n_cols = len(self.rows[0])

    def __repr__(self):
        return self.__str__()

    def __str__(self):
        return "\n".join(" ".join(str(i) for i in row) for row in self.rows)

    def __add__(self, other):
        if self.n_rows != other.n_rows or self.n_cols != other.n_cols:
            print("ERROR")
            exit()
        fields = [[a + b for a, b in zip(row_s, row_o)] for row_s, row_o in zip(self.rows, other.rows)]
        return Matrix(fields)
