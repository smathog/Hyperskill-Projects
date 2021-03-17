import re


# Return true if valid, otherwise string explaining error
def validate_statement(line, var_dict):
    num = True
    for token in line.split():
        if num:
            if not re.fullmatch(r"[+|-]?[0-9]+", token):
                if not validate_name(token):
                    return "Invalid identifier"
                elif token not in var_dict:
                    return "Unknown variable"
                else:
                    num = False
            else:
                num = False
        else:
            if not re.fullmatch(r"(\++|-+)", token):
                return "Invalid statement"
            else:
                num = True
    return True


def evaluate_statement(line, var_dict):
    add = True
    total = 0
    for term in line.split():
        if re.fullmatch("-(--)*", term):
            add = False
        elif re.fullmatch("\\++", term) or re.fullmatch("(--)+", term):
            add = True
        else:
            if term in var_dict:
                term = var_dict[term]
            if add:
                total += int(term)
            else:
                total -= int(term)
    return total


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
    nums = input()
    if nums == "":
        continue
    elif nums == "/exit":
        print("Bye!")
        break
    elif nums == "/help":
        print("This program calculates the sum and difference of numbers")
    elif nums.startswith("/"):
        print("Unknown command")
    else:
        if re.fullmatch(r".*=.*", nums):
            assign = validate_assignment(nums, variable_dict)
            if isinstance(assign, str):
                print(assign)
        else:
            statement = validate_statement(nums, variable_dict)
            if isinstance(statement, str):
                print(statement)
            else:
                print(evaluate_statement(nums, variable_dict))
