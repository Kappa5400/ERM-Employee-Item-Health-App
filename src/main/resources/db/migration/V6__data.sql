-- Below is dummy info to insert into db for testing.
INSERT INTO employee (name, title, email, boss, boss_role, has_boss, username, password) 
VALUES ('Big boss', 'CEO', 'boss@example.com', 'None', TRUE, FALSE, 'test', '$2a$10$oFUYWKahTh1lnQ79cCF4IOu4Q9l9aHfmbdnz5VnAFCPTqcWADY6aS');

INSERT INTO employee (name, title, email, boss, boss_role, has_boss, username, password) 
VALUES ('Middle manager', 'Manager', 'manager@example.com', 'Big boss', TRUE, TRUE, 'test3', '$2a$10$oFUYWKahTh1lnQ79cCF4IOu4Q9l9aHfmbdnz5VnAFCPTqcWADY6aS');

INSERT INTO employee (name, title, email, boss, boss_role, has_boss, username, password) 
VALUES ('Lowly Employee', 'Employee', 'test@example.com', 'Middle manager', FALSE, TRUE, 'test2', '$2a$10$oFUYWKahTh1lnQ79cCF4IOu4Q9l9aHfmbdnz5VnAFCPTqcWADY6aS');


INSERT INTO boss (employee_id) VALUES (1);
INSERT INTO boss (employee_id) VALUES (2);


INSERT INTO boss_subordinates (boss_id, subordinate_id) VALUES (1, 2);
INSERT INTO boss_subordinates (boss_id, subordinate_id) VALUES (2, 3);


INSERT INTO laptop (os_version, laptop_year, in_use, employee_id) VALUES (11, 2023, TRUE, 1);
INSERT INTO laptop (os_version, laptop_year, in_use, employee_id) VALUES (10, 2021, TRUE, 2);
INSERT INTO laptop (os_version, laptop_year, in_use, employee_id) VALUES (11, 2024, TRUE, 3);


INSERT INTO car (car_year, milage, to_replace, last_serviced, to_service, need_to_service_date, last_insurance_renewal, insurance_expire_date, to_renew_insurance, in_use, employee_ID) 
VALUES (2022, 15000, FALSE, '2024-01-10', FALSE, '2024-07-10', '2024-01-01', '2025-01-01', FALSE, TRUE, 1);

INSERT INTO car (car_year, milage, to_replace, last_serviced, to_service, need_to_service_date, last_insurance_renewal, insurance_expire_date, to_renew_insurance, in_use, employee_ID) 
VALUES (2015, 85000, FALSE, '2023-05-20', FALSE, '2023-11-20', '2023-06-01', '2024-06-01', FALSE, TRUE, 2);


INSERT INTO id_card (employee_id, last_renewed_date, need_to_renew_date, in_use, to_renew) 
VALUES (1, '2023-01-01', '2026-01-01', TRUE, FALSE);

INSERT INTO id_card (employee_id, last_renewed_date, need_to_renew_date, in_use, to_renew) 
VALUES (2, '2022-05-15', '2025-05-15', TRUE, FALSE);