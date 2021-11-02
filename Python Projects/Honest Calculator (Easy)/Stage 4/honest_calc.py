from typing import (Tuple, List)


def main() -> None:
    calc = HonestCalculator()
    calc.calculation_loop()


class HonestCalculator:
    MSG_0: str = "Enter an equation"
    MSG_1: str = "Do you even know what numbers are? Stay focused!"
    MSG_2: str = "Yes ... an interesting math operation." \
                 " You've slept through all classes, haven't you?"
    MSG_3: str = "Yeah... division by zero. Smart move..."
    MSG_4: str = "Do you want to store the result? (y / n):"
    MSG_5: str = "Do you want to continue calculations? (y / n):"
    MSG_6: str = " ... lazy"
    MSG_7: str = " ... very lazy"
    MSG_8: str = " ... very, very lazy"
    MSG_9: str = "You are"

    def __init__(self):
        self.memory: float = 0.0

    def calculation_loop(self) -> None:
        while True:
            result = self.stage_2(self.stage_1())
            print(result)
            if not self.stage_3(result):
                break

    def stage_1(self) -> Tuple[float, str, float]:
        """

        :return: Given valid input, return the associated numbers as floats and operator as str
        """

        while True:
            print(self.MSG_0)
            (x, op, y) = input().split(" ")
            if not self.is_num(x) and not x == "M":
                print(self.MSG_1)
            elif not self.is_num(y) and not y == "M":
                print(self.MSG_1)
            elif not self.is_valid_op(op):
                print(self.MSG_2)
            else:
                x = self.memory if x == "M" else float(x)
                y = self.memory if y == "M" else float(y)
                self.check(x, y, op)
                return x, op, y

    def stage_2(self, expr: Tuple[float, str, float]) -> float:
        """

        :param expr: An expression to be evaluated based on the operator in str
        :return: The evaluated expression
        """

        if expr[1] == "+":
            result = expr[0] + expr[2]
        elif expr[1] == "-":
            result = expr[0] - expr[2]
        elif expr[1] == "*":
            result = expr[0] * expr[2]
        else:
            if expr[2] == 0:
                print(self.MSG_3)
                result = self.stage_2(self.stage_1())
            else:
                result = expr[0] / expr[2]
        return result

    def stage_3(self, result: float) -> bool:
        """

        :param result: The result of stage_1 -> stage_2
        :return: true if continue calculations, false otherwise
        """
        while True:
            print(self.MSG_4)
            answer = input()
            if answer == "y" or answer == "n":
                break
        if answer == "y":
            self.memory = result
        while True:
            print(self.MSG_5)
            answer = input()
            if answer == "y" or answer == "n":
                break
        return answer == "y"

    @staticmethod
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

    @staticmethod
    def is_valid_op(op: str) -> bool:
        """

        :param op: The operator string to be assessed
        :return: bool if operator is a valid/known operator
        """

        return op == "+" or op == "-" or op == "*" or op == "/"

    @staticmethod
    def check(v1: float, v2: float, v3: str) -> None:
        """

        :param v1: num representing x
        :param v2: num representing y
        :param v3: str representing operator
        :return: None
        """

        msg: List[str] = []
        if HonestCalculator.is_one_digit(v1) and HonestCalculator.is_one_digit(v2):
            msg.append(HonestCalculator.MSG_6)
        if (v1 == 1 or v2 == 1) and v3 == "*":
            msg.append(HonestCalculator.MSG_7)
        if (v1 == 0 or v2 == 0) and (v3 == "*" or v3 == "+" or v3 == "-"):
            msg.append(HonestCalculator.MSG_8)
        if msg:
            msg.insert(0, HonestCalculator.MSG_9)
            print("".join(msg))

    @staticmethod
    def is_one_digit(v1: float) -> bool:
        """

        :param v1: The number to check
        :return: True if v1 is a single digit, False otherwise
        """

        return -10 < v1 < 10 and int(v1) == v1


if __name__ == "__main__":
    main()
