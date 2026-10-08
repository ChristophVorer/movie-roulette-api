UPDATE users
SET username = user_name
WHERE username IS NULL
   OR username = '';

ALTER TABLE users
    DROP COLUMN user_name;