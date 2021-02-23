import json


def check_demand_stops(bus_schedule):
    on_demand_stops = {stop["stop_name"] for stop in bus_schedule if stop["stop_type"] == "O"}
    start_stops = {stop["stop_name"] for stop in bus_schedule if stop["stop_type"] == "S"}
    final_stops = {stop["stop_name"] for stop in bus_schedule if stop["stop_type"] == "F"}
    street_dict = {}
    transfer_stops = set()
    for stop in bus_schedule:
        if stop["stop_name"] not in street_dict:
            street_dict[stop["stop_name"]] = stop["bus_id"]
        else:
            if stop["bus_id"] != street_dict[stop["stop_name"]]:
                transfer_stops.add(stop["stop_name"])
    start_intersection = set.intersection(on_demand_stops, start_stops)
    final_intersection = set.intersection(on_demand_stops, final_stops)
    transfer_intersection = set.intersection(on_demand_stops, transfer_stops)
    wrong_stops = set.union(start_intersection, final_intersection, transfer_intersection)
    print("On demand stops test:")
    if wrong_stops:
        print(f"Wrong stop type: {sorted(wrong_stops)}")
    else:
        print("OK")


bus_schedule = json.loads(input())
check_demand_stops(bus_schedule)
