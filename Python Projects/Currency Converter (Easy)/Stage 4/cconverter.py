exchange_rates = {"RUB": 2.98, "ARS": 0.82, "HNL": 0.17, "AUD": 1.9622, "MAD": 0.208}
n = float(input())
for currency, rate in exchange_rates.items():
    print(f"I will get {n * rate} {currency} from the sale of {n} conicoins.")
