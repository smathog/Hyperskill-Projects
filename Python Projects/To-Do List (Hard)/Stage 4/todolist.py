from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Date
from sqlalchemy.orm import sessionmaker
from datetime import datetime, timedelta


Base = declarative_base()


class Table(Base):
    __tablename__ = "task"
    id = Column(Integer, primary_key=True)
    task = Column(String)
    deadline = Column(Date, default=datetime.today())

    def __repr__(self):
        return f"{self.task} {self.deadline}"


def yield_engine(file_name):
    engine = create_engine(fr"sqlite:///{file_name}?check_same_thread=False")
    Base.metadata.create_all(engine)
    return engine


def get_session(file_name):
    session = sessionmaker(bind=yield_engine(file_name))
    return session()


def print_today(session):
    print(f"Today {datetime.today().strftime('%d %B')}:")
    todo_list = session.query(Table).filter(Table.deadline == datetime.today().date()).all()
    print_indexed_list_no_date(todo_list, "Nothing to do!")


def print_week(session):
    today = datetime.today()
    current = datetime(today.year, today.month, today.day)
    week_later = current + timedelta(days=6)
    while current <= week_later:
        print(f"{current.strftime('%A %d %B')}:")
        todo_list = session.query(Table).filter(Table.deadline == current.date()).order_by(Table.deadline).all()
        print_indexed_list_no_date(todo_list, "Nothing to do!")
        if current != week_later:
            print()
        current = current + timedelta(days=1)


def print_all_tasks(session):
    print("All tasks:")
    todo_list = session.query(Table).order_by(Table.deadline).all()
    print_indexed_list(todo_list, "Nothing to do!")


def print_missed_tasks(session):
    print("Missed tasks:")
    missed_list = session.query(Table).filter(Table.deadline < datetime.today().date()).order_by(Table.deadline).all()
    print_indexed_list(missed_list, "Nothing is missed!")


def print_indexed_list(lst, empty_text):
    if lst:
        for index, task in enumerate(lst):
            print(f"{index + 1}. {task.task}. {task.deadline.strftime('%d %B')}")
    else:
        print(empty_text)


def print_indexed_list_no_date(lst, empty_text):
    if lst:
        for index, task in enumerate(lst):
            print(f"{index + 1}. {task.task}")
    else:
        print(empty_text)


def add_task(session):
    print("Enter task")
    task = input()
    print("Enter deadline")
    date_string = input()
    session.add(Table(task=task, deadline=datetime.strptime(date_string, "%Y-%m-%d")))
    session.commit()
    print("The task has been added!")


def delete_task(session):
    task_list = session.query(Table).order_by(Table.deadline).all()
    print("Choose the number of the task you want to delete:")
    print_indexed_list(task_list, "No tasks recorded!")
    num = int(input())
    target = task_list[num - 1]
    session.delete(target)
    session.commit()
    print("The task has been deleted!")


def menu(file_name):
    session = get_session(file_name)
    while True:
        print("1) Today's tasks")
        print("2) Week's tasks")
        print("3) All tasks")
        print("4) Missed tasks")
        print("5) Add task")
        print("6) Delete task")
        print("0) Exit")
        choice = int(input())
        if choice == 0:
            break
        elif choice == 1:
            print_today(session)
        elif choice == 2:
            print_week(session)
        elif choice == 3:
            print_all_tasks(session)
        elif choice == 4:
            print_missed_tasks(session)
        elif choice == 5:
            add_task(session)
        elif choice == 6:
            delete_task(session)
        else:
            print("This shouldn't happen!")
            exit()
        print()
    print("Bye!")


menu("todo.db")
