--liquibase formatted SQL

-- changeset postgres:0

CREATE TABLE "Roles" (
                         "id" UUID NOT NULL UNIQUE,
                         "Role_name" VARCHAR(255),
                         PRIMARY KEY("id")
);


CREATE TABLE "Users" (
                         "Chat_id" BIGINT NOT NULL UNIQUE,
                         "Role_id" UUID NOT NULL UNIQUE,
                         PRIMARY KEY("Chat_id", "Role_id")
);


CREATE TABLE "Reports" (
                           "id" UUID NOT NULL UNIQUE,
                           "user_id" BIGINT NOT NULL UNIQUE,
                           "reports_body" VARCHAR(255),
                           "reports_date" DATE,
                           "working_hours" INTEGER,
                           "today_done" BOOLEAN,
                           PRIMARY KEY("id", "user_id")
);


CREATE TABLE "citates" (
                           "id" UUID NOT NULL UNIQUE,
                           "text" VARCHAR(255),
                           PRIMARY KEY("id")
);


ALTER TABLE "Roles"
    ADD FOREIGN KEY("id") REFERENCES "Users"("Role_id")
        ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE "Users"
    ADD FOREIGN KEY("Chat_id") REFERENCES "Reports"("user_id")
        ON UPDATE NO ACTION ON DELETE NO ACTION;