CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    state_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS custom_states (
    name VARCHAR(255) PRIMARY KEY,
    is_admin BOOLEAN NOT NULL,
    direction VARCHAR(255) NOT NULL,
    is_enabled BOOLEAN NOT NULL,
    can_write BOOLEAN NOT NULL,
    upload_speed INT NOT NULL,
    download_speed INT NOT NULL
);

CREATE TABLE user_snapshots (
                                id SERIAL PRIMARY KEY,
                                name VARCHAR(255),
                                username VARCHAR(255),
                                password VARCHAR(255),
                                state_name VARCHAR(255)
);