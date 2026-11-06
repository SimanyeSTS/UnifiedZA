USE unifiedza_db;

INSERT INTO sponsors (id, name, email, contact_number)
VALUES (1, 'NSFAS', 'info@nsfas.org.za', '0800067327')
    ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Removed closing_date from the column list and the '2026-11-30' from the VALUES
INSERT INTO bursaries (title, field_of_study, min_aps_score, max_household_income, sponsor_id)
VALUES ('NSFAS General Bursary', 'Information Technology', 24, 350000.00, 1)
    ON DUPLICATE KEY UPDATE title=VALUES(title);