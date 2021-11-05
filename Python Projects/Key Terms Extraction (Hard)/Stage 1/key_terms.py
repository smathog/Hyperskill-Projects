from nltk.tokenize import word_tokenize
from lxml import etree
from collections import Counter

FILE_PATH: str = "news.xml"


def parse_news() -> None:
    root = etree.parse(FILE_PATH).getroot()
    for element in root[0]:
        header: str = element[0].text
        body: str = element[1].text

        # Count tokens in lowercased body...
        counter: Counter = Counter(word_tokenize(body.lower()))

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