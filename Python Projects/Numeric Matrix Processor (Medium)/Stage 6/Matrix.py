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

    def transpose_main_diagonal(self):
        fields = [[0 for _ in range(self.n_cols)] for _ in range(self.n_rows)]
        for i in range(self.n_rows):
            for j in range(self.n_cols):
                fields[i][j] = self.rows[j][i]
        return Matrix(fields)

    def transpose_side_diagonal(self):
        fields = [[0 for _ in range(self.n_cols)] for _ in range(self.n_rows)]
        for i in range(self.n_rows):
            for j in range(self.n_cols):
                fields[i][j] = self.rows[self.n_rows - j - 1][self.n_cols - i - 1]
        return Matrix(fields)

    def transpose_vertical_line(self):
        fields = [[0 for _ in range(self.n_cols)] for _ in range(self.n_rows)]
        for i in range(self.n_rows):
            for j in range(self.n_cols):
                fields[i][j] = self.rows[i][self.n_cols - j - 1]
        return Matrix(fields)

    def transpose_horizontal_line(self):
        fields = [[0 for _ in range(self.n_cols)] for _ in range(self.n_rows)]
        for i in range(self.n_rows):
            for j in range(self.n_cols):
                fields[i][j] = self.rows[self.n_rows - i - 1][j]
        return Matrix(fields)

    def determinant(self):
        # Base Cases
        if self.n_cols == 1:
            return self.rows[0][0]
        elif self.n_cols == 2:
            return self.rows[0][0] * self.rows[1][1] - self.rows[1][0] * self.rows[0][1]
        else:
            return sum(self.rows[0][j] * self.cofactor(0, j) for j in range(self.n_cols))

    def cofactor_matrix(self, row, col):
        fields = [[self.rows[i][j] for j in range(self.n_cols) if j != col] for i in range(self.n_rows) if i != row]
        return Matrix(fields)

    def cofactor(self, row, col):
        return (-1) ** (row + col) * self.cofactor_matrix(row, col).determinant()

    def inverse_matrix(self):
        det = self.determinant()
        if det == 0:
            raise RuntimeError("This matrix doesn't have an inverse.")
        else:
            fields = [[self.cofactor(i, j) for j in range(self.n_cols)] for i in range(self.n_rows)]
            return Matrix(fields).transpose_main_diagonal() * (1 / det)
