USE mindcare_db;

-- Dummy data for platform_users
INSERT INTO platform_users (full_name, email, phone, role, student_id, department, status, join_date, last_active) VALUES
('Sarah Johnson', 'sarah.j@university.edu', '+1 234 567 8901', 'STUDENT', 'STU2024001', 'Computer Science', 'ACTIVE', '2024-09-15 09:00:00', '2024-09-15 11:00:00'),
('Michael Chen', 'michael.c@university.edu', '+1 234 567 8902', 'STUDENT', 'STU2024002', 'Engineering', 'ACTIVE', '2024-09-18 08:30:00', '2024-09-18 10:30:00'),
('Dr. Emily Roberts', 'e.roberts@university.edu', '+1 234 567 8903', 'PROFESSIONAL', 'PRO2024001', 'Counselling', 'ACTIVE', '2024-08-01 10:00:00', '2024-08-01 10:30:00'),
('Dr. James Wilson', 'j.wilson@university.edu', '+1 234 567 8904', 'PROFESSIONAL', 'PRO2024002', 'Psychology', 'ACTIVE', '2024-08-01 14:00:00', '2024-08-01 14:30:00'),
('Admin User', 'admin@university.edu', '+1 234 567 8905', 'ADMIN', 'ADM2024001', 'Administration', 'ACTIVE', '2024-07-01 09:00:00', '2024-07-01 09:05:00'),
('Jessica Martinez', 'jessica.m@university.edu', '+1 234 567 8906', 'STUDENT', 'STU2024003', 'Biology', 'INACTIVE', '2024-09-10 12:00:00', '2024-09-24 12:00:00'),
('David Thompson', 'david.t@university.edu', '+1 234 567 8907', 'STUDENT', 'STU2024004', 'Physics', 'ACTIVE', '2024-09-20 12:00:00', '2024-09-20 12:05:00'),
('Dr. Amanda Lee', 'a.lee@university.edu', '+1 234 567 8908', 'PROFESSIONAL', 'PRO2024003', 'Psychiatry', 'ACTIVE', '2024-08-15 13:00:00', '2024-08-15 14:00:00');

-- Dummy data for assessment_results (DASS-21)
INSERT INTO assessment_results (user_id, assessment_name, depression_score, anxiety_score, stress_score, depression_level, anxiety_level, stress_level, answers, completed_at) VALUES
(1, 'DASS-21', 8, 10, 12, 'Mild', 'Mild', 'Normal', '[1,2,3,4,2,1,2,3,2,2,3,2,2,1,2,2,1,3,2,3,2]', '2025-11-05 10:00:00'),
(1, 'DASS-21', 12, 14, 16, 'Moderate', 'Moderate', 'Moderate', '[2,3,3,4,2,2,3,4,2,3,3,3,2,2,3,3,2,3,3,3,2]', '2025-10-20 11:00:00');

-- Dummy data for counselling_session (simplified)
INSERT INTO counselling_session (student_id, counselor_id, session_date, session_time) VALUES
(1, 1, '2024-11-01', '10:00:00'),
(1, 1, '2024-11-15', '10:00:00'),
(2, 2, '2024-11-10', '12:00:00');
