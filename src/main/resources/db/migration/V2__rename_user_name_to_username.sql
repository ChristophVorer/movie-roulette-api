ALTER TABLE users RENAME COLUMN user_name TO username;
ALTER TABLE users RENAME INDEX uk_users_user_name TO uk_users_username;