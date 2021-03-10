def formatter():
    formatters = set(("plain bold italic link inline-code header "
                      "ordered-list unordered-list line-break").split())
    special_commands = {"!help", "!done"}
    lines = []
    while True:
        choice = input("- Choose a formatter: ")
        if choice not in formatters and choice not in special_commands:
            print("Unknown formatting type or command. Please try again")
        elif choice in formatters:
            # Set up preliminary variables
            if choice == "header":
                level = int(input("- Level: "))
                if not 1 <= level <= 6:
                    print("The level should be within the range of 1 to 6")
                    continue
            elif choice == "link":
                label = input("- Label: ")
                url = input("- URL: ")
            if choice not in {"link", "line-break", "ordered-list", "unordered-list"}:
                text = input("- Text: ")

            # Add appropriate text response for line
            if choice == "line-break":
                lines.append(line_break())
            elif choice == "plain":
                lines.append(plain(text))
            elif choice == "bold":
                lines.append(bold(text))
            elif choice == "italic":
                lines.append(italic(text))
            elif choice == "inline-code":
                lines.append(inline_code(text))
            elif choice == "header":
                lines.append(header(level, text))
            elif choice == "link":
                lines.append(link(label, url))

            # Print result up to this point
            print("".join(lines))
        elif choice in special_commands:
            if choice == "!help":
                print("Available formatters: " + " ".join(formatters))
                print("Special commands: " + " ".join(special_commands))
            elif choice == "!done":
                break


def plain(text):
    return text


def bold(text):
    return f"**{text}**"


def italic(text):
    return f"*{text}*"


def inline_code(text):
    return f"`{text}`"


def header(level, text):
    return f"{'#' * level} {text}\n"


def line_break():
    return "\n"


def link(label, url):
    return f"[{label}]({url})"


formatter()
