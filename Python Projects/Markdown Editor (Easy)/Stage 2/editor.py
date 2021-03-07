def formatter():
    formatters = set(("plain bold italic link inline-code header "
                      "ordered-list unordered-list line-break").split())
    special_commands = {"!help", "!done"}
    while True:
        choice = input("- choose a formatter: ")
        if choice not in formatters and choice not in special_commands:
            print("Unknown formatting type or command.")
            print("Please try again")
        elif choice in formatters:
            continue
        elif choice in special_commands:
            if choice == "!help":
                print("Available formatters: " + " ".join(formatters))
                print("Special commands: " + " ".join(special_commands))
            elif choice == "!done":
                break


formatter()
