from collections import Counter
from random import choices
from random import choice

from nltk.util import bigrams
from nltk.tokenize import WhitespaceTokenizer


def get_bgrm_dict_and_tokens():
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
    return bgrm_dict, tokens


def get_tail(bgrm_dict, word):
    population, weights = zip(*bgrm_dict[word].most_common())
    return choices(population=population, weights=weights)[0]


def query_loop():
    bgrm_dict, tokens = get_bgrm_dict_and_tokens()
    for _ in range(10):
        tail = choice(tokens)
        words = [tail]
        for _ in range(9):
            tail = get_tail(bgrm_dict, tail)
            words.append(tail)
        print(" ".join(words))


query_loop()
