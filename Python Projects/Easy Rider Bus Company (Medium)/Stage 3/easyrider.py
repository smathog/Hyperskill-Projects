import json

bus_dict = {}
bus_schedule = json.loads(input())
for stop in bus_schedule:
    if stop["bus_id"] in bus_dict:
        bus_dict[stop["bus_id"]] += 1
    else:
        bus_dict[stop["bus_id"]] = 1
print("Line names and number of stops:")
for bus_id, num_stops in bus_dict.items():
    print(f"bus_id: {bus_id}, stops: {num_stops}")
