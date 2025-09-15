INSERT INTO slot_client
SELECT N_SUBJECT_ID
FROM SI_V_SUBJECTS
WHERE N_SUBJ_TYPE_ID = 6
;
commit ;

SELECT * FROM slot_client;


INSERT INTO slot_store
SELECT N_SUBJECT_ID
FROM SI_V_SUBJECTS
WHERE N_SUBJ_TYPE_ID = 9
;
commit ;

--  Добавление пунктов налива
INSERT INTO loading_point(n_loading_point_id, n_store_id, vc_code, vc_name, vc_comment)
VALUES (loading_point_seq.NEXTVAL, 1, 'пн-1', 'пункт налива-1', 'бензин, дизтопливо, тс');

INSERT INTO loading_point(n_loading_point_id, n_store_id, vc_code, vc_name, vc_comment)
VALUES (loading_point_seq.NEXTVAL, 1, 'пн-2', 'пункт налива-2', 'бензин, дизтопливо, тс');

INSERT INTO loading_point(n_loading_point_id, n_store_id, vc_code, vc_name, vc_comment)
VALUES (loading_point_seq.NEXTVAL, 1, 'пн-3', 'пункт налива-3', 'бензин, дизтопливо');

INSERT INTO loading_point(n_loading_point_id, n_store_id, vc_code, vc_name, vc_comment)
VALUES (loading_point_seq.NEXTVAL, 2, 'пн-1', 'пункт налива-1', 'бензин, дизтопливо');

INSERT INTO loading_point(n_loading_point_id, n_store_id, vc_code, vc_name, vc_comment)
VALUES (loading_point_seq.NEXTVAL, 2, 'пн-2', 'пункт налива-2', 'бензин, дизтопливо');

INSERT INTO loading_point(n_loading_point_id, n_store_id, vc_code, vc_name, vc_comment)
VALUES (loading_point_seq.NEXTVAL, 3, 'пн-1', 'пункт налива-1', 'бензин, дизтопливо, тс');

INSERT INTO loading_point(n_loading_point_id, n_store_id, vc_code, vc_name, vc_comment)
VALUES (loading_point_seq.NEXTVAL, 3, 'пн-2', 'пункт налива-2', 'бензин, дизтопливо');

INSERT INTO loading_point(n_loading_point_id, n_store_id, vc_code, vc_name, vc_comment)
VALUES (loading_point_seq.NEXTVAL, 3, 'пн-3', 'пункт налива-3', 'бензин, дизтопливо, тс');

commit ;
--