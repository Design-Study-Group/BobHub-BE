-- 테스트용 초기 데이터

-- 테스트 사용자 데이터
INSERT INTO users (id, email, name, picture) VALUES
(61, 'test.owner@example.com', '테스트 소유자', 'https://example.com/picture1.jpg'),
(62, 'test.user1@example.com', '테스트 사용자1', 'https://example.com/picture2.jpg'),
(63, 'test.user2@example.com', '테스트 사용자2', 'https://example.com/picture3.jpg'),
(999, 'test.nonowner@example.com', '테스트 비소유자', 'https://example.com/picture4.jpg');

-- 테스트 파티 데이터
INSERT INTO party (id, category, title, limit_people, limit_price, owner_id, is_open, created_at, finished_at) VALUES 
(1, 'DELIVERY', '테스트 배달 파티', 4, 30000, 61, true, CURRENT_TIMESTAMP, DATEADD('DAY', 1, CURRENT_TIMESTAMP)),
(2, 'DINE_OUT', '테스트 외식 파티', 6, 50000, 61, true, CURRENT_TIMESTAMP, DATEADD('DAY', 2, CURRENT_TIMESTAMP)),
(3, 'LUNCHBOX', '테스트 도시락 파티', 8, 15000, 62, true, CURRENT_TIMESTAMP, DATEADD('DAY', 3, CURRENT_TIMESTAMP)),
(4, 'BETTING', '테스트 내기 파티', 4, 10000, 63, false, CURRENT_TIMESTAMP, DATEADD('DAY', 1, CURRENT_TIMESTAMP));

-- 테스트 파티 멤버 데이터
INSERT INTO partymember (party_id, user_id, role) VALUES 
(1, 61, 'OWNER'),
(1, 62, 'MEMBER'),
(2, 61, 'OWNER'),
(3, 62, 'OWNER'),
(3, 63, 'MEMBER'),
(4, 63, 'OWNER'); 