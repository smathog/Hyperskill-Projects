import requests

print("Input the URL:")
url = input()
print()
request = requests.get(url)
if not request or "content" not in request.json():
    print("Invalid quote resource!")
else:
    print(request.json()["content"])
