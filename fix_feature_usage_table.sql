-- Fix the feature_usage table schema
-- Run this in your MySQL database

USE mindcare_db;

-- Drop the table if it exists to recreate it with proper schema
DROP TABLE IF EXISTS feature_usage;

-- Create the table with correct column names and default values
CREATE TABLE feature_usage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    `self-care` INT NOT NULL DEFAULT 0,
    assessments INT NOT NULL DEFAULT 0,
    `peer-support` INT NOT NULL DEFAULT 0,
    resources INT NOT NULL DEFAULT 0,
    counselling INT NOT NULL DEFAULT 0,
    UNIQUE KEY unique_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed sample feature usage to mirror UI screenshots
INSERT INTO feature_usage (user_id, `self-care`, assessments, `peer-support`, resources, counselling) VALUES
(1, 85, 78, 62, 68, 71),
(2, 70, 65, 55, 60, 58);

-- Verify the table structure
DESCRIBE feature_usage;

SELECT 'Feature usage table created successfully!' AS status;
