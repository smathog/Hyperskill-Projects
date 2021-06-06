from itertools import combinations
from random import shuffle
from random import choice
from random import randint
from functools import total_ordering
from collections import deque
from itertools import islice


@total_ordering
class Domino:
    def __init__(self, first, second):
        self.first = first
        self.second = second

    def __eq__(self, other):
        if not isinstance(other, type(self)):
            raise NotImplementedError
        return (self.first == other.first and self.second == other.second) or \
               (self.second == other.first and self.first == other.second)

    def __hash__(self):
        if self.first <= self.second:
            return hash((self.first, self.second))
        else:
            return hash((self.second, self.first))

    def __lt__(self, other):
        if not isinstance(other, type(self)):
            raise NotImplementedError
        self_list = sorted([self.first, self.second])
        other_list = sorted([other.first, other.second])
        if self_list[1] < other_list[1]:
            return True
        elif self_list[1] > other_list[1]:
            return False
        else:
            return self_list[0] < other_list[0]

    def __str__(self):
        return str([self.first, self.second])


class DominoSet:
    def __init__(self):
        self.dominoes = list(Domino(first, second) for first, second in combinations(range(7), 2))
        self.double_set = set(Domino(i, i) for i in range(7))
        self.dominoes.extend(self.double_set)
        index_list = list()
        index_list.extend(0 for _ in range(7))
        index_list.extend(1 for _ in range(7))
        index_list.extend(2 for _ in range(14))
        self.player_1_set = set()
        self.player_2_set = set()
        self.stock_set = set()
        while (not self.player_1_set.intersection(self.double_set) and
               not self.player_2_set.intersection(self.double_set)):
            shuffle(index_list)
            self.player_1_set = set()
            self.player_2_set = set()
            self.stock_set = set()
            for index, target in enumerate(index_list):
                if target == 0:
                    self.player_1_set.add(self.dominoes[index])
                elif target == 1:
                    self.player_2_set.add(self.dominoes[index])
                else:
                    self.stock_set.add(self.dominoes[index])


class Game:
    def __init__(self):
        self.domino_set = DominoSet()
        max_p1 = max(self.domino_set.player_1_set)
        max_p2 = max(self.domino_set.player_2_set)
        max_overall = max(max_p1, max_p2)
        if max_p1 == max_overall:
            self.domino_set.player_1_set.remove(max_p1)
            self.status = "computer"
        else:
            self.domino_set.player_2_set.remove(max_p2)
            self.status = "player"
        self.snake = deque()
        self.snake.append(max_overall)
        self.player_pieces = list(self.domino_set.player_1_set)
        self.computer_pieces = list(self.domino_set.player_2_set)
        self.finished = False

    def __str__(self):
        print_list = ["=" * 70, f"Stock size: {len(self.domino_set.stock_set)}",
                      f"Computer pieces: {len(self.computer_pieces)}", ""]
        if len(self.snake) <= 6:
            print_list.append("".join(i.__str__() for i in self.snake))
        else:
            front = "".join(i.__str__() for i in islice(self.snake, 0, 3))
            middle = "..."
            back = "".join(i.__str__() for i in islice(self.snake, len(self.snake) - 3, None))
            print_list.append(f"{front}{middle}{back}")
        print_list.append("")
        print_list.append("Your pieces: ")
        for index, piece in enumerate(self.player_pieces):
            print_list.append(f"{index + 1}: " + piece.__str__())
        print_list.append("")
        if self.status == "player":
            print_list.append("Status: It's your turn to make a move. Enter your command.")
        elif self.status == "computer":
            print_list.append("Status: Computer is about to make a move. Press Enter to continue...")
        else:
            print_list.append(self.status)
        return "\n".join(print_list)

    def round(self, command):
        if self.status == "computer":
            num = randint(-1, 1)
            if num == 0:
                if self.domino_set.stock_set:
                    piece = choice(tuple(self.domino_set.stock_set))
                    self.domino_set.stock_set.remove(piece)
                    self.computer_pieces.append(piece)
            else:
                piece = choice(self.computer_pieces)
                self.computer_pieces.remove(piece)
                if num == -1:
                    self.snake.appendleft(piece)
                else:
                    self.snake.append(piece)
            self.status = "player"
        else:
            if int(command) == 0:
                if self.domino_set.stock_set:
                    piece = choice(tuple(self.domino_set.stock_set))
                    self.domino_set.stock_set.remove(piece)
                    self.player_pieces.append(piece)
            else:
                if int(command) < 0:
                    self.snake.appendleft(self.player_pieces[(-1 * int(command)) - 1])
                    del self.player_pieces[(-1 * int(command)) - 1]
                else:
                    self.snake.append(self.player_pieces[int(command) - 1])
                    del self.player_pieces[int(command) - 1]
            self.status = "computer"
        is_done = self.is_finished()
        if is_done:
            self.status = is_done
            self.finished = True

    def is_finished(self):
        if not self.computer_pieces:
            return "Status: The game is over. The computer won!"
        if not self.player_pieces:
            return "Status: The game is over. You won!"
        if self.snake[0].first == self.snake[-1].second:
            count = 0
            for piece in self.snake:
                if piece.first == self.snake[0].first:
                    count += 1
                if piece.second == self.snake[0].first:
                    count += 1
            if count == 8:
                return "Status: The game is over. It's a draw!"
        return None


game = Game()
while not game.finished:
    print(game)
    response = input()
    if game.status == "player":
        while not response.isnumeric() or abs(int(response)) > len(game.player_pieces):
            print("Invalid input. Please try again.")
            response = input()
    game.round(response)
print(game)
