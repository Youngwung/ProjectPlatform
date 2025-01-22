import React, { useEffect, useState } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import { getList } from "../../api/joinProjectApi";
import useCustomMove from "../../hooks/useCustomMove";
import PageComponent from "../common/PageComponent";

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
	const { page, size, moveToList } = useCustomMove();

	const [serverData, setServerData] = useState(initState);

	useEffect(() => {
		getList({ page, size }).then((data) => {
			console.log(data);
			setServerData(data);
		});
		return () => {};
	}, [page, size]);

	return (
		<div>
			<div>
				<div>
					<Container className="mt-4">
						<Row xs={1} sm={2} md={4} className="g-4">
							{serverData.dtoList.map((project, index) => (
								<Col key={index}>
									{project.public && (
										<Card>
											<Card.Body>
												<Card.Title>{project.title}</Card.Title>
												<Card.Text>{project.description}</Card.Text>
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
