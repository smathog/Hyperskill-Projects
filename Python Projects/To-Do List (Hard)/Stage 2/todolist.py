from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Date
from sqlalchemy.orm import sessionmaker
from datetime import datetime

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
    print("Today:")
    todo_list = session.query(Table).all()
    if todo_list:
        for index, task in enumerate(todo_list):
            print(f"{index + 1}) {task.task}")
    else:
        print("Nothing to do!")


def add_task(session):
    print("Enter task")
    task = input()
    session.add(Table(task=task))
    session.commit()
    print("The task has been added!")


def menu(file_name):
    session = get_session(file_name)
    while True:
        print("1) Today's tasks")
        print("2) Add task")
        print("0) Exit")
        choice = int(input())
        if choice == 0:
            break
        elif choice == 1:
            print_today(session)
        elif choice == 2:
            add_task(session)
        else:
            print("This shouldn't happen!")
            exit()
        print()
    print("Bye!")


menu("todo.db")
