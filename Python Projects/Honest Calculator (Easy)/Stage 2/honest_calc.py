from typing import (Tuple)

MSG_0: str = "Enter an equation"
MSG_1: str = "Do you even know what numbers are? Stay focused!"
MSG_2: str = "Yes ... an interesting math operation." \
             " You've slept through all classes, haven't you?"
MSG_3: str = "Yeah... division by zero. Smart move..."


def main() -> None:
    print(stage_2(stage_1()))


def stage_1() -> Tuple[float, str, float]:
    """

    :return: Given valid input, return the associated numbers as floats and operator as str
    """

    while True:
        print(MSG_0)
        calc = input().split(" ")
        if not is_num(calc[0]) or not is_num(calc[2]):
            print(MSG_1)
        elif not is_valid_op(calc[1]):
            print(MSG_2)
        else:
            return float(calc[0]), calc[1], float(calc[2])


def stage_2(expr: Tuple[float, str, float]) -> float:
    """

    :param expr: An expression to be evaluated based on the operator in str
    :return: The evaluated expression
    """

    if expr[1] == "+":
        return expr[0] + expr[2]
    elif expr[1] == "-":
        return expr[0] - expr[2]
    elif expr[1] == "*":
        return expr[0] * expr[2]
    else:
        if expr[2] == 0:
            print(MSG_3)
            return stage_2(stage_1())
        else:
            return expr[0] / expr[2]


def is_num(num: str) -> bool:
    """

    :param num: a string which may or may not represent a float or int
    :return: true if num is float or int, false otherwise
    """

    try:
        int(num)
        return True
    except ValueError:
        try:
            float(num)
            return True
        except ValueError:
            return False


def is_valid_op(op: str) -> bool:
    """

    :param op: The operator string to be assessed
    :return: bool if operator is a valid/known operator
    """

    return op == "+" or op == "-" or op == "*" or op == "/"


if __name__ == "__main__":
    main()
