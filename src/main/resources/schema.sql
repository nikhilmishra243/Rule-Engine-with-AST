CREATE TABLE IF NOT EXISTS rules (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     rule_string VARCHAR(255) NOT NULL,
    ast CLOB
    );