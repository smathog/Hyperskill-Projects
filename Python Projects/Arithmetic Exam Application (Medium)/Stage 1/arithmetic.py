command = input().split()
first = int(command[0])
second = int(command[2])
if command[1] == "+":
    print(first + second)
elif command[1] == "-":
    print(first - second)
elif command[1] == "*":
    print(first * second)
elif command[1] == "/":
    print(first / second)
else:
    print("Unknown operand.")
