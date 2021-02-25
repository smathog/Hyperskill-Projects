from Matrix import Matrix

rows_a = int(input().split()[0])
fields_a = [[int(i) for i in input().split()] for _ in range(rows_a)]
rows_b = int(input().split()[0])
fields_b = [[int(i) for i in input().split()] for _ in range(rows_b)]
matrix_a = Matrix(fields_a)
matrix_b = Matrix(fields_b)
print(matrix_a + matrix_b)
