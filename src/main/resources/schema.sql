SET MODE Oracle;

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

 CREATE VIEW IF NOT EXISTS v_loading_point AS
    SELECT
        lp.n_loading_point_id,
         lp.vc_code,
         lp.vc_name,
         lp.vc_comment,
         lp.n_store_id,
         s.vc_code as vc_store_code,
         s.vc_name as vc_store_name
     FROM loading_point lp
     JOIN v_store s on lp.n_store_id = s.n_store_id
;

-- Создание sequence
CREATE SEQUENCE slot_seq START WITH 100 INCREMENT BY 1;

-- Список слотов (новая таблица)
CREATE TABLE IF NOT EXISTS slot (
     n_slot_id bigint NOT NULL,
     d_date date,
     n_loading_point_id bigint NOT NULL,
     n_client_id bigint ,
     d_start_time time,
     d_end_time time,
     vc_status varchar(1) NOT NULL,
    CONSTRAINT slot_pkey PRIMARY KEY (n_slot_id)
    );

 CREATE VIEW IF NOT EXISTS v_slot AS
 SELECT
     slot.n_slot_id,
     slot.d_date,
     slot.d_start_time,
     slot.d_end_time,
     slot.vc_status,
     slot.n_loading_point_id,
     lp.vc_code as vc_Loading_Point_Code,
     lp.vc_name as vc_Loading_Point_Name,
     lp.vc_comment as vc_Loading_Point_Comment,
     lp.n_store_id,
     lp.vc_store_code,
     lp.vc_store_name,
     slot.n_client_id,
     c.vc_code as vc_client_code,
     c.vc_name as vc_client_name
 FROM slot
     JOIN v_loading_point lp on lp.n_loading_point_id = slot.n_loading_point_id
     LEFT JOIN v_client c on c.n_client_id = slot.n_client_id
 ;

-- Список шаблонов слотов (новая таблица)
CREATE TABLE IF NOT EXISTS slot_template (
                                    n_slot_template_id bigint NOT NULL,
                                    n_loading_point_id bigint NOT NULL,
                                    d_start_time time,
                                    d_end_time time,
    CONSTRAINT slot_template_pkey PRIMARY KEY (n_slot_template_id)
    );
