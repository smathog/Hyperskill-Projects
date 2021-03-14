import re


def validate_input(line):
    num = True
    for token in line.split():
        if num:
            if not re.fullmatch(r"[+|-]?[0-9]+", token):
                return False
            else:
                num = False
        else:
            if not re.fullmatch(r"(\++|-+)", token):
                return False
            else:
                num = True
    return True


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
        if not validate_input(nums):
            print("Invalid expression")
        else:
            add = True
            total = 0
            for term in nums.split():
                if re.fullmatch("-(--)*", term):
                    add = False
                elif re.fullmatch("\\++", term) or re.fullmatch("(--)+", term):
                    add = True
                else:
                    if add:
                        total += int(term)
                    else:
                        total -= int(term)
            print(total)
