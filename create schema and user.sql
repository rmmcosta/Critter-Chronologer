CREATE SCHEMA 'critter';
CREATE USER 'critter_admin'@'localhost' IDENTIFIED BY 'caxpto123';
GRANT ALL PRIVILEGES ON critter.* TO 'critter_admin'@'localhost';