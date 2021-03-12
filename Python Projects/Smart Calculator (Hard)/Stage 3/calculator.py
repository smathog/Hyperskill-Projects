while True:
    nums = input()
    if nums == "":
        continue
    elif nums == "/exit":
        print("Bye!")
        break
    elif nums == "/help":
        print("This program calculates the sum of numbers")
    else:
        print(sum(int(i) for i in nums.split()))
