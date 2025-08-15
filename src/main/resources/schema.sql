-- Список покупателей (из view)

CREATE TABLE IF NOT EXISTS v_client
(
    n_client_id bigint NOT NULL,
    vc_code varchar(100),
    vc_name varchar(1000)
    )
;


CREATE TABLE IF NOT EXISTS v_store
(
    n_store_id bigint NOT NULL,
    vc_code varchar(100),
    vc_name varchar(1000)
    )
;

--  Список пользователей (новая таблица)
--  Table: client_user

CREATE TABLE IF NOT EXISTS client_user (
    n_user_id bigint NOT NULL,
    vc_first_name varchar(100),
    vc_last_name varchar(100),
    vc_second_name varchar(100),
    vc_email varchar(100),
    d_start_date date,
    d_end_date date,
    n_client_id bigint NOT NULL,
    vc_phone varchar(100),
    vc_login varchar(100),
    vc_password varchar(1000),
    CONSTRAINT client_user_pkey PRIMARY KEY (n_user_id)
    );

-- Список пунктов налива (новая таблица)
CREATE TABLE IF NOT EXISTS loading_point (
    n_loading_point_id bigint NOT NULL,
    n_store_id bigint NOT NULL,
    vc_code varchar(100),
    vc_name varchar(100),
    vc_comment varchar(400),
    CONSTRAINT loading_point_pkey PRIMARY KEY (n_loading_point_id)
    );

-- Список слотов (новая таблица)
CREATE TABLE IF NOT EXISTS slot (
     n_slot_id bigint NOT NULL,
     d_date date,
     n_loading_point_id bigint NOT NULL,
     n_client_id bigint NOT NULL,
     d_start_time time,
     d_end_time time,
     vc_status varchar(1),
    CONSTRAINT slot_pkey PRIMARY KEY (n_slot_id)
    );

