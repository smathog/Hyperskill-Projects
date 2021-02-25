from Matrix import Matrix

rows_a = int(input().split()[0])
fields_a = [[int(i) for i in input().split()] for _ in range(rows_a)]
matrix_a = Matrix(fields_a)
num = int(input())
print(matrix_a * num)
