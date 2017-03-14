
# users schema

# --- !Ups

CREATE TABLE users (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tasks (
    id INTEGER NOT NULL AUTO_INCREMENT,
    title VARCHAR(2048) NOT NULL,
    content VARCHAR,
    deadline_at DATETIME,
    completed_at DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_tasks (
    user_id INTEGER NOT NULL,
    task_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, task_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (task_id) REFERENCES tasks(id)
);

# --- !Downs

DROP TABLE user_tasks;
DROP TABLE tasks;
DROP TABLE users;

