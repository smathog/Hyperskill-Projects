from itertools import combinations
from random import shuffle
from functools import total_ordering


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


domino_set = DominoSet()
max_p1 = max(domino_set.player_1_set)
max_p2 = max(domino_set.player_2_set)
max_overall = max(max_p1, max_p2)
if max_p1 == max_overall:
    domino_set.player_1_set.remove(max_p1)
    status = "computer"
else:
    domino_set.player_2_set.remove(max_p2)
    status = "player"
print("=" * 70)
print(f"Stock size: {len(domino_set.stock_set)}")
print(f"Computer pieces: {len(domino_set.player_2_set)}")
print()
print(max_overall)
print()
print("Your pieces: ")
for index, piece in enumerate(list(domino_set.player_1_set)):
    print(f"{index + 1}: " + piece.__str__())
if status == "player":
    print("Status: It's your turn to make a move. Enter your command.")
else:
    print("Status: Computer is about to make a move. Press Enter to continue...")
