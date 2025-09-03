-- Создание sequence
CREATE SEQUENCE slot_seq START WITH 100 INCREMENT BY 1;

-- Статусы слотов
--DROP TABLE slot_status;

CREATE TABLE slot_status
(
    n_status_Id number NOT NULL,
    vc_code     varchar2(100),
    vc_name     varchar2(1000),
    CONSTRAINT slot_status_pkey PRIMARY KEY (n_status_Id)
)
;

INSERT INTO slot_status(n_status_Id, vc_code, vc_name)
VALUES (1, 'С', 'Свободен');
INSERT INTO slot_status(n_status_Id, vc_code, vc_name)
VALUES (2, 'З', 'Занят');
INSERT INTO slot_status(n_status_Id, vc_code, vc_name)
VALUES (3, 'О', 'Обед');
INSERT INTO slot_status(n_status_Id, vc_code, vc_name)
VALUES (4, 'П', 'Пересменка');

-- DROP TABLE slot_role;

CREATE TABLE slot_role
(
    n_role_id number NOT NULL,
    vc_code   varchar2(100),
    vc_name   varchar2(1000),
    CONSTRAINT slot_role_pkey PRIMARY KEY (n_role_id)
)
;

INSERT INTO slot_role(n_role_id, vc_code, vc_name)
VALUES (1, 'A', 'Администратор');
INSERT INTO slot_role(n_role_id, vc_code, vc_name)
VALUES (2, 'D', 'Диспетчер');
INSERT INTO slot_role(n_role_id, vc_code, vc_name)
VALUES (3, 'C', 'Клиент');


-- Список нефтебаз, для которых есть расписание
CREATE TABLE slot_store
(
    n_slot_store_Id number NOT NULL,
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

-- Список клиентов, которые могут резервировать слоты
CREATE TABLE slot_client
(
    n_slot_client_Id number NOT NULL,
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

--  Список пользователей (новая таблица)
--  Table: client_user
CREATE TABLE client_user
(
    n_user_id      number NOT NULL,
    vc_first_name  varchar(100),
    vc_last_name   varchar(100),
    vc_second_name varchar2(100),
    vc_email       varchar2(100),
    d_start_date   date,
    d_end_date     date,
    n_client_id    number,
    vc_phone       varchar2(100),
    vc_login       varchar2(100) NOT NULL,
    vc_password    varchar2(1000) NOT NULL,
    n_role_id      number NOT NULL,
    CONSTRAINT client_user_pkey PRIMARY KEY (n_user_id)
);

INSERT INTO client_user(n_user_id, vc_first_name, vc_last_name, n_client_id,
                        vc_login, vc_password, vc_email, n_role_id)
VALUES (1, 'ADMIN', 'ADMIN', 1,
        'ADMIN',
        '8257a3811b9f6bb9d59dfb3931e220fa5574cee38fff551066caca1a50b1691ebdffa87f2d7213910e8bdbcf4d669c2756e57196667dd8f5e8af66971b2',
        'tixft@mail.ru', 1);

-- Список пунктов налива (новая таблица)
CREATE TABLE loading_point
(
    n_loading_point_id number NOT NULL,
    n_store_id         number NOT NULL,
    vc_code            varchar2(100),
    vc_name            varchar2(100),
    vc_comment         varchar2(400),
    CONSTRAINT loading_point_pkey PRIMARY KEY (n_loading_point_id)
);

CREATE VIEW v_loading_point AS
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

CREATE SEQUENCE loading_point_seq START WITH 1 INCREMENT BY 1;

-- Список слотов (новая таблица)
CREATE TABLE slot
(
    n_slot_id          number NOT NULL,
    d_date             date,
    n_loading_point_id number NOT NULL,
    n_client_id        number,
    d_start_time       TIMESTAMP,
    d_end_time         TIMESTAMP,
    n_status_Id        number NOT NULL,
    CONSTRAINT slot_pkey PRIMARY KEY (n_slot_id)
);

CREATE VIEW v_slot AS
SELECT slot.n_slot_id,
       slot.d_date,
       slot.d_start_TIME,
       slot.d_end_TIME,
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
CREATE TABLE slot_template
(
    n_slot_template_id number NOT NULL,
    n_loading_point_id number NOT NULL,
    d_start_time       TIMESTAMP,
    d_end_time         TIMESTAMP,
    n_status_Id        number NOT NULL,
    CONSTRAINT slot_template_pkey PRIMARY KEY (n_slot_template_id)
);


-- Список пользователей покупателей ( view)

CREATE VIEW v_client_user
AS
SELECT u.n_user_id,
       c.vc_name || ':' || u.vc_email as vc_info,
       u.vc_email
FROM v_client c
         JOIN client_user u on u.n_client_id = c.n_client_id
WHERE u.n_role_id = 3
;
