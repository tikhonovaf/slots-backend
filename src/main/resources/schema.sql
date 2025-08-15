-- Table: public.client_user

CREATE TABLE IF NOT EXISTS client
(
    id bigint NOT NULL,
    name varchar(100),
    CONSTRAINT client_pkey PRIMARY KEY (id)
    )
;

-- Table: public.client_user

CREATE TABLE IF NOT EXISTS client_user (
   id bigint NOT NULL,
   first_name varchar(100),
    last_name varchar(100),
    second_name varchar(100),
    email varchar(100),
    start_date date,
    end_date date,
    client_id bigint NOT NULL,
    phone varchar(100),
    login varchar(100),
    password varchar(1000),
    CONSTRAINT client_user_pkey PRIMARY KEY (id)
    );

