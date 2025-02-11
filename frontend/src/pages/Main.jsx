import React from 'react';
import { Card, Container, Row, Col } from 'react-bootstrap';
import AlertBtn from '../components/alert/AlertBtn';
import AlertPage from './AlertPage';

const jobs = [
  { title: '프론트엔드 개발자', description: 'React 및 Vue.js 경험자를 모집합니다.', image: 'https://source.unsplash.com/300x200/?technology' },
  { title: '백엔드 개발자', description: 'Spring Boot 및 MySQL 경험자를 모집합니다.', image: 'https://source.unsplash.com/300x200/?startup' },
  { title: 'UI/UX 디자이너', description: '디자인 경험 3년 이상.', image: 'https://source.unsplash.com/300x200/?design' },
  { title: '프로젝트 매니저', description: '프로젝트 관리 경력자.', image: 'https://source.unsplash.com/300x200/?management' },
  { title: '프론트엔드 개발자', description: 'React 및 Vue.js 경험자를 모집합니다.', image: 'https://source.unsplash.com/300x200/?technology' },
  { title: '백엔드 개발자', description: 'Spring Boot 및 MySQL 경험자를 모집합니다.', image: 'https://source.unsplash.com/300x200/?startup' },
  { title: 'UI/UX 디자이너', description: '디자인 경험 3년 이상.', image: 'https://source.unsplash.com/300x200/?design' },
  { title: '프로젝트 매니저', description: '프로젝트 관리 경력자.', image: 'https://source.unsplash.com/300x200/?management' },
];

const seekers = [
  { title: '신입 프론트엔드 개발자', description: 'JavaScript 및 React 스킬 보유.', image: 'https://source.unsplash.com/300x200/?developer' },
  { title: '신입 백엔드 개발자', description: 'Spring Framework 관심.', image: 'https://source.unsplash.com/300x200/?backend' },
  { title: '마케팅 인턴', description: 'SEO와 데이터 분석 가능.', image: 'https://source.unsplash.com/300x200/?marketing' },
  { title: '디자인 신입', description: 'Photoshop 및 Figma 가능.', image: 'https://source.unsplash.com/300x200/?creative' },
  { title: '프론트엔드 개발자', description: 'React 및 Vue.js 경험자를 모집합니다.', image: 'https://source.unsplash.com/300x200/?technology' },
  { title: '백엔드 개발자', description: 'Spring Boot 및 MySQL 경험자를 모집합니다.', image: 'https://source.unsplash.com/300x200/?startup' },
  { title: 'UI/UX 디자이너', description: '디자인 경험 3년 이상.', image: 'https://source.unsplash.com/300x200/?design' },
  { title: '프로젝트 매니저', description: '프로젝트 관리 경력자.', image: 'https://source.unsplash.com/300x200/?management' },
];

const Main = () => {
  return (
    <Container className="my-5">
      <AlertPage/>
      <br/>
      <br/>
      <br/>
      <br/>
      <br/>
      <br/>
      <br/>
      <br/>
      <br/>
      <br/>
      <br/>
      <AlertBtn/> {/* 📌 알람 버튼 추가 */}
      <section>
        <h2 className="my-4">구인 섹션</h2>
        <Row>
          {jobs.map((job, index) => (
            <Col key={index} md={3} className="mb-4">
              <Card>
                <Card.Img variant="top" src={job.image} />
                <Card.Body>
                  <Card.Title>{job.title}</Card.Title>
                  <Card.Text>{job.description}</Card.Text>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </section>

      <section className="mt-5">
        <h2 className="my-4">구직 섹션</h2>
        <Row>
          {seekers.map((seeker, index) => (
            <Col key={index} md={3} className="mb-4">
              <Card>
                <Card.Img variant="top" src={seeker.image} />
                <Card.Body>
                  <Card.Title>{seeker.title}</Card.Title>
                  <Card.Text>{seeker.description}</Card.Text>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </section>
    </Container>
  );
};

export default Main;
