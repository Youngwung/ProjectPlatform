import React, { useEffect, useState } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import { getList } from "../../api/joinProjectApi";
import useCustomMove from "../../hooks/useCustomMove";
import PageComponent from "../common/PageComponent";
import SkillTagComponent from "../skill/SkillTagComponent";

// 리스트 페이지에서 사용하는 모든 변수들을 기본값으로 초기화함.
// 리액트에서 편하게 사용하도록 리스트에서 사용하는 모든 변수를 서버사이드에서 다루게 만들었음.
const initState = {
	dtoList: [],
	pageNumList: [],
	pageRequestDTO: null,
	prev: false,
	next: false,
	totalCount: 0,
	prevPage: 0,
	nextPage: 0,
	totalPage: 0,
	current: 0,
};

export default function ListComponent() {
	const { page, size, moveToList,moveToRead, refresh } = useCustomMove();

	const [serverData, setServerData] = useState(initState);

	useEffect(() => {
		getList({ page, size }).then((data) => {
			console.log(data);
			setServerData(data);
		});
		return () => {};
		// refresh 값이 변경되면 데이터를 새로 받아옴.
	}, [page, size, refresh]);

	return (
		<div>
			<div>
				<div>
					<Container className="mt-4">
						<Row xs={1} sm={2} md={4} className="g-4">
							{serverData.dtoList.map((project, index) => (
								<Col key={index}>
									{project.public && (
										<Card onClick={() => moveToRead(project.id)}>
											<Card.Body>
												<Card.Title>{project.title}</Card.Title>
												{/* TODO: 유저 이름 출력 (현재 userId 출력) */}
												<Card.Text>작성자: {project.userId}</Card.Text>
												<Card.Text>인원: {project.maxPeople}</Card.Text>
												<Card.Footer>
													<SkillTagComponent skills={project.skills} />
												</Card.Footer>
											</Card.Body>
										</Card>
									)}
								</Col>
							))}
						</Row>
					</Container>
				</div>
				<PageComponent serverData={serverData} movePage={moveToList} />
			</div>
		</div>
	);
}
