import requests
import string
import os
import re
from bs4 import BeautifulSoup


def get_article_urls(page_num, article_type):
    r = requests.get(f"https://www.nature.com/nature/articles?searchType=journalSearch&sort=PubDate&page={page_num}")
    article_soup = BeautifulSoup(r.content, "html.parser")
    span_generator = (tag for tag in article_soup.find_all("span", class_="c-meta__type") if tag.text == article_type)
    parent_generator = (tag.findParent("div", class_="c-card__body") for tag in span_generator)
    return [f"https://www.nature.com{link.a.get('href')}" for link in parent_generator]


def save_article(url, directory):
    r = requests.get(url)
    article_soup = BeautifulSoup(r.content, "html.parser")
    title = article_soup.find("h1", class_="article-item__title").text.strip()
    title = "_".join(title.translate(str.maketrans("", "", string.punctuation)).split())
    contents = article_soup.find("div", class_=re.compile("article.*body")).text.strip()
    with open(directory + f"/{title}.txt", "w") as file:
        file.write(contents)


num = int(input())
article_type = input()
for page in range(1, num + 1):
    if not os.path.exists(f"Page_{page}"):
        os.mkdir(f"Page_{page}")
    for url in get_article_urls(page, article_type):
        save_article(url, f"Page_{page}")
