import json
import re


def check_stop_name_format(stop_name):
    return re.match(r"[A-Z]\w*\s(\w+\s)*(Road|Avenue|Boulevard|Street)\Z", stop_name)


def check_stop_type_format(stop_type):
    if not stop_type:
        return True
    else:
        return re.match(r"[SOF]\Z", stop_type)


def check_a_time_format(a_time):
    return re.match(r"(([0-1][0-9])|(2[0-3])):[0-5][0-9]\Z", a_time)


def check_key(key, value):
    if key == "stop_name":
        return check_stop_name_format(value)
    elif key == "stop_type":
        return check_stop_type_format(value)
    elif key == "a_time":
        return check_a_time_format(value)


error_dict = dict.fromkeys(["stop_name", "stop_type", "a_time"], 0)
schedule = json.loads(input())
total_errors = 0
for stop in schedule:
    for key in error_dict.keys():
        if not check_key(key, stop[key]):
            total_errors += 1
            error_dict[key] += 1
print(f"Format validation: {total_errors} errors")
print(f"stop_name: {error_dict['stop_name']}")
print(f"stop_type: {error_dict['stop_type']}")
print(f"a_time: {error_dict['a_time']}")
