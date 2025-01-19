CREATE TABLE Course(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    code varchar(10) NOT NULL,
    name varchar(100) NOT NULL,
    description TEXT,
    status enum('ACTIVE', 'INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
    inactivation_date datetime,
    instructor_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (instructor_id) REFERENCES user(id),
    CONSTRAINT UC_code UNIQUE (code)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;