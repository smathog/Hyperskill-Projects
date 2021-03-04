from collections import Counter

from nltk.util import bigrams
from nltk.tokenize import WhitespaceTokenizer

file_name = input()
with open(file_name, "r", encoding="utf-8") as corpus_file:
    corpus_string = corpus_file.read()
tokens = WhitespaceTokenizer().tokenize(corpus_string)
bgrms = list(bigrams(tokens))
bgrm_dict = {}
# Associate each head to list of all tails
for head, tail in bgrms:
    bgrm_dict.setdefault(head, list()).append(tail)
# Replace list of tails with Counter of tails for each head
for key, value in bgrm_dict.items():
    bgrm_dict[key] = Counter(value)

# Query loop
while True:
    in_str = input()
    if in_str == "exit":
        break
    else:
        print(f"Head: {in_str}")
        if in_str in bgrm_dict:
            for tail, count in bgrm_dict[in_str].most_common():
                print(f"Tail: {tail}\t\tCount: {count}")
        else:
            print("Key Error! The requested word is not in the model. Please input another word.")
        print()
