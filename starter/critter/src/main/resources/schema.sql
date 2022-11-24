CREATE TABLE if not exists customer (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    notes VARCHAR(5000),
    PRIMARY KEY (id)
);

CREATE TABLE if not exists pet (
    id BIGINT NOT NULL AUTO_INCREMENT,
    type VARCHAR(25) NOT NULL,
    name VARCHAR(128) NOT NULL,
    owner_id BIGINT NOT NULL,
    birth_date DATE NOT NULL,
    notes VARCHAR(5000),
    PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES customer(id)
);

CREATE TABLE if not exists schedule (
    id BIGINT NOT NULL AUTO_INCREMENT,
    schedule_date DATE NOT NULL,
    start_time time not null,
    end_time time not null,
    PRIMARY KEY (id)
);

CREATE TABLE if not exists employee (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE if not exists schedule_employee (
    schedule_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    FOREIGN KEY (schedule_id) REFERENCES schedule(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE if not exists schedule_pet (
    schedule_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    FOREIGN KEY (schedule_id) REFERENCES schedule(id),
    FOREIGN KEY (pet_id) REFERENCES pet(id)
);

CREATE TABLE if not exists schedule_activity (
    schedule_id BIGINT NOT NULL,
    activity VARCHAR(100) NOT NULL,
    FOREIGN KEY (schedule_id) REFERENCES schedule(id)
);

CREATE TABLE if not exists employee_skill (
    employee_id BIGINT NOT NULL,
    skill int NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE if not exists employee_availability (
    employee_id BIGINT NOT NULL,
    availability int NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    UNIQUE KEY emp_day (employee_id, availability)
);