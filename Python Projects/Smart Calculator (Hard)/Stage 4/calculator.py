import re

while True:
    nums = input()
    if nums == "":
        continue
    elif nums == "/exit":
        print("Bye!")
        break
    elif nums == "/help":
        print("This program calculates the sum and difference of numbers")
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
