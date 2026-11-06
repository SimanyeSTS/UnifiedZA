CREATE TABLE IF NOT EXISTS sponsors (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    contact_number VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS bursaries (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         title VARCHAR(255) NOT NULL,
    field_of_study VARCHAR(100) NOT NULL,
    min_aps_score INT NOT NULL,
    max_household_income DECIMAL(12,2) NOT NULL,
    sponsor_id BIGINT,
    FOREIGN KEY (sponsor_id) REFERENCES sponsors(id) ON DELETE SET NULL
    );