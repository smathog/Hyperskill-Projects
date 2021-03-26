class Applicant:
    def __init__(self, first_name, last_name, gpa, first, second, third):
        self.first_name = first_name
        self.last_name = last_name
        self.gpa = float(gpa)
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

    def __str__(self):
        return f"{self.first_name} {self.last_name} {self.gpa}"

    def __hash__(self):
        return hash((self.first_name, self.last_name, self.gpa, self.first, self.second, self.third))

    def __eq__(self, other):
        if not isinstance(other, type(self)):
            return NotImplemented
        else:
            return (other.first_name == self.first_name and
                    other.last_name == self.last_name and
                    other.gpa == self.gpa and
                    other.first == self.first and
                    other.second == self.second and
                    other.third == self.third)


class Department:
    def __init__(self, name, num_accept):
        self.name = name
        self.num_accept = num_accept
        self.accepted = []

    # Select candidates for given rank; returns the general list of applicants without those accepted
    # NOTE: applicants must be pre-sorted by gpa for this to work
    def accept(self, applicants, rank):
        num_spaces = self.num_accept - len(self.accepted)
        # If department is already full, just ignore
        if num_spaces == 0:
            return applicants
        else:
            # Get list of applicants who put this department at given rank
            dept_applicants = [x for x in applicants if x.choice_n(rank) == self.name]
            if len(dept_applicants) <= num_spaces:
                self.accepted.extend(dept_applicants)
                to_remove = dept_applicants
            else:
                to_remove = dept_applicants[:num_spaces]
                self.accepted.extend(to_remove)
            # Remove accepted applicants from general applicants list
            return [x for x in applicants if x not in set(to_remove)]

    def __str__(self):
        str_list = [self.name]
        str_list.extend([x.__str__() for x in sorted(self.accepted, key=lambda x: (-x.gpa, x.name()))])
        return '\n'.join(str_list)


n_accept = int(input())
dept_list = ["Biotech", "Chemistry", "Engineering", "Mathematics", "Physics"]
dept_list = [Department(name, n_accept) for name in dept_list]
applicants_list = []
with open("applicants.txt", "r") as file:
    for line in file:
        applicants_list.append(Applicant(*line.split()))
applicants_list = sorted(applicants_list, key=lambda x: (-x.gpa, x.name()))
for i in range(3):
    for dept in dept_list:
        applicants_list = dept.accept(applicants_list, i + 1)
for dept in dept_list:
    print(dept)
    print()
