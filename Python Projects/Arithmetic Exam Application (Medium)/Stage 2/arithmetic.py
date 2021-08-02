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


exp = generate_expression()
print(exp)
user_response = int(input())
if user_response == evaluate_expression(exp):
    print("Right!")
else:
    print("Wrong!")
