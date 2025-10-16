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
         'tixft1@mail.ru', 1)
;


