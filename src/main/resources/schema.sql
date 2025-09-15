SET
MODE Oracle;
CREATE TABLE IF NOT EXISTS slot_role
(
    n_role_id
    bigint
    NOT
    NULL,
    vc_code
    varchar
(
    100
),
    vc_name varchar
(
    1000
)
    )
;

CREATE TABLE IF NOT EXISTS slot_status
(
    n_status_Id
    bigint
    NOT
    NULL,
    vc_code
    varchar
(
    100
),
    vc_name varchar
(
    1000
)
    )
;


--  Таблица для эмуляции таблоицы из Mercury_GT
CREATE TABLE IF NOT EXISTS SI_V_SUBJECTS
(
    N_SUBJECT_ID
    bigint
    NOT
    NULL,
    vc_code
    varchar
(
    100
),
    vc_name varchar
(
    1000
),
    N_SUBJ_TYPE_ID bigint,
    CONSTRAINT SI_V_SUBJECTS_pkey PRIMARY KEY
(
    N_SUBJECT_ID
)
    )
;

-- Список клиентов, которые могут резервировать слоты
CREATE TABLE slot_client
(
    n_slot_client_Id bigint NOT NULL,
    CONSTRAINT slot_client_pkey PRIMARY KEY (n_slot_client_Id)
);

--  VIEW  со списком отобранных клиентов
CREATE VIEW v_client AS
SELECT SC.n_slot_client_Id AS n_client_id,
       S.vc_code,
       S.vc_name
FROM slot_client sc
         JOIN SI_V_SUBJECTS S ON SC.n_slot_client_Id = S.N_SUBJECT_ID
;


-- Список нефтебаз, для которых есть расписание
CREATE TABLE slot_store
(
    n_slot_store_Id bigint NOT NULL,
    CONSTRAINT slot_store_pkey PRIMARY KEY (n_slot_store_Id)
);

--  VIEW  со списком отобранных нефтебаз
CREATE VIEW v_store AS
SELECT SS.n_slot_store_Id AS n_store_id,
       S.vc_code,
       S.vc_name
FROM slot_store ss
         JOIN SI_V_SUBJECTS S ON SS.n_slot_store_Id = S.N_SUBJECT_ID
;

--  Список пользователей (новая таблица)
--  Table: client_user
CREATE TABLE IF NOT EXISTS client_user
(
    n_user_id
    bigint
    NOT
    NULL,
    vc_first_name
    varchar
(
    100
),
    vc_last_name varchar
(
    100
),
    vc_second_name varchar
(
    100
),
    vc_email varchar
(
    100
),
    d_start_date date,
    d_end_date date,
    n_client_id bigint,
    vc_phone varchar
(
    100
) ,
    vc_login varchar
(
    100
) NOT NULL,
    vc_password varchar
(
    1000
) NOT NULL,
    n_role_id bigint NOT NULL,
    CONSTRAINT client_user_pkey PRIMARY KEY
(
    n_user_id
)
    );

-- Список пунктов налива (новая таблица)
CREATE TABLE IF NOT EXISTS loading_point
(
    n_loading_point_id
    bigint
    NOT
    NULL,
    n_store_id
    bigint
    NOT
    NULL,
    vc_code
    varchar
(
    100
),
    vc_name varchar
(
    100
),
    vc_comment varchar
(
    400
),
    CONSTRAINT loading_point_pkey PRIMARY KEY
(
    n_loading_point_id
)
    );

CREATE VIEW IF NOT EXISTS v_loading_point AS
SELECT lp.n_loading_point_id,
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
CREATE TABLE IF NOT EXISTS slot
(
    n_slot_id
    bigint
    NOT
    NULL,
    d_date
    date,
    n_loading_point_id
    bigint
    NOT
    NULL,
    n_client_id
    bigint,
    d_start_time
    time,
    d_end_time
    time,
    n_status_Id
    bigint
    NOT
    NULL,
    CONSTRAINT
    slot_pkey
    PRIMARY
    KEY
(
    n_slot_id
)
    );

CREATE VIEW IF NOT EXISTS v_slot AS
SELECT slot.n_slot_id,
       slot.d_date,
       slot.d_start_time,
       slot.d_end_time,
       slot.n_loading_point_id,
       lp.vc_code    as vc_Loading_Point_Code,
       lp.vc_name    as vc_Loading_Point_Name,
       lp.vc_comment as vc_Loading_Point_Comment,
       lp.n_store_id,
       lp.vc_store_code,
       lp.vc_store_name,
       slot.n_client_id,
       c.vc_code     as vc_client_code,
       c.vc_name     as vc_client_name,
       slot.n_status_Id,
       ss.vc_code    as vc_status_code,
       ss.vc_name    as vc_status_name
FROM slot
         JOIN v_loading_point lp on lp.n_loading_point_id = slot.n_loading_point_id
         JOIN slot_status ss on ss.n_status_Id = slot.n_status_Id
         LEFT JOIN v_client c on c.n_client_id = slot.n_client_id
;

-- Список шаблонов слотов (новая таблица)
CREATE TABLE IF NOT EXISTS slot_template
(
    n_slot_template_id
    bigint
    NOT
    NULL,
    n_loading_point_id
    bigint
    NOT
    NULL,
    d_start_time
    time,
    d_end_time
    time,
    n_status_Id
    bigint
    NOT
    NULL,
    CONSTRAINT
    slot_template_pkey
    PRIMARY
    KEY
(
    n_slot_template_id
)
    );


-- Список пользователей покупателей ( view)

CREATE VIEW IF NOT EXISTS v_client_user
AS
SELECT u.n_user_id,
       c.vc_name || ':' || u.vc_email as vc_info,
       u.vc_email
FROM client_user u
         JOIN v_client c on u.n_client_id = c.n_client_id
WHERE u.n_role_id = 3
;


-- Список пользователей покупателей ( view)

CREATE VIEW v_client_user_detail
AS
SELECT u.n_user_id,
       u.n_client_id,
       c.vc_code as vc_client_code,
       vc_first_name,
       u.vc_last_name,
       u.vc_second_name,
       u.vc_email,
       u.vc_phone,
       u.vc_login,
       u.n_role_id,
       r.vc_code as vc_role_code,
       r.vc_name as vc_role_name
FROM client_user u
         LEFT JOIN v_client c on u.n_client_id = c.n_client_id
         JOIN slot_role r on r.n_role_id = u.n_role_id
;