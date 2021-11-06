import string
from collections import Counter
from typing import (List)

import nltk
from nltk.tokenize import word_tokenize
from nltk.stem import WordNetLemmatizer
from nltk.corpus import stopwords
from nltk import download
from lxml import etree
from sklearn.feature_extraction.text import TfidfVectorizer

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
    header_list: List[str] = []
    nouns_lists: List[str] = []
    for element in root[0]:
        header: str = element[0].text
        body: str = element[1].text
        header_list.append(header)

        # Get raw tokens of lowercased body
        tokens = word_tokenize(body.lower())

        # lemmatize tokens
        tokens = (lemmatizer.lemmatize(token) for token in tokens
                  if token not in punctuation and lemmatizer.lemmatize(token) not in stop_words)

        # Get list of valid nouns, append to relevant list
        nouns_lists.append(" ".join(word[0][0] for word in (nltk.pos_tag([token]) for token in tokens)
                                    if word[0][1] == "NN"))

    # Generate TF-IDF metric matrix:
    vectorizer = TfidfVectorizer()
    matrix = vectorizer.fit_transform(nouns_lists)
    words_list = vectorizer.get_feature_names_out()
    (rows, cols) = matrix.shape
    for i in range(rows):
        title = header_list[i]

        # Get a sorted list of 5 best-match words...
        tokens_list = sorted(((word, index) for index, word in enumerate(words_list)),
                             key=lambda entry: (matrix[(i, entry[1])], entry[0]),
                             reverse=True)[:5]

        # Print title and then tokens
        print(f"{title}:")
        print(" ".join(token[0] for token in tokens_list))
        print()


def main() -> None:
    parse_news()


if __name__ == "__main__":
    main()
