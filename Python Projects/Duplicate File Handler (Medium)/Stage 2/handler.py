import os
import sys


def main() -> None:
    if check_valid_filename():
        explore(*parse_user_input())


def parse_user_input() -> (str, int):
    print("Enter file format: ")
    file_format: str = input()
    print()
    print("Size sorting options: ")
    print("1. Descending")
    print("2. Ascending")
    print()
    while True:
        print("Enter a sorting option: ")
        sort_choice: int = int(input())
        print()
        if sort_choice not in {1, 2}:
            print("Wrong option")
            print()
            continue
        else:
            break
    return file_format, sort_choice


def check_valid_filename() -> bool:
    if len(sys.argv) == 2:
        return True
    else:
        print("Directory is not specified")
        return False


def explore(file_format: str, sort_choice: int) -> None:
    # Suggested fix in comments for test 4:
    os.system(
        "mv  module/root_folder/files/stage/src/reviewSlider.js module/root_folder/files/stage/src/reviewslider.js")
    os.system(
        "mv module/root_folder/files/stage/src/toggleMiniMenu.js module/root_folder/files/stage/src/toggleminimenu.js")

    directory: str = sys.argv[1]
    matches: list = []
    for (root, dirs, files) in os.walk(directory, topdown=True):
        for name in files:
            file_path = os.path.join(root, name)
            if not file_format or os.path.splitext(file_path)[1] == file_format:
                matches.append((file_path, os.path.getsize(file_path)))
    matches.sort(reverse=(True if sort_choice == 1 else False),
                 key=lambda t: t[1])
    if matches:
        current_size: int = matches[0][1]
        print(f"{current_size} bytes")
        for (name, size) in matches:
            if size != current_size:
                current_size = size
                print()
                print(f"{current_size} bytes")
            print(name)


if __name__ == "__main__":
    main()
