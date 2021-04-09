import sqlite3
import argparse


def parse_args():
    parser = argparse.ArgumentParser(description="Load database file")
    parser.add_argument("db_name")
    parser.add_argument("--ingredients")
    parser.add_argument("--meals")
    args = parser.parse_args()
    return (args.db_name,
            set(args.ingredients.split(",")) if args.ingredients else None,
            set(args.meals.split(",")) if args.meals else None)


def db_init(db_name_str):
    connection = sqlite3.connect(db_name_str)
    cursor = connection.cursor()
    cursor.execute("CREATE TABLE IF NOT EXISTS meals( "
                   "meal_id INTEGER PRIMARY KEY, "
                   "meal_name TEXT NOT NULL,"
                   "UNIQUE(meal_name) ON CONFLICT REPLACE "
                   ");")
    cursor.execute("CREATE TABLE IF NOT EXISTS ingredients( "
                   "ingredient_id INTEGER PRIMARY KEY, "
                   "ingredient_name TEXT NOT NULL, "
                   "UNIQUE(ingredient_name) ON CONFLICT REPLACE "
                   ");")
    cursor.execute("CREATE TABLE IF NOT EXISTS measures( "
                   "measure_id INTEGER PRIMARY KEY, "
                   "measure_name TEXT, "
                   "UNIQUE(measure_name) ON CONFLICT REPLACE "
                   ");")
    cursor.execute("PRAGMA foreign_keys = ON;")
    cursor.execute("CREATE TABLE IF NOT EXISTS serve( "
                   "serve_id INTEGER PRIMARY KEY, "
                   "recipe_id INTEGER NOT NULL,"
                   "meal_id INTEGER NOT NULL, "
                   "FOREIGN KEY(meal_id) REFERENCES meals(meal_id), "
                   "FOREIGN KEY(recipe_id) REFERENCES recipes(recipe_id));")
    cursor.execute("CREATE TABLE IF NOT EXISTS recipes( "
                   "recipe_id INTEGER PRIMARY KEY, "
                   "recipe_name TEXT NOT NULL, "
                   "recipe_description TEXT "
                   ");")
    cursor.execute("CREATE TABLE IF NOT EXISTS quantity( "
                   "quantity_id INTEGER PRIMARY KEY, "
                   "measure_id INTEGER NOT NULL, "
                   "ingredient_id INTEGER NOT NULL, "
                   "quantity INTEGER NOT NULL, "
                   "recipe_id INTEGER NOT NULL, "
                   "FOREIGN KEY(measure_id) REFERENCES measures(measure_id), "
                   "FOREIGN KEY(ingredient_id) REFERENCES ingredients(ingredient_id), "
                   "FOREIGN KEY(recipe_id) REFERENCES recipes(recipe_id));")
    data = {"meals": ("breakfast", "brunch", "lunch", "supper"),
            "ingredients": ("milk", "cacao", "strawberry", "blueberry", "blackberry", "sugar"),
            "measures": ("ml", "g", "l", "cup", "tbsp", "tsp", "dsp", "")}
    column_dict = {"meals": "meal_name", "ingredients": "ingredient_name", "measures": "measure_name"}
    for key in data:
        col_name = column_dict[key]
        for value in data[key]:
            cursor.execute(f"INSERT INTO {key} ({col_name}) VALUES('{value}');")
    connection.commit()
    connection.close()


def add_recipes(db_name_str):
    connection = sqlite3.connect(db_name_str)
    cursor = connection.cursor()
    meals = cursor.execute("SELECT * FROM meals")
    meal_times = " ".join(f"{row[0]} {row[1]}) " for row in meals.fetchall())
    print("Pass the empty recipe name to exit.")
    while True:
        recipe_name = input("Recipe name: ")
        if recipe_name:
            # recipes table insert
            recipe_description = input("Recipe description: ")
            recipe_id = cursor.execute("INSERT INTO recipes(recipe_name, recipe_description) "
                                       f"VALUES('{recipe_name}', '{recipe_description}');").lastrowid

            # serve table insert
            print(meal_times)
            times = [int(i) for i in input("Enter proposed meals separated by a space: ").split(" ")]
            for i in times:
                cursor.execute("INSERT INTO serve(recipe_id, meal_id) "
                               f"VALUES({recipe_id}, {i});")

            # quantity table insert
            while True:
                line = input("Input quantity of ingredient <press enter to stop>: ")
                if line:
                    line = line.split()
                    quantity = int(line[0])
                    measure = line[1] if len(line) == 3 else ""
                    if measure:
                        measure_id = cursor.execute("SELECT measure_id "
                                                    "FROM measures "
                                                    f"WHERE measure_name LIKE '{measure}%';").fetchall()
                    else:
                        measure_id = cursor.execute("SELECT measure_id "
                                                    "FROM measures "
                                                    f"WHERE measure_name = ''").fetchall()
                    if measure_id and len(measure_id) == 1:
                        measure_id = measure_id[0][0]
                    else:
                        print("The measure is not conclusive!")
                        continue
                    ingredient = line[2] if len(line) == 3 else line[1]
                    ingredient_id = cursor.execute("SELECT ingredient_id "
                                                   "FROM ingredients "
                                                   f"WHERE ingredient_name LIKE '{ingredient}%';").fetchall()
                    if ingredient_id and len(ingredient_id) == 1:
                        ingredient_id = ingredient_id[0][0]
                    else:
                        print("The ingredient is not conclusive!")
                        continue
                    cursor.execute("INSERT INTO quantity(measure_id, ingredient_id, quantity, recipe_id) "
                                   f"VALUES({measure_id}, {ingredient_id}, {quantity}, {recipe_id});")
                else:
                    break

            connection.commit()
        else:
            break
    connection.close()


def find_recipes(db_name_str, ingredients, meals):
    connection = sqlite3.connect(db_name_str)
    cursor = connection.cursor()
    meal_ids = {cursor.execute(f"SELECT meal_id FROM meals WHERE meal_name = '{meal}';").fetchone()[0] for meal in
                meals}
    recipes = cursor.execute("SELECT recipe_id, recipe_name FROM recipes;").fetchall()
    matches = []
    for recipe in recipes:
        recipe_id, recipe_name = recipe
        recipe_ingredient_ids = cursor.execute(f"SELECT ingredient_id FROM quantity WHERE recipe_id = {recipe_id};")
        recipe_ingredient_ids = recipe_ingredient_ids.fetchall()
        recipe_ingredients = set(cursor.execute(f"SELECT ingredient_name "
                                                f"FROM ingredients "
                                                f"WHERE ingredient_id = {j[0]};").fetchone()[0]
                                 for j in recipe_ingredient_ids)
        if recipe_ingredients.issuperset(ingredients):
            for meal_id in meal_ids:
                if cursor.execute(f"SELECT * FROM serve "
                                  f"WHERE meal_id = {meal_id} AND recipe_id = {recipe_id};").fetchone():
                    matches.append(recipe_name)
                    break
        else:
            continue
    if matches:
        print(f"Recipes selected for you: {', '.join(matches)}")
    else:
        print("There are no such recipes in the database.")


db, ingredient_set, meal_set = parse_args()
# If optional command line arguments
if ingredient_set and meal_set:
    find_recipes(db, ingredient_set, meal_set)
else:
    db_init(db)
    add_recipes(db)
