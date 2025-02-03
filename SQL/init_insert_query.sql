-- enum 데이터 삽입 SQL

-- link_type의 종류 데이터 저장
INSERT INTO link_type (name, description) VALUES ('Github', '깃허브 주소');
INSERT INTO link_type (name, description) VALUES ('Notion', '노션 주소');
INSERT INTO link_type (name, description) VALUES ('Personal Website', '개인 웹사이트');
INSERT INTO link_type (name, description) VALUES ('기타', '위 종류 외의 모든 주소');

-- skill_level의 종류 데이터 저장
INSERT INTO skill_level (name) VALUES ('초급');
INSERT INTO skill_level (name) VALUES ('중급');
INSERT INTO skill_level (name) VALUES ('고급');

-- skill_category의 종류 데이터 저장
INSERT INTO skill_category (name, description) VALUES ('Programming Language', '프로그래밍 언어');
INSERT INTO skill_category (name, description) VALUES ('Markup Language', '마크업 언어');
INSERT INTO skill_category (name, description) VALUES ('Style Sheet Language', '스타일 시트 언어');
INSERT INTO skill_category (name, description) VALUES ('Database', '데이터베이스 관련');
INSERT INTO skill_category (name, description) VALUES ('Containerization', '컨테이너 관련');
INSERT INTO skill_category (name, description) VALUES ('Version Control', '버전 관리 시스템');
INSERT INTO skill_category (name, description) VALUES ('Orchestration', '오케스트레이션 관련');
INSERT INTO skill_category (name, description) VALUES ('Cloud Platform', '클라우드 플랫폼');
INSERT INTO skill_category (name, description) VALUES ('Web Server', '웹 서버');
INSERT INTO skill_category (name, description) VALUES ('Configuration Management', '구성 관리');
INSERT INTO skill_category (name, description) VALUES ('Infrastructure as Code', '코드로서의 인프라');
INSERT INTO skill_category (name, description) VALUES ('CI/CD', '지속적 통합 및 배포');
INSERT INTO skill_category (name, description) VALUES ('Monitoring', '모니터링 시스템');
INSERT INTO skill_category (name, description) VALUES ('Search Engine', '검색 엔진');
INSERT INTO skill_category (name, description) VALUES ('Log Management', '로그 관리');
INSERT INTO skill_category (name, description) VALUES ('Data Visualization', '데이터 시각화');
INSERT INTO skill_category (name, description) VALUES ('Message Broker', '메시지 브로커');
INSERT INTO skill_category (name, description) VALUES ('Framework', '프레임워크');
INSERT INTO skill_category (name, description) VALUES ('JavaScript Framework', '자바스크립트 프레임워크');
INSERT INTO skill_category (name, description) VALUES ('Scripting Language', '스크립팅 언어');


-- skill의 종류 데이터 저장
INSERT INTO skill (name, skill_category_id) VALUES ('Java', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Python', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('JavaScript', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('HTML', 2);
INSERT INTO skill (name, skill_category_id) VALUES ('CSS', 3);
INSERT INTO skill (name, skill_category_id) VALUES ('SQL', 4);
INSERT INTO skill (name, skill_category_id) VALUES ('C#', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('C++', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Ruby', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('PHP', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Swift', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Kotlin', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('R', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Go', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Scala', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Perl', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('TypeScript', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Shell Scripting', 20);
INSERT INTO skill (name, skill_category_id) VALUES ('Objective-C', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Rust', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Dart', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Groovy', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Lua', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('MATLAB', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Julia', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Haskell', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Elixir', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Clojure', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('F#', 1);
INSERT INTO skill (name, skill_category_id) VALUES ('Erlang', 1);
-- 서버쪽 기술 스택 및 프레임워크 추가
INSERT INTO skill (name, skill_category_id) VALUES ('Docker', 5);
INSERT INTO skill (name, skill_category_id) VALUES ('GitHub', 6);
INSERT INTO skill (name, skill_category_id) VALUES ('Kubernetes', 7);
INSERT INTO skill (name, skill_category_id) VALUES ('AWS', 8);
INSERT INTO skill (name, skill_category_id) VALUES ('Azure', 8);
INSERT INTO skill (name, skill_category_id) VALUES ('Google Cloud', 8);
INSERT INTO skill (name, skill_category_id) VALUES ('Heroku', 8);
INSERT INTO skill (name, skill_category_id) VALUES ('Nginx', 9);
INSERT INTO skill (name, skill_category_id) VALUES ('Apache', 9);
INSERT INTO skill (name, skill_category_id) VALUES ('Ansible', 10);
INSERT INTO skill (name, skill_category_id) VALUES ('Terraform', 11);
INSERT INTO skill (name, skill_category_id) VALUES ('Jenkins', 12);
INSERT INTO skill (name, skill_category_id) VALUES ('CI/CD', 12);
INSERT INTO skill (name, skill_category_id) VALUES ('Prometheus', 13);
INSERT INTO skill (name, skill_category_id) VALUES ('Grafana', 13);
INSERT INTO skill (name, skill_category_id) VALUES ('ElasticSearch', 14);
INSERT INTO skill (name, skill_category_id) VALUES ('Logstash', 15);
INSERT INTO skill (name, skill_category_id) VALUES ('Kibana', 16);
INSERT INTO skill (name, skill_category_id) VALUES ('RabbitMQ', 17);
INSERT INTO skill (name, skill_category_id) VALUES ('Kafka', 17);
-- 서버 언어 프레임워크 추가
INSERT INTO skill (name, skill_category_id) VALUES ('Spring Boot', 18);
INSERT INTO skill (name, skill_category_id) VALUES ('Next.js', 18);
INSERT INTO skill (name, skill_category_id) VALUES ('Express', 18);
INSERT INTO skill (name, skill_category_id) VALUES ('Django', 18);
INSERT INTO skill (name, skill_category_id) VALUES ('Flask', 18);
INSERT INTO skill (name, skill_category_id) VALUES ('ASP.NET Core', 18);
INSERT INTO skill (name, skill_category_id) VALUES ('Ruby on Rails', 18);
INSERT INTO skill (name, skill_category_id) VALUES ('Laravel', 18);
INSERT INTO skill (name, skill_category_id) VALUES ('NestJS', 18);
INSERT INTO skill (name, skill_category_id) VALUES ('FastAPI', 18);
-- 자바스크립트 프레임워크 추가
INSERT INTO skill (name, skill_category_id) VALUES ('React', 19);
INSERT INTO skill (name, skill_category_id) VALUES ('Vue.js', 19);
INSERT INTO skill (name, skill_category_id) VALUES ('Angular', 19);
INSERT INTO skill (name, skill_category_id) VALUES ('Svelte', 19);
INSERT INTO skill (name, skill_category_id) VALUES ('Ember.js', 19);
INSERT INTO skill (name, skill_category_id) VALUES ('Meteor', 19);
INSERT INTO skill (name, skill_category_id) VALUES ('Backbone.js', 19);
INSERT INTO skill (name, skill_category_id) VALUES ('Preact', 19);
INSERT INTO skill (name, skill_category_id) VALUES ('Nuxt.js', 19);

commit;