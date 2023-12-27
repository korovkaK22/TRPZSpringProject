
CREATE TABLE IF NOT EXISTS roles (
             id SERIAL PRIMARY KEY,
             name VARCHAR(255) NOT NULL,
             path VARCHAR(255) NOT NULL,
             is_admin BOOLEAN NOT NULL,
             is_enabled BOOLEAN NOT NULL,
             can_write BOOLEAN NOT NULL,
             upload_speed INT NOT NULL,
             download_speed INT NOT NULL
);




CREATE TABLE IF NOT EXISTS users (
             id SERIAL PRIMARY KEY NOT NULL,
             username VARCHAR(255) NOT NULL,
             password VARCHAR(255) NOT NULL,
             role_id SERIAL NOT NULL references roles(id)
);

INSERT INTO roles (name, path, is_admin, is_enabled, can_write, upload_speed, download_speed)
