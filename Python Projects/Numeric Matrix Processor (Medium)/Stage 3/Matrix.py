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
            raise Exception("ERROR")
        else:
            fields = [[a + b for a, b in zip(row_s, row_o)] for row_s, row_o in zip(self.rows, other.rows)]
            return Matrix(fields)

    def __mul__(self, other):
        """Multiplies self by scalar other to yield new matrix"""
        fields = [[a * other for a in row] for row in self.rows]
        return Matrix(fields)

    def __matmul__(self, other):
        """Multiplies self by Matrix other to yield a new matrix"""
        if self.n_cols != other.n_rows:
            raise Exception("The operation cannot be performed.")
        else:
            f = [[sum(a*b for a, b in zip(row, other.get_col(i))) for i in range(other.n_cols)] for row in self.rows]
            return Matrix(f)

    def get_col(self, index):
        if index < 0 or index >= self.n_cols:
            raise IndexError("Bad index to get_cols!")
        else:
            return [row[index] for row in self.rows]
