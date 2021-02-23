import json


# Checks that each bus has exactly one starting point and one final stop
def validate_schedule(bus_schedule):
    bus_dict = {}
    for stop in bus_schedule:
        bus_id = stop["bus_id"]
        if bus_id not in bus_dict:
            bus_dict[bus_id] = [None, None]
        if stop["stop_type"] == "S":
            if bus_dict[bus_id][0] is None:
                bus_dict[bus_id][0] = stop["stop_name"]
            else:
                print(f"{bus_id} has multiple starting points")
                return False
        elif stop["stop_type"] == "F":
            if bus_dict[bus_id][1] is None:
                bus_dict[bus_id][1] = stop["stop_name"]
            else:
                print(f"{bus_id} has multiple stopping points")
                return False
    for bus_id, stops in bus_dict.items():
        if stops[0] is None and stops[1] is None:
            print(f"There is no start or end stop for the line: {bus_id}")
            return False
        elif stops[0] is None:
            print(f"There is no start stop for the line: {bus_id}")
            return False
        elif stops[1] is None:
            print(f"There is no end stop for the line: {bus_id}")
            return False
    return True


def output_stop_info(bus_schedule):
    street_dict = {}
    transfer_stops = set()
    for stop in bus_schedule:
        if stop["stop_name"] not in street_dict:
            street_dict[stop["stop_name"]] = stop["bus_id"]
        else:
            if stop["bus_id"] != street_dict[stop["stop_name"]]:
                transfer_stops.add(stop["stop_name"])
    transfer_stops = sorted(transfer_stops)
    start_stops = sorted({stop["stop_name"] for stop in bus_schedule if stop["stop_type"] == "S"})
    finish_stops = sorted({stop["stop_name"] for stop in bus_schedule if stop["stop_type"] == "F"})
    print(f"Start stops: {len(start_stops)} {start_stops}")
    print(f"Transfer stops: {len(transfer_stops)} {transfer_stops}")
    print(f"Finish stops: {len(finish_stops)} {finish_stops}")


bus_schedule = json.loads(input())
if validate_schedule(bus_schedule):
    output_stop_info(bus_schedule)
