INSERT INTO v_client( n_client_id, vc_code, vc_name)
VALUES
    (1, 'orgCode1', 'orgName1' ),
    (2, 'orgCode2', 'orgName2'),
    (3, 'orgCode3', 'orgName3'),
    (4, 'orgCode4', 'orgName4'),
    (5, 'orgCode5', 'orgName5'),
    (6, 'orgCode6', 'orgName6'),
    (7, 'orgCode7', 'orgName7'),
    (8, 'orgCode8', 'orgName8')
;

INSERT INTO v_store( n_store_id, vc_code, vc_name)
VALUES
    (1, 'storeCode1', 'storeName1' ),
    (2, 'storeCode2', 'storeName2'),
    (3, 'storeCode3', 'storeName3'),
    (4, 'storeCode4', 'storeName4'),
    (5, 'storeCode5', 'storeName5'),
    (6, 'storeCode6', 'storeName6'),
    (7, 'storeCode7', 'storeName7'),
    (8, 'storeCode8', 'storeName8')
;

INSERT INTO client_user(n_user_id, vc_first_name, vc_last_name, n_client_id, vc_login, vc_password)
VALUES (1, 'ADMIN', 'ADMIN', 1,  'ADMIN', '8257a3811b9f6bb9d59dfb3931e220fa5574cee38fff551066caca1a50b1691ebdffa87f2d7213910e8bdbcf4d669c2756e57196667dd8f5e8af66971b2');

INSERT INTO loading_point( n_loading_point_id, n_store_id, vc_code, vc_name, vc_comment)
VALUES
    (1, 1, 'loading_point_Code1', 'loading_point_Name1' , 'loading_point_Comment_1' ),
    (2, 1, 'loading_point_Code2', 'loading_point_Name2' , 'loading_point_Comment_2' ),
    (3, 1, 'loading_point_Code3', 'loading_point_Name3' , 'loading_point_Comment_3' ),
    (4, 2, 'loading_point_Code1', 'loading_point_Name1' , 'loading_point_Comment_1' ),
    (5, 2, 'loading_point_Code2', 'loading_point_Name3' , 'loading_point_Comment_2' ),
    (6, 3, 'loading_point_Code1', 'loading_point_Name1' , 'loading_point_Comment_1' ),
    (7, 3, 'loading_point_Code2', 'loading_point_Name3' , 'loading_point_Comment_2' ),
    (8, 3, 'loading_point_Code1', 'loading_point_Name1' , 'loading_point_Comment_1' )
;


    INSERT INTO slot( n_slot_id, d_date, n_loading_point_id, n_client_id, d_start_time, d_end_time, vc_status)
    VALUES
(1, '2025-08-16', 1, 1, '14:30:00', '15:30:00' , 'B' ),
(2, '2025-08-16', 1, 2, '15:30:00', '16:30:00' , 'B' ),
(3, '2025-08-16', 1, 3, '15:30:00', '17:30:00' , 'B' ),
(4, '2025-08-16', 3, 5, '14:30:00', '15:30:00' , 'B' ),
(5, '2025-08-16', 3, 6, '15:30:00', '16:30:00' , 'B' ),
(6, '2025-08-16', 5, 4, '10:30:00', '11:30:00' , 'B' ),
(7, '2025-08-16', 5, 7, '11:30:00', '12:30:00' , 'B' )
    ;

