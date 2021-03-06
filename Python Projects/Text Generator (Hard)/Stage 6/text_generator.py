from collections import Counter
from random import choices
from random import choice

from nltk.util import trigrams
from nltk.tokenize import WhitespaceTokenizer


def get_trigram_dict():
    file_name = input()
    with open(file_name, "r", encoding="utf-8") as corpus_file:
        corpus_string = corpus_file.read()
    tokens = WhitespaceTokenizer().tokenize(corpus_string)
    trgrms = list(trigrams(tokens))
    tgrm_dict = {}
    # Associate each head to list of all tails
    for *head, tail in trgrms:
        tgrm_dict.setdefault(tuple(head), list()).append(tail)
    # Replace list of tails with Counter of tails for each head
    for key, value in tgrm_dict.items():
        tgrm_dict[key] = Counter(value)
    return tgrm_dict


def generate_sentence(tgrm_dict):
    tail = choice(list(tgrm_dict.keys()))
    terminal_punctuation = (".", "!", "?")
    while not tail[0][0].isupper() or tail[0].endswith(terminal_punctuation):
        tail = choice(list(tgrm_dict.keys()))
    words = list(tail)
    while len(words) < 5 or not tail[1].endswith(terminal_punctuation):
        tails, weights = zip(*tgrm_dict[tail].most_common())
        next_word = choices(tails, weights)[0]
        if tail[1].endswith(terminal_punctuation):
            while not next_word[0].isupper():
                next_word = choices(tails, weights)[0]
        tail = (tail[1], next_word)
        words.append(next_word)
    return " ".join(words)


def query_loop():
    trgrm_dict = get_trigram_dict()
    for _ in range(10):
        print(generate_sentence(trgrm_dict))


query_loop()
