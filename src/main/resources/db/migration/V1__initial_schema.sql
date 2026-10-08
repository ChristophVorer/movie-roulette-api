CREATE TABLE users
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    user_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_users_user_name UNIQUE (user_name),
    CONSTRAINT uk_users_email UNIQUE (email)
);