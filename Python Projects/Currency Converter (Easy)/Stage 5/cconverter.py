import json
import requests


def generate_url(code):
    return f"http://www.floatrates.com/daily/{code.lower()}.json"


currency_code = input()
currency_info = json.loads(requests.get(generate_url(currency_code)).text)
print(currency_info["usd"])
print(currency_info["eur"])
