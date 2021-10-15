import os
import sys


def main() -> None:
    if len(sys.argv) == 2:
        directory = sys.argv[1]
        for (root, dirs, files) in os.walk(directory, topdown=True):
            for name in files:
                print(os.path.join(root, name))
    else:
        print("Directory is not specified")


if __name__ == "__main__":
    main()
