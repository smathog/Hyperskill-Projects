MSG_0: str = "Enter an equation"
MSG_1: str = "Do you even know what numbers are? Stay focused!"
MSG_2: str = "Yes ... an interesting math operation." \
             " You've slept through all classes, haven't you?"


def main() -> None:
    stage_1()


def stage_1() -> None:
    while True:
        print(MSG_0)
        calc = input().split(" ")
        if not is_num(calc[0]) or not is_num(calc[2]):
            print(MSG_1)
        elif not is_valid_op(calc[1]):
            print(MSG_2)
        else:
            break


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
