-- 1. Employee
INSERT INTO employee (name, title, boss, boss_role, has_boss) VALUES ('Alice Tanaka', 'CEO', 'None', TRUE, FALSE);
INSERT INTO employee (name, title, boss, boss_role, has_boss) VALUES ('Bob Smith', 'Manager', 'Alice Tanaka', TRUE, TRUE);
INSERT INTO employee (name, title, boss, boss_role, has_boss) VALUES ('Charlie Day', 'Engineer', 'Bob Smith', FALSE, TRUE);

-- 2. Boss
INSERT INTO boss (employee_id) VALUES (1);
INSERT INTO boss (employee_id) VALUES (2);

-- 3. Boss Subordinates
-- Alice (boss_id 1) -> Bob (employee_id 2)
-- Bob (boss_id 2) -> Charlie (employee_id 3)
INSERT INTO boss_subordinates (boss_id, subordinate_id) VALUES (1, 2);
INSERT INTO boss_subordinates (boss_id, subordinate_id) VALUES (2, 3);

-- 4. Laptop
INSERT INTO laptop (os_version, laptop_year, in_use, employee_id) VALUES (11, 2023, TRUE, 1);
INSERT INTO laptop (os_version, laptop_year, in_use, employee_id) VALUES (10, 2021, TRUE, 2);
INSERT INTO laptop (os_version, laptop_year, in_use, employee_id) VALUES (11, 2024, TRUE, 3);

-- 5. Car
INSERT INTO car (car_year, milage, to_replace, last_serviced, to_service, need_to_service_date, last_insurance_renewal, insurance_expire_date, to_renew_insurance, in_use, employee_ID) 
VALUES (2022, 15000, FALSE, '2024-01-10', FALSE, '2024-07-10', '2024-01-01', '2025-01-01', FALSE, TRUE, 1);

INSERT INTO car (car_year, milage, to_replace, last_serviced, to_service, need_to_service_date, last_insurance_renewal, insurance_expire_date, to_renew_insurance, in_use, employee_ID) 
VALUES (2015, 85000, FALSE, '2023-05-20', FALSE, '2023-11-20', '2023-06-01', '2024-06-01', FALSE, TRUE, 2);

-- 6. ID Card
INSERT INTO id_card (employee_id, last_renewed_date, need_to_renew_date, in_use, to_renew) 
VALUES (1, '2023-01-01', '2026-01-01', TRUE, FALSE);

INSERT INTO id_card (employee_id, last_renewed_date, need_to_renew_date, in_use, to_renew) 
VALUES (2, '2022-05-15', '2025-05-15', TRUE, FALSE);