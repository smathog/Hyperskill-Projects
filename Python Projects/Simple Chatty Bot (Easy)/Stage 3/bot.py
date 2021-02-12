print('Hello! My name is Aid.')
print('I was created in 2020.')
print('Please, remind me your name.')

name = input()

print('What a great name you have, ' + name + '!')
print('Let me guess your age.')
print('Enter remainders of dividing your age by 3, 5 and 7.')

# reading all remainders
remainder_3 = int(input())
remainder_5 = int(input())
remainder_7 = int(input())

age = (remainder_3 * 70 + remainder_5 * 21 + remainder_7 * 15) % 105

print("Your age is {your_age}; that's a good time to start programming!".format(your_age=age))
