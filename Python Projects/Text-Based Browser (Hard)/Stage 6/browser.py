import argparse
import os
import requests
from collections import deque
from CustomBeautifulSoup import CustomBeautifulSoup
from colorama import Fore, Style


def get_directory():
    parser = argparse.ArgumentParser(description="Browse via text command")
    parser.add_argument("directory", metavar="dir")
    args = parser.parse_args()
    return args.directory


def get_valid_url(url):
    try:
        if not url.startswith("https://"):
            url = "https://" + url
        requests.get(url)
        return url
    except requests.exceptions.ConnectionError:
        print("Error: incorrect URL")


def print_and_save(url, directory):
    request = requests.get(url)
    s = CustomBeautifulSoup(request.content, "html.parser")
    print_text(s.get_text("\n", strip=True))

    # Trim down url name to appropriate filename
    if "www." in url:
        url = url[url.index("www.") + 4:url.rindex(".")]
    elif "https://" in url:
        url = url[url.index("https://") + 8:url.rindex(".")]
    else:
        url = url[:url.rindex(".")]

    # Write to file and return saved file name
    with open(directory + "\\" + url, "w") as file:
        file.write(s.get_text())
    return url


def print_text(text):
    lines = text.split("\n")
    for line in lines:
        if line.startswith("CUSTOM_PRINT_BLUE"):
            print(Fore.BLUE + line[len("CUSTOM_PRINT_BLUE"):])
            print(Style.RESET_ALL)
        else:
            print(line)


def run():
    directory = get_directory()
    if not os.path.exists(directory):
        os.mkdir(directory)

    files = deque()
    files_set = set()
    current = None

    def read_file(file_name):
        with open(directory + "\\" + file_name, "r") as file:
            for line in file:
                if line.startswith("CUSTOM_PRINT_BLUE"):
                    print(Fore.BLUE + line[len("CUSTOM_PRINT_BLUE"):])
                    print(Style.RESET_ALL)
                else:
                    print(line)

    while True:
        choice = input()
        if choice == "exit":
            break
        elif choice == "back":
            top = files.pop()
            if top == current:
                top = files.pop()
            read_file(top)
        elif choice in files_set:
            read_file(choice)
            break
        else:
            choice = get_valid_url(choice)
            if choice is not None:
                filename = print_and_save(choice, directory)
                files.append(filename)
                files_set.add(filename)
                current = filename
            else:
                if len(files) == 0:
                    continue
                else:
                    break


run()
