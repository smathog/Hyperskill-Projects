from nltk.tokenize import WhitespaceTokenizer

file_name = input()
with open(file_name, "r", encoding="utf-8") as corpus_file:
    corpus_string = corpus_file.read()
tokens = WhitespaceTokenizer().tokenize(corpus_string)
unique_tokens = set(tokens)
print("Corpus statistics")
print(f"All tokens: {len(tokens)}")
print(f"Unique tokens: {len(unique_tokens)}")
print()

# Query loop
while True:
    in_str = input()
    if in_str == "exit":
        break
    else:
        try:
            index = int(in_str)
            print(tokens[index])
        except ValueError:
            print("Type Error. Please input an integer")
        except IndexError:
            print("Index Error. Please input an integer that is in the range of the corpus.")
