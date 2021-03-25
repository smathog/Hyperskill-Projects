num_applicants = int(input())
num_accept = int(input())
applicants = [input().split() for _ in range(num_applicants)]
accepted = sorted(applicants, reverse=True, key=lambda x: float(x[2]))[:num_accept]
print("Successful applicants:")
for applicant in accepted:
    print(applicant[0] + " " + applicant[1])
