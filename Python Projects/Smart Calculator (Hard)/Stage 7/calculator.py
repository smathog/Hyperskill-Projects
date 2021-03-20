import re
from collections import deque

PRECEDENCE_DICT = {"+": 0, "-": 0, "*": 1, "/": 1, "(": 2, ")": 2}
INVALID_EXPRESSION = "Invalid expression"


# Return a postfix list representation if valid, otherwise string explaining error
def validate_statement(line, var_dict):
    # First, get rid of all whitespace in the statement
    line = "".join(line.split())

    # First term must be an operand
    operand = True
    op_stack = deque()
    result = []

    # Iterate over line, parsing for valid statements
    while line:
        # check if matches operand
        if re.match(r"[+\-]?([a-zA-Z]+|\d+)", line) and operand:
            operand = False
            # Check if leading unary operator
            unary_minus = False
            # In the unary + case, can just discard and treat the value plainly
            if line.startswith("+"):
                line = line[1:]
            elif line.startswith("-"):
                unary_minus = True
                line = line[1:]
            match = re.match(r"([a-zA-Z]+|\d+)", line)
            # Match on possible valid variable name
            if re.match(r"[a-zA-Z]+", line):
                if line[:match.end()] not in var_dict:
                    return "Unknown variable"
                else:
                    if unary_minus:
                        result.append(var_dict[line[:match.end()]] * -1)
                    else:
                        result.append(var_dict[line[:match.end()]])
            # Match must be on numbers
            else:
                if unary_minus:
                    result.append(int(line[:match.end()]) * -1)
                else:
                    result.append(int(line[:match.end()]))
            # Either way, remove the operand from line
            line = line[match.end():]
        # check if matches operators
        elif re.match(r"(\++|-+|\*|/)", line) and not operand:
            if operand:
                return INVALID_EXPRESSION
            else:
                operand = True
                # Extract the operator and trim down line
                if line.startswith("*") or line.startswith("/"):
                    operator = line[0:1]
                    line = line[1:]
                else:
                    match = re.match(r"(\++|-+)", line)
                    if line.startswith("+"):
                        operator = "+"
                    else:
                        if (match.end() - match.start()) % 2 == 1:
                            operator = "-"
                        else:
                            operator = "+"
                    line = line[match.end():]

                # Handle insertion of operator into result or stack
                if not op_stack or op_stack[-1] == "(":
                    op_stack.append(operator)
                elif PRECEDENCE_DICT[op_stack[-1]] < PRECEDENCE_DICT[operator]:
                    op_stack.append(operator)
                else:
                    while op_stack:
                        if op_stack[-1] == "(" or PRECEDENCE_DICT[op_stack[-1]] < PRECEDENCE_DICT[operator]:
                            break
                        else:
                            result.append(op_stack.pop())
                    op_stack.append(operator)
        # check if parentheses
        elif re.match(r"[()]", line):
            # If left parentheses encountered, push onto stack
            if line.startswith("("):
                op_stack.append("(")
            # If right parentheses encountered, pop operator stack and add until left parentheses encountered
            # If stack is empty before, a mismatch has occurred (syntax error)
            elif line.startswith(")"):
                if not op_stack:
                    return INVALID_EXPRESSION
                else:
                    while op_stack[-1] != "(":
                        result.append(op_stack.pop())
                        if not op_stack:
                            return INVALID_EXPRESSION
                    if not op_stack:
                        return INVALID_EXPRESSION
                    else:
                        # Discard both matching left and right parentheses
                        op_stack.pop()
            # Either way, shorten line:
            line = line[1:]
        # Failure to make any matches above implies syntax error, so just indicate that by return
        else:
            return INVALID_EXPRESSION

    # Add any remaining operators in the stack to the result list
    while op_stack:
        if op_stack[-1] == "(":
            return INVALID_EXPRESSION
        else:
            result.append(op_stack.pop())

    return result


# Evaluate a postfix list representation validated by validate_statement
def evaluate_statement(result):
    stack = deque()
    for term in result:
        if isinstance(term, int):
            stack.append(term)
        else:
            b = stack.pop()
            a = stack.pop()
            if term == "+":
                stack.append(a + b)
            elif term == "-":
                stack.append(a - b)
            elif term == "*":
                stack.append(a * b)
            elif term == "/":
                stack.append(a // b)
            else:
                print("Ruh roh, this shouldn't happen!")
                exit()
    return stack[-1]


def validate_name(name):
    if not re.fullmatch(r"[a-zA-Z]+", name):
        return False
    else:
        return True


# Return true if valid assignment statement, otherwise string explaining reason
def validate_assignment(line, var_dict):
    match = re.fullmatch(r"(\S+)\s*=\s*(\S+)", line)
    if not match:
        return "Invalid assignment"
    else:
        # Make sure lvalue is value identifier
        if not validate_name(match.group(1)):
            return "Invalid identifier"
        # if rvalue is number, just assign
        if re.fullmatch(r"\d+", match.group(2)):
            var_dict[match.group(1)] = int(match.group(2))
            return True
        # If rvalue is variable, validate name
        elif validate_name(match.group(2)):
            if match.group(2) not in var_dict:
                return "Unknown variable"
            else:
                var_dict[match.group(1)] = var_dict[match.group(2)]
                return True
        else:
            return "Invalid assignment"


variable_dict = {}
while True:
    nums = input().strip()
    if nums == "":
        continue
    elif nums == "/exit":
        print("Bye!")
        break
    elif nums == "/help":
        print("This program calculates the sum, difference, product, and quotient of numbers.")
        print("It can also accept variable declarations and order-of-operations based upon standard operators.")
    elif nums.startswith("/"):
        print("Unknown command")
    else:
        if re.fullmatch(r".*=.*", nums):
            assign = validate_assignment(nums, variable_dict)
            if isinstance(assign, str):
                print(assign)
        else:
            results = validate_statement(nums, variable_dict)
            if isinstance(results, str):
                print(results)
            else:
                print(evaluate_statement(results))
