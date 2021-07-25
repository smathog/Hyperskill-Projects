from random import choice

print("Enter the number of friends joining (including you): ")
num_people = int(input())
if num_people > 0:
    print("Enter the name of every friend (including you), each on a new line: ")
    bill_dict = {input(): 0 for _ in range(num_people)}
    print()
    print("Enter the total bill value:")
    total_value = float(input())
    print()
    print("Do you want to use the \"Who is lucky?\" feature? Write Yes/No:")
    response = input()
    print()
    if response == "Yes":
        lucky = choice(list(bill_dict.keys()))
        print(f"{lucky} is the lucky one!")
    else:
        print("No one is going to be lucky")
    split_value = round(total_value / num_people, 2)
    # for key in bill_dict:
    #     bill_dict[key] = split_value
    # print()
    # print(bill_dict)
else:
    print("No one is joining for the party")
