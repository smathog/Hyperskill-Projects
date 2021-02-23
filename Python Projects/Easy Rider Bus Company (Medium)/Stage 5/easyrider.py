import json


def validate_arrival_times(bus_schedule):
    bus_dict = {}
    bus_errors = {}
    for stop in bus_schedule:
        if stop["bus_id"] not in bus_dict:
            bus_dict[stop["bus_id"]] = stop["a_time"]
        else:
            if stop["bus_id"] in bus_errors:
                continue
            else:
                if stop["a_time"] > bus_dict[stop["bus_id"]]:
                    bus_dict[stop["bus_id"]] = stop["a_time"]
                else:
                    bus_errors[stop["bus_id"]] = stop["stop_name"]
    print("Arrival time test:")
    if bus_errors:
        for bus_id, station in bus_errors.items():
            print(f"bus_id line {bus_id}: wrong time on station {station}")
    else:
        print("OK")


bus_schedule = json.loads(input())
validate_arrival_times(bus_schedule)
