INSERT INTO slot_status( n_status_Id, vc_code, vc_name)
VALUES
    (1, 'С', 'Свободен' ),
    (2, 'З', 'Занят'),
    (3, 'О', 'Обед'),
    (4, 'П', 'Пересменка')
;

INSERT INTO slot_role( n_role_id, vc_code, vc_name)
VALUES
    (1, 'A', 'Администратор' ),
    (2, 'D', 'Диспетчер'),
    (3, 'C', 'Пользователь клиента')
;

INSERT INTO SI_V_SUBJECTS( N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID)
VALUES
    (1, '215', 'АО ТТК склад 215', 9 ),
    (2, '243', 'АО ТТК склад 243', 9),
    (3, '263', 'АО ТТК склад 263', 9),
    (4, '265', 'АО ТТК склад 265', 9),
    (5, 'ООО ПСМК', 'ООО ПСМК',6 ),
    (6, 'Каскад-Инфра ООО', 'Каскад-Инфра ООО', 6),
    (7, 'НИПИГОРМАШ НАО', 'НИПИГОРМАШ НАО',6),
    (8, 'Арктур', 'Арктур',6),
    (9, 'ООО Затундра', 'ООО Затундра',6),
    (10, 'ООО ЗСК', 'ООО ЗСК',6),
    (11, 'ООО ПСК', 'ООО ПСК',6),
    (12, 'Синтез-01', 'Синтез-0',6)
;

INSERT INTO slot_client
   SELECT N_SUBJECT_ID
   FROM SI_V_SUBJECTS
   WHERE N_SUBJ_TYPE_ID = 6
;

INSERT INTO slot_store
SELECT N_SUBJECT_ID
FROM SI_V_SUBJECTS
WHERE N_SUBJ_TYPE_ID = 9
;


INSERT INTO client_user(n_user_id, vc_last_name, vc_first_name, vc_second_name, n_client_id,
                        vc_login, vc_password, vc_email, n_role_id)
VALUES (1, 'ADMIN', 'ADMIN', 'ADMIN', 5,
        'ADMIN',
        '8257a3811b9f6bb9d59dfb3931e220fa5574cee38fff551066caca1a50b1691ebdffa87f2d7213910e8bdbcf4d669c2756e57196667dd8f5e8af66971b2',
         'tixft1@mail.ru', 1),
       (2, 'USER2', 'USER2', 'USER2', 6,
        'USER2',
        '8257a3811b9f6bb9d59dfb3931e220fa5574cee38fff551066caca1a50b1691ebdffa87f2d7213910e8bdbcf4d669c2756e57196667dd8f5e8af66971b2',
        'tixft2@mail.ru', 3),
       (3, 'USER3', 'USER3', 'USER3', 7,
        'USER3',
        '8257a3811b9f6bb9d59dfb3931e220fa5574cee38fff551066caca1a50b1691ebdffa87f2d7213910e8bdbcf4d669c2756e57196667dd8f5e8af66971b2',
        'tixft3@mail.ru', 3),
       (4, 'Петров', 'Михаил', 'Петрович', 8,
        'USER4',
        '8257a3811b9f6bb9d59dfb3931e220fa5574cee38fff551066caca1a50b1691ebdffa87f2d7213910e8bdbcf4d669c2756e57196667dd8f5e8af66971b2',
        'tixft@mail.ru', 3)
;

INSERT INTO loading_point( n_loading_point_id, n_store_id, vc_code, vc_name, vc_comment)
VALUES
    (1, 1, 'пн-1', 'пункт налива-1' , 'бензин, дизтопливо, тс' ),
    (2, 1, 'пн-2', 'пункт налива-2' , 'бензин, дизтопливо, тс' ),
    (3, 1, 'пн-3', 'пункт налива-3' , 'бензин, дизтопливо' ),
    (4, 2, 'пн-1', 'пункт налива-1' , 'бензин, дизтопливо' ),
    (5, 2, 'пн-2', 'пункт налива-2' , 'бензин, дизтопливо' ),
    (6, 3, 'пн-1', 'пункт налива-1' , 'бензин, дизтопливо, тс' ),
    (7, 3, 'пн-2', 'пункт налива-2' , 'бензин, дизтопливо' ),
    (8, 3, 'пн-3', 'пункт налива-3' , 'бензин, дизтопливо, тс' )
;


    INSERT INTO slot(  n_slot_id, d_date, n_loading_point_id, n_client_id, d_start_time, d_end_time, n_status_Id)
    VALUES
        (1, '2025-08-16', 1, 1, '14:30:00', '15:30:00' , 1 ),
        (2, '2025-08-16', 1, 2, '15:30:00', '16:30:00' , 2 ),
        (3, '2025-08-16', 1, 3, '15:30:00', '17:30:00' , 2 ),
        (4, '2025-08-16', 3, 5, '14:30:00', '15:30:00' , 3 ),
        (5, '2025-08-16', 3, 6, '15:30:00', '16:30:00' , 1 ),
        (6, '2025-08-16', 5, 4, '10:30:00', '11:30:00' , 1 ),
        (7, '2025-08-16', 5, 7, '11:30:00', '12:30:00' , 4 )
    ;


INSERT INTO slot_template( n_slot_template_id, n_loading_point_id, n_status_Id, d_start_time, d_end_time)
VALUES
    ( 1, 1, 1,'14:30:00', '15:30:00'),
    ( 2, 1, 1,'15:30:00', '16:30:00'  ),
    ( 3,1, 1,'15:30:00', '17:30:00'  ),
    ( 4,3, 4, '14:30:00', '15:30:00' ),
    ( 5,3, 3,'15:30:00', '16:30:00'  ),
    ( 6,5, 1, '10:30:00', '11:30:00'  ),
    ( 7,5, 1, '11:30:00', '12:30:00'  )
;
