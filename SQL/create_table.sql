-- user 테이블 생성
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    experience TEXT,
    provider_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- provider 테이블 생성
CREATE TABLE provider (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(30)
);

-- link 테이블 생성
CREATE TABLE link (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    link_type_id BIGINT NOT NULL,
    url VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- link_type 테이블 생성
CREATE TABLE link_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

-- skill 테이블 생성
CREATE TABLE skill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    skill_category_id BIGINT NOT NULL
);

-- skill_category 테이블 생성
CREATE TABLE skill_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(50)
);


-- skill_level 테이블 생성
CREATE TABLE skill_level (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- user_skill 테이블 생성
CREATE TABLE user_skill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    skill_level_id BIGINT NOT NULL
);

-- joinproject 테이블 생성
CREATE TABLE project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    max_people INT,
    status ENUM('모집_중', '진행_중', '완료') DEFAULT '모집_중',
    is_public BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- joinproject_skill 테이블 생성
CREATE TABLE project_skill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    skill_level_id BIGINT NOT NULL
);

-- findproject 테이블 생성
CREATE TABLE portfolio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- bookmark 테이블 생성
CREATE TABLE bookmark_project (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	user_id BIGINT NOT NULL,
    project_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- bookmark 테이블 생성
CREATE TABLE bookmark_portfolio (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	user_id BIGINT NOT NULL,
    portfolio_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE project_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('all', 'skill', 'content') DEFAULT 'all',
    project_id BIGINT NOT NULL
);
CREATE TABLE `alert_portfolio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `portfolio_id` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `content` text,
  `status` enum('초대','접수','합격','불합격') DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_read` tinyint NOT NULL DEFAULT '0',
  `type` enum('참가알림','초대알림') DEFAULT NULL,
  PRIMARY KEY (`id`)https://docs.google.com/spreadsheets/d/1pP7Qc_6jqKE1Fs5lKI7elNvJDN61QAjLAXdi7yCLDUg/edit?gid=538927429#gid=538927429
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `alert_project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `project_id` bigint DEFAULT NULL,
  `alert_owner_id` bigint NOT NULL,
  `sender_id` bigint NOT NULL,
  `receiver_id` bigint NOT NULL,
  `content` text,
  `status` enum('신청','검토중','합격','불합격','초대수락','초대거절') DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_read` tinyint NOT NULL DEFAULT '0',
  `type` enum('참가알림','초대알림') DEFAULT NULL,
  `step` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

