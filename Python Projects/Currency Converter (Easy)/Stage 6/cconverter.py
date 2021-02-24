import json
import requests


def generate_url(code):
    return f"http://www.floatrates.com/daily/{code.lower()}.json"


exchange_cache = {}
currency_code = input()
usd_info = json.loads(requests.get(generate_url("usd")).text)
eur_info = json.loads(requests.get(generate_url("eur")).text)
exchange_cache["usd"] = usd_info
exchange_cache["eur"] = eur_info
while True:
    return_currency_code = input()
    # Handle empty input
    if return_currency_code == "":
        break
    amount = float(input())
    print("Checking the cache…")
    if return_currency_code in exchange_cache:
        print("Oh! It is in the cache!")
    else:
        print("Sorry, but it is not in the cache!")
        currency_info = json.loads(requests.get(generate_url(return_currency_code)).text)
        exchange_cache[return_currency_code] = currency_info
    rate = exchange_cache[return_currency_code][currency_code.lower()]["inverseRate"]
    print(f"You received {rate * amount} {return_currency_code.upper()}")
