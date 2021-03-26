class Applicant:
    def __init__(self, first_name, last_name, physics_exam, chemistry_exam, math_exam, cs_exam, first, second, third):
        self.first_name = first_name
        self.last_name = last_name
        self.scores = {"Physics": int(physics_exam), "Chemistry": int(chemistry_exam),
                       "Mathematics": int(math_exam), "Computer Science": int(cs_exam)}
        self.first = first
        self.second = second
        self.third = third

    def choice_n(self, n):
        if n == 1:
            return self.first
        elif n == 2:
            return self.second
        elif n == 3:
            return self.third

    def name(self):
        return f"{self.first_name} {self.last_name}"

    def __hash__(self):
        return hash((self.first_name, self.last_name, *self.scores.values(), self.first, self.second, self.third))

    def __eq__(self, other):
        if not isinstance(other, type(self)):
            return NotImplemented
        else:
            return (other.first_name == self.first_name and
                    other.last_name == self.last_name and
                    other.scores == self.scores and
                    other.first == self.first and
                    other.second == self.second and
                    other.third == self.third)


class Department:
    exam_lookup = {"Physics": "Physics", "Chemistry": "Chemistry", "Mathematics": "Mathematics",
                   "Engineering": "Computer Science", "Biotech": "Chemistry"}

    def __init__(self, name, num_accept):
        self.name = name
        self.num_accept = num_accept
        self.accepted = []

    # Select candidates for given rank; returns the general set of applicants without those accepted
    def accept(self, applicants, rank):
        num_spaces = self.num_accept - len(self.accepted)
        # If department is already full, just ignore
        if num_spaces == 0:
            return applicants
        else:
            # Get list of applicants who put this department at given rank
            dept_applicants = [x for x in applicants if x.choice_n(rank) == self.name]
            dept_applicants = sorted(dept_applicants,
                                     key=lambda x: (-x.scores[Department.exam_lookup[self.name]], x.name()))
            if len(dept_applicants) <= num_spaces:
                self.accepted.extend(dept_applicants)
                to_remove = dept_applicants
            else:
                to_remove = dept_applicants[:num_spaces]
                self.accepted.extend(to_remove)
            # Remove accepted applicants from general applicants list
            applicants.difference_update(to_remove)
            return applicants

    def __str__(self):
        str_list = [self.name]
        str_list.extend([x.name() + " " + str(x.scores[Department.exam_lookup[self.name]])
                         for x in
                         sorted(self.accepted, key=lambda x: (-x.scores[Department.exam_lookup[self.name]], x.name()))])
        return '\n'.join(str_list)


n_accept = int(input())
dept_list = ["Biotech", "Chemistry", "Engineering", "Mathematics", "Physics"]
dept_list = [Department(name, n_accept) for name in dept_list]
applicants_set = set()
with open("applicants.txt", "r") as file:
    for line in file:
        applicants_set.add(Applicant(*line.split()))
for i in range(3):
    for dept in dept_list:
        applicants_set = dept.accept(applicants_set, i + 1)
for dept in dept_list:
    print(dept)
    print()
