from random import randint
from random import choice


def generate_expression():
    operator = choice(["+", "-", "*"])
    return f"{randint(2, 9)} {operator} {randint(2, 9)}"


def evaluate_expression(expression):
    command = expression.split()
    first = int(command[0])
    second = int(command[2])
    if command[1] == "+":
        return first + second
    elif command[1] == "-":
        return first - second
    elif command[1] == "*":
        return first * second
    else:
        raise RuntimeError("Unknown operator")


count = 0
num_correct = 0
while count < 5:
    exp = generate_expression()
    print(exp)
    while True:
        try:
            user_response = int(input())
            break
        except ValueError:
            print("Incorrect format.")
    if user_response == evaluate_expression(exp):
        print("Right!")
        num_correct += 1
    else:
        print("Wrong!")
    count += 1
print(f"Your mark is {num_correct}/5.")
