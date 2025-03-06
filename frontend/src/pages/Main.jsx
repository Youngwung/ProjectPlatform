import React, { useEffect, useState } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import portfolioApi from "../api/portfolioApi";
import { getProjectsForMain } from "../api/projectApi";
import SkillTagComponent from "../components/skill/SkillTagComponent";
import useCustomMove from "../hooks/useCustomMove";
import useCustomPortfolioMove from "../hooks/useCustomPortfolioMove";

const Main = () => {
	const [project, setProject] = useState([]);
	const [portfolio, setPortfolio] = useState([]);
	const moveToProject = useCustomMove().moveToRead;
	const moveToPortfolio = useCustomPortfolioMove().moveToRead;
	useEffect(() => {
		getProjectsForMain()
			.then((res) => {
				console.log(res);

				setProject(res);
			})
			.catch((error) => {
				console.error(error);
			});
		portfolioApi
			.getListForMain()
			.then((res) => {
				setPortfolio(res);
			})
			.catch((error) => {
				console.error(error);
			});
	}, []);

	return (
		<Container className="my-5">
			<section>
				<h2 className="my-4">추천 프로젝트</h2>
				<Container className="mt-4">
					<Row xs={1} sm={2} md={4} className="g-4">
						{project.map((project, index) => (
							<Col key={index}>
								{project.public && (
									<Card
										onClick={() => moveToProject(project.id)}
										className="cursor-pointer"
									>
										<Card.Body>
											<Card.Title>{project.title}</Card.Title>
											{/* TODO: 유저 이름 출력 (현재 userId 출력) */}
											<Card.Text>작성자: {project.userId}</Card.Text>
											<Card.Text>인원: {project.maxPeople}</Card.Text>
											<Card.Footer className="m-0 p-2 py-1">
												<SkillTagComponent skills={project.skills} />
											</Card.Footer>
										</Card.Body>
									</Card>
								)}
							</Col>
						))}
					</Row>
				</Container>
			</section>

			<section className="mt-5">
				<h2 className="my-4">인기 포트폴리오</h2>
				<Container>
					<Row>
						{portfolio.map((portfolio) => (
							<Col md={3} className="mb-4" key={portfolio.id}>
								<Card
									onClick={() => moveToPortfolio(portfolio.id)}
									style={{ cursor: "pointer" }}
								>
									{/* <Card.Img variant="top" src={portfolio.image_url} /> */}
									<Card.Body>
										<Card.Title>{portfolio.title}</Card.Title>
										<Card.Text>
											<strong>작성자:</strong> {portfolio.userId}
										</Card.Text>
										<Card.Text>{portfolio.description}</Card.Text>
										<Card.Text>
											<SkillTagComponent skills={portfolio.skills} />
										</Card.Text>
										<Card.Text>
											<strong>링크 :</strong>
										</Card.Text>
									</Card.Body>
								</Card>
							</Col>
						))}
					</Row>
				</Container>
			</section>
		</Container>
	);
};

export default Main;
