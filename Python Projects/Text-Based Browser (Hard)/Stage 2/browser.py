import argparse
import os

nytimes_com = '''
This New Liquid Is Magnetic, and Mesmerizing

Scientists have created “soft” magnets that can flow 
and change shape, and that could be a boon to medicine 
and robotics. (Source: New York Times)


Most Wikipedia Profiles Are of Men. This Scientist Is Changing That.

Jessica Wade has added nearly 700 Wikipedia biographies for
 important female and minority scientists in less than two 
 years.

'''

bloomberg_com = '''
The Space Race: From Apollo 11 to Elon Musk

It's 50 years since the world was gripped by historic images
 of Apollo 11, and Neil Armstrong -- the first man to walk 
 on the moon. It was the height of the Cold War, and the charts
 were filled with David Bowie's Space Oddity, and Creedence's 
 Bad Moon Rising. The world is a very different place than 
 it was 5 decades ago. But how has the space race changed since
 the summer of '69? (Source: Bloomberg)


Twitter CEO Jack Dorsey Gives Talk at Apple Headquarters

Twitter and Square Chief Executive Officer Jack Dorsey 
 addressed Apple Inc. employees at the iPhone maker’s headquarters
 Tuesday, a signal of the strong ties between the Silicon Valley giants.
'''

KNOWN_URLS = {"nytimes.com": nytimes_com, "bloomberg.com": bloomberg_com}


def get_directory():
    parser = argparse.ArgumentParser(description="Browse via text command")
    parser.add_argument("directory", metavar="dir")
    args = parser.parse_args()
    return args.directory


def get_valid_url(url):
    if "." in url:
        if url in KNOWN_URLS:
            return url
        else:
            print("Error: unknown URL")
    else:
        print("Error: incorrect URL")


def print_and_save(url, directory):
    print(KNOWN_URLS[url])
    with open(directory + "\\" + url[:url.index(".")], "w") as file:
        file.write(KNOWN_URLS[url])
    return url[:url.index(".")]


def run():
    directory = get_directory()
    if not os.path.exists(directory):
        os.mkdir(directory)

    files = []
    while True:
        choice = input()
        if choice == "exit":
            break
        elif choice in files:
            with open(directory + "\\" + choice, "r") as file:
                for line in file:
                    print(line + "\n")
            break
        else:
            if get_valid_url(choice) is not None:
                files.append(print_and_save(choice, directory))
            else:
                if len(files) == 0:
                    continue
                else:
                    break


run()
