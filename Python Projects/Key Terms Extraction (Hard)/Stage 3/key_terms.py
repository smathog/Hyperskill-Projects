import string

import nltk
from nltk.tokenize import word_tokenize
from nltk.stem import WordNetLemmatizer
from nltk.corpus import stopwords
from nltk import download
from lxml import etree
from collections import Counter

FILE_PATH: str = "news.xml"


def parse_news() -> None:
    # Enable if complaints about missing resources
    # download("wordnet")
    # download("stopwords")
    # download("averaged_perceptron_tagger")

    lemmatizer = WordNetLemmatizer()
    stop_words = set(stopwords.words("english"))
    punctuation = set(string.punctuation)
    root = etree.parse(FILE_PATH).getroot()
    for element in root[0]:
        header: str = element[0].text
        body: str = element[1].text

        # Get raw tokens of lowercased body
        tokens = word_tokenize(body.lower())

        # lemmatize tokens
        tokens = (lemmatizer.lemmatize(token) for token in tokens
                                   if token not in punctuation and lemmatizer.lemmatize(token) not in stop_words)

        # Count POS-NN tokens in body...
        counter: Counter = Counter(word[0][0] for word in (nltk.pos_tag([token]) for token in tokens)
                                   if word[0][1] == "NN")

        # Convert to sorted list of five most common tokens...
        tokens_list = sorted(counter.items(), key=lambda entry: (entry[1], entry[0]), reverse=True)[:5]

        # Print title and then tokens
        print(f"{header}:")
        print(" ".join(token[0] for token in tokens_list))
        print()


def main() -> None:
    parse_news()


if __name__ == "__main__":
    main()