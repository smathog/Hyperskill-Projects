exams = [int(input()) for _ in range(3)]
average = sum(exams) / len(exams)
print(average)
if average >= 60.0:
    print("Congratulations, you are accepted!")
else:
    print("We regret to inform you that we will not be able to offer you admission.")
