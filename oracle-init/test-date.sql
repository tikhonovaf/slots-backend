INSERT INTO slot(n_slot_id, d_date, n_loading_point_id, n_client_id, d_start_time, d_end_time, n_status_Id)
VALUES (1, TO_DATE('2025-08-16', 'YYYY-MM-DD'), 1, 1, TO_DATE('14:30:00', 'HH24:MI:SS'), TO_DATE('15:30:00', 'HH24:MI:SS'), 1);

INSERT INTO slot(n_slot_id, d_date, n_loading_point_id, n_client_id, d_start_time, d_end_time, n_status_Id)
VALUES (2, TO_DATE('2025-08-16', 'YYYY-MM-DD'), 1, 2, TO_DATE('15:30:00', 'HH24:MI:SS'), TO_DATE('16:30:00', 'HH24:MI:SS'), 2);

INSERT INTO slot(n_slot_id, d_date, n_loading_point_id, n_client_id, d_start_time, d_end_time, n_status_Id)
VALUES (3, TO_DATE('2025-08-16', 'YYYY-MM-DD'), 1, 3, TO_DATE('15:30:00', 'HH24:MI:SS'), TO_DATE('17:30:00', 'HH24:MI:SS'), 2);

INSERT INTO slot(n_slot_id, d_date, n_loading_point_id, n_client_id, d_start_time, d_end_time, n_status_Id)
VALUES (4, TO_DATE('2025-08-16', 'YYYY-MM-DD'), 3, 5, TO_DATE('14:30:00', 'HH24:MI:SS'), TO_DATE('15:30:00', 'HH24:MI:SS'), 3);

INSERT INTO slot(n_slot_id, d_date, n_loading_point_id, n_client_id, d_start_time, d_end_time, n_status_Id)
VALUES (5, TO_DATE('2025-08-16', 'YYYY-MM-DD'), 3, 6, TO_DATE('15:30:00', 'HH24:MI:SS'), TO_DATE('16:30:00', 'HH24:MI:SS'), 1);

INSERT INTO slot(n_slot_id, d_date, n_loading_point_id, n_client_id, d_start_time, d_end_time, n_status_Id)
VALUES (6, TO_DATE('2025-08-16', 'YYYY-MM-DD'), 5, 4, TO_DATE('10:30:00', 'HH24:MI:SS'), TO_DATE('11:30:00', 'HH24:MI:SS'), 1);

INSERT INTO slot(n_slot_id, d_date, n_loading_point_id, n_client_id, d_start_time, d_end_time, n_status_Id)
VALUES (7, TO_DATE('2025-08-16', 'YYYY-MM-DD'), 5, 7, TO_DATE('11:30:00', 'HH24:MI:SS'), TO_DATE('12:30:00', 'HH24:MI:SS'), 4);



---
INSERT INTO slot_template(n_slot_template_id, n_loading_point_id, n_status_Id, d_start_time, d_end_time)
VALUES (1, 1, 1, TO_DATE('14:30:00', 'HH24:MI:SS'), TO_DATE('15:30:00', 'HH24:MI:SS'));

INSERT INTO slot_template(n_slot_template_id, n_loading_point_id, n_status_Id, d_start_time, d_end_time)
VALUES (2, 1, 1, TO_DATE('15:30:00', 'HH24:MI:SS'), TO_DATE('16:30:00', 'HH24:MI:SS'));

INSERT INTO slot_template(n_slot_template_id, n_loading_point_id, n_status_Id, d_start_time, d_end_time)
VALUES (3, 1, 1, TO_DATE('15:30:00', 'HH24:MI:SS'), TO_DATE('17:30:00', 'HH24:MI:SS'));

INSERT INTO slot_template(n_slot_template_id, n_loading_point_id, n_status_Id, d_start_time, d_end_time)
VALUES (4, 3, 4, TO_DATE('14:30:00', 'HH24:MI:SS'), TO_DATE('15:30:00', 'HH24:MI:SS'));

INSERT INTO slot_template(n_slot_template_id, n_loading_point_id, n_status_Id, d_start_time, d_end_time)
VALUES (5, 3, 3, TO_DATE('15:30:00', 'HH24:MI:SS'), TO_DATE('16:30:00', 'HH24:MI:SS'));

INSERT INTO slot_template(n_slot_template_id, n_loading_point_id, n_status_Id, d_start_time, d_end_time)
VALUES (6, 5, 1, TO_DATE('10:30:00', 'HH24:MI:SS'), TO_DATE('11:30:00', 'HH24:MI:SS'));

INSERT INTO slot_template(n_slot_template_id, n_loading_point_id, n_status_Id, d_start_time, d_end_time)
VALUES (7, 5, 1, TO_DATE('11:30:00', 'HH24:MI:SS'), TO_DATE('12:30:00', 'HH24:MI:SS'));