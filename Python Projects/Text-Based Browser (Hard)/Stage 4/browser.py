import argparse
import os
import requests
from collections import deque


def get_directory():
    parser = argparse.ArgumentParser(description="Browse via text command")
    parser.add_argument("directory", metavar="dir")
    args = parser.parse_args()
    return args.directory


def get_valid_url(url):
    if "." in url:
        return url
    else:
        print("Error: incorrect URL")


def print_and_save(url, directory):
    if not url.startswith("https://"):
        request = requests.get("https://" + url)
    else:
        request = requests.get(url)
    print(request.text)
    with open(directory + "\\" + url[:url.rindex(".")], "w") as file:
        file.write(request.text)
    return url[:url.index(".")]


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
            if get_valid_url(choice) is not None:
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
