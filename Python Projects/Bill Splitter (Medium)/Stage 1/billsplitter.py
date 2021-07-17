print("Enter the number of friends joining (including you): ")
num_people = int(input())
if num_people > 0:
    print("Enter the name of every friend (including you), each on a new line: ")
    bill_dict = {input(): 0 for _ in range(num_people)}
    print()
    print(bill_dict)
else:
    print("No one is joining for the party")
