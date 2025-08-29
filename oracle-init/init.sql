-- Статусы слотов

CREATE TABLE slot_status
(
    n_status_Id number NOT NULL,
    vc_code varchar2(100),
    vc_name varchar2(1000)
)
;
INSERT INTO slot_status( n_status_Id, vc_code, vc_name)
VALUES
    (1, 'С', 'Свободен' ),
    (2, 'З', 'Занят'),
    (3, 'О', 'Обед'),
    (4, 'П', 'Пересменка')
;