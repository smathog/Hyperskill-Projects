import os
import sys
import hashlib
from typing import (Tuple, List, Set, Dict)


def main() -> None:
    if check_valid_filename():
        file_tuple_list = explore(*parse_user_input())
        if check_for_hash():
            print()
            duplicates: List[str] = hash_files(file_tuple_list)
            if check_for_delete():
                freed: int = kill_the_spare(duplicates, parse_deletion_requests(len(duplicates)))
                print(f"Total freed up space: {freed} bytes")


def parse_user_input() -> (str, int):
    print("Enter file format: ")
    file_format: str = input()
    # Handle case somebody forgot the . on extension...
    if file_format and not file_format.startswith("."):
        file_format = "." + file_format
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
    return get_response("Check for duplicates?")


def hash_files(file_tuple_list: List[Tuple[List[str], int]]) -> List[str]:
    copies_list: List[str] = list()
    for (file_list, size) in file_tuple_list:
        print(f"{size} bytes")
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
                for file in list_of_files:
                    copies_list.append(file)
                    print(f"{len(copies_list)}. {file}")
                print()
    return copies_list


def get_response(question: str) -> bool:
    """

    :param question: The question to be asked
    :return: bool true/false once valid answer provided to question
    """

    while True:
        print(question)
        response: str = input().lower()
        print()
        if response not in {"yes", "no"}:
            print("Wrong option")
            print()
            continue
        else:
            break
    return response == "yes"


def check_for_delete() -> bool:
    return get_response("Delete files?")


def parse_deletion_requests(size: int) -> Set[int]:
    """

    :param size: The size of the list of copies
    :return: A validated set of requested deletions
    """

    while True:
        print("Enter file numbers to delete: ")
        error_flag = False
        delete_set: Set[int] = set()
        try:
            for element in input().split(" "):
                if 1 <= int(element) <= size and (int(element) - 1) not in delete_set:
                    delete_set.add(int(element) - 1)
                else:
                    error_flag = True
                    break
        except ValueError:
            error_flag = True
        if error_flag:
            print()
            print("Wrong option.")
            print()
            continue
        else:
            print()
            return delete_set


def kill_the_spare(duplicate_list: List[str], to_kill: Set[int]) -> int:
    """

    :param duplicate_list: List of file names that are duplicates
    :param to_kill: Indexes from duplicate_list to kill
    :return: int number of bytes freed
    """

    total_freed: int = 0
    for index in to_kill:
        target = duplicate_list[index]
        total_freed += os.path.getsize(target)
        os.remove(target)
    return total_freed


if __name__ == "__main__":
    main()
