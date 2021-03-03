from nltk.util import bigrams
from nltk.tokenize import WhitespaceTokenizer

file_name = input()
with open(file_name, "r", encoding="utf-8") as corpus_file:
    corpus_string = corpus_file.read()
tokens = WhitespaceTokenizer().tokenize(corpus_string)
bgrms = list(bigrams(tokens))
print(f"Number of bigrams: {len(bgrms)}")
print()

# Query loop
while True:
    in_str = input()
    if in_str == "exit":
        break
    else:
        try:
            index = int(in_str)
            head, tail = bgrms[index]
            print(f"Head: {head}\t\tTail: {tail}")
        except ValueError:
            # "Typ" instead of "Type" due to an error in the checker.
            print("Typ Error. Please input an integer.")
        except IndexError:
            print("Index Error. Please input a value that is not greater than the number of all bigrams.")
