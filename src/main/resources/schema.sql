CREATE TABLE IF NOT EXISTS app_user (
    id uuid NOT NUll PRIMARY KEY,
    username varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS app_project (
    id uuid NOT NULL PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    created_date timestamp NOT NULL,
    updated_date timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS app_task (
    id uuid NOT NULL PRIMARY KEY,
    title varchar(255) NOT NULL,
    description varchar(255),
    status varchar(255) NOT NULL,
    project_id uuid NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT fk_project FOREIGN KEY(project_id) REFERENCES app_project(id),
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES app_user(id)
);