from Matrix import Matrix


def load_matrix(n_rows):
    fields = [[int(float(i)) if float(i).is_integer() else float(i) for i in input().split()] for _ in range(n_rows)]
    return Matrix(fields)


def add_matrices():
    a, b = load_two_matrices()
    return a + b


def scalar_multiply():
    row = int(input("Enter size of matrix:").split()[0])
    print("Enter matrix:")
    matrix = load_matrix(row)
    scalar = float(input("Enter constant: "))
    scalar = int(scalar) if scalar.is_integer() else scalar
    return matrix * scalar


def matrix_multiply():
    a, b = load_two_matrices()
    return a @ b


def load_two_matrices():
    rows = int(input("Enter size of first matrix:").split()[0])
    print("Enter first matrix:")
    a = load_matrix(rows)
    rows = int(input("Enter size of second matrix:").split()[0])
    print("Enter second matrix:")
    b = load_matrix(rows)
    return a, b


def menu():
    while True:
        print("1. Add matrices")
        print("2. Multiply matrix by a constant")
        print("3. Multiply matrices")
        print("0. Exit")
        choice = int(input("Your choice: "))
        if choice == 0:
            break
        elif choice == 1:
            try:
                result = add_matrices()
                print("The result is: ")
                print(result)
            except Exception:
                print("The operation cannot be performed")
        elif choice == 2:
            result = scalar_multiply()
            print("The result is: ")
            print(result)
        elif choice == 3:
            try:
                result = matrix_multiply()
                print("The result is: ")
                print(result)
            except Exception:
                print("The operation cannot be performed")
        print()


menu()