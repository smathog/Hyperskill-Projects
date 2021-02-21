import json

error_dict = dict.fromkeys(["bus_id", "stop_id", "stop_name", "next_stop", "stop_type", "a_time"], 0)
schedule = json.loads(input())
for stop in schedule:
    for key, value in stop.items():
        flag = False
        if key == "bus_id":
            if not isinstance(value, int):
                flag = True
        elif key == "stop_id":
            if not isinstance(value, int):
                flag = True
        elif key == "stop_name":
            if not isinstance(value, str):
                flag = True
            elif not value:
                flag = True
        elif key == "next_stop":
            if not isinstance(value, int):
                flag = True
        elif key == "stop_type":
            if value is not None:
                if not isinstance(value, str):
                    flag = True
                elif len(value) > 1:
                    flag = True
        elif key == "a_time":
            if not isinstance(value, str):
                flag = True
            elif not value:
                flag = True
        if flag:
            error_dict[key] += 1
total_errors = sum(error_dict.values())
print(f"Type and required field validation: {total_errors} errors")
for key, value in error_dict.items():
    print(f"{key}: {value}")
