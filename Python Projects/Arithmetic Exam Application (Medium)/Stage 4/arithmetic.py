from random import randint
from random import choice

LEVEL_1_DESCRIPTION = "simple operations with numbers 2-9"
LEVEL_2_DESCRIPTION = "integral squares 11-29"


def generate_level_1_expression():
    operator = choice(["+", "-", "*"])
    return f"{randint(2, 9)} {operator} {randint(2, 9)}"


def evaluate_level_1_expression(expression):
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


def generate_level_2_expression():
    return randint(11, 29)


def evaluate_level_2_expression(expression):
    return expression * expression


def level_1_and_2_response_validator():
    while True:
        try:
            response = int(input())
            return response
        except ValueError:
            print("Incorrect format. Try again.")


def challenge(expression_generator, response_validator, evaluator):
    count = 0
    num_correct = 0
    while count < 5:
        expression = expression_generator()
        print(expression)
        user_response = response_validator()
        if user_response == evaluator(expression):
            print("Right!")
            num_correct += 1
        else:
            print("Wrong!")
        count += 1
    return num_correct


print("Which level do you want? Enter a number: ")
print(f"1 - {LEVEL_1_DESCRIPTION}")
print(f"2 - {LEVEL_2_DESCRIPTION}")
while True:
    selection = input()
    if selection != "1" and selection != "2":
        continue
    selection = int(selection)
    break
if selection == 1:
    num_right = challenge(generate_level_1_expression,
                          level_1_and_2_response_validator,
                          evaluate_level_1_expression)
else:
    num_right = challenge(generate_level_2_expression,
                          level_1_and_2_response_validator,
                          evaluate_level_2_expression)
print(f"Your mark is {num_right}/5. Would you like to save the result?")
print("Enter yes or no.")
positive_response = {"yes", "YES", "y", "Yes"}
if input() in positive_response:
    print("What is your name?")
    name = input()
    result_string = f"{name}: {num_right}/5 in level {selection} " \
                    f"({LEVEL_1_DESCRIPTION if selection == 1 else LEVEL_2_DESCRIPTION})."
    with open("results.txt", "a") as file:
        file.write(result_string)
    print("The results are saved in \"results.txt\".")
