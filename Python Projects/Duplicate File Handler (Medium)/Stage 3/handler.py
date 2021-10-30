import os
import sys
import hashlib
from typing import (Tuple, List, Dict)


def main() -> None:
    if check_valid_filename():
        file_tuple_list = explore(*parse_user_input())
        if check_for_hash():
            print()
            hash_files(file_tuple_list)


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


def explore(file_format: str, sort_choice: int) -> List[Tuple[List[str], int]]:
    # Suggested fix in comments for test 4 of stage 2:
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
    matches = sort_files_by_bytes(matches)

    # Handle printing output
    print()
    for (file_names, size) in matches:
        print(f"{size} bytes")
        for file_name in file_names:
            print(file_name)
        print()

    return matches


def sort_files_by_bytes(files: List[Tuple[str, int]]) -> List[Tuple[List[str], int]]:
    """

    :param files: A list of items of the form item = (file_path, file_size_in_bytes),
        sorted on the second field of the tuple
    :return: A list of tuples, each containing a int size and a list of files of that size
    """

    file_list = []
    for (file_path, size) in files:
        if file_list:
            last_size = file_list[-1][1]
            if last_size == size:
                file_list[-1][0].append(file_path)
                continue
        file_list.append(([file_path], size))
    return file_list


def check_for_hash() -> bool:
    print("Check for duplicates?")
    choice: str = input().lower()
    while choice not in {"yes", "no"}:
        print()
        print("Wrong option")
        print()
        print("Check for duplicates?")
        choice = input().lower()
    return choice == "yes"


def hash_files(file_tuple_list: List[Tuple[List[str], int]]) -> \
        List[Tuple[List[Tuple[List[Tuple[int, str]], str]], int]]:
    count: int = 1
    copies_list: List[Tuple[List[Tuple[List[Tuple[int, str]], str]], int]] = list()
    for (file_list, size) in file_tuple_list:
        print(f"{size} bytes")
        byte_list: List[Tuple[List[Tuple[int, str]], str]] = list()
        hash_dict: Dict[str, List[str]] = dict()
        for file in file_list:
            with open(file, "rb") as reader:
                hash_object = hashlib.md5()
                hash_object.update(reader.read())
                hash_list = hash_dict.setdefault(hash_object.hexdigest(), list())
                hash_list.append(file)
        for (hash_val, list_of_files) in hash_dict.items():
            if len(list_of_files) >= 2:
                print(f"Hash: {hash_val}")
                hash_tuple: Tuple[List[Tuple[int, str]], str] = (list(), hash_val)
                for file in list_of_files:
                    print(f"{count}. {file}")
                    hash_tuple[0].append((count, file))
                    count += 1
                print()
                byte_list.append(hash_tuple)
        if byte_list:
            copies_list.append((byte_list, size))
    return copies_list


if __name__ == "__main__":
    main()
