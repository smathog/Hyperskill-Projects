import requests
from bs4 import BeautifulSoup


def prev_stage(url):
    request = requests.get(url, headers={"Accept-Language": "en-US,en;q=0.5"})
    imdb_soup = BeautifulSoup(request.content, "html.parser")
    title = imdb_soup.find("div", {"class": "title_wrapper"})
    summary = imdb_soup.find("div", {"class": "summary_text"})
    if not request or not title or not summary:
        print("Invalid movie page!")
    else:
        print({"title": title.h1.contents[0].strip(),
               "description": summary.text.strip()})


print("Input the URL:")
url = input()
print()
request = requests.get(url)
if request:
    with open("../stage4/source.html", "wb") as file:
        file.write(request.content)
        print("Content saved.")
else:
    print(f"The URL returned {request.status_code}!")
