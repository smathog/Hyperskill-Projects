while True:
    nums = input()
    if nums == "":
        continue
    elif nums == "/exit":
        print("Bye!")
        break
    else:
        print(sum(int(i) for i in nums.split()))
