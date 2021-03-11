import requests
import string
from bs4 import BeautifulSoup


def get_article_urls():
    r = requests.get("https://www.nature.com/nature/articles")
    article_soup = BeautifulSoup(r.content, "html.parser")
    span_generator = (tag for tag in article_soup.find_all("span", class_="c-meta__type") if tag.text == "News")
    parent_generator = (tag.findParent("div", class_="c-card__body") for tag in span_generator)
    return [f"https://www.nature.com{link.a.get('href')}" for link in parent_generator]


def save_article(url):
    r = requests.get(url)
    article_soup = BeautifulSoup(r.content, "html.parser")
    title = article_soup.find("h1", class_="article-item__title").text.strip()
    title = "_".join(title.translate(str.maketrans("", "", string.punctuation)).split())
    contents = article_soup.find("div", class_="article__body").text.strip()
    with open(f"{title}.txt", "w") as file:
        file.write(contents)


for url in get_article_urls():
    save_article(url)
