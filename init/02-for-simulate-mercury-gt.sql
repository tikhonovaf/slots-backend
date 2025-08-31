CREATE TABLE SI_V_SUBJECTS
(
    N_SUBJECT_ID number NOT NULL,
    vc_code varchar2(100),
    vc_name varchar2(1000),
    N_SUBJ_TYPE_ID number
)
;
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (1, '215', 'АО ТТК склад 215', 9);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (2, '243', 'АО ТТК склад 243', 9);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (3, '263', 'АО ТТК склад 263', 9);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (4, '265', 'АО ТТК склад 265', 9);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (5, 'ООО ПСМК', 'ООО ПСМК', 6);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (6, 'Каскад-Инфра ООО', 'Каскад-Инфра ООО', 6);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (7, 'НИПИГОРМАШ НАО', 'НИПИГОРМАШ НАО', 6);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (8, 'Арктур', 'Арктур', 6);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (9, 'ООО Затундра', 'ООО Затундра', 6);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (10, 'ООО ЗСК', 'ООО ЗСК', 6);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (11, 'ООО ПСК', 'ООО ПСК', 6);
INSERT INTO SI_V_SUBJECTS(N_SUBJECT_ID, vc_code, vc_name, N_SUBJ_TYPE_ID) VALUES (12, 'Синтез-01', 'Синтез-0', 6);


COMMIT;