import React, { useEffect, useState } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import { useLocation } from "react-router-dom";
import { getList, projectSearch } from "../../api/projectApi";
import useCustomMove from "../../hooks/useCustomMove";
import PageComponent from "../common/PageComponent";
import SearchBar from "../search/SearchBar";
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

const initQueryData = {
	page: 1,
	size: 12,
	query: "",
	querySkills: [],
	type: "all",
	sortOption: "relevance",
}

export default function ListComponent() {
	const { page, size, moveToList, moveToSearch, moveToRead, refresh } =
		useCustomMove();
	const [queryData, setQueryData] = useState(initQueryData);
	const [serverData, setServerData] = useState(initState);
	const location = useLocation();
	const isSearchPage = location.pathname.includes('/search');

	useEffect(() => {
		const params = new URLSearchParams(location.search);
		const query = params.get("query");
		const querySkills = params.getAll("querySkills");
		const type = params.get("type");
		const sortOption = params.get("sortOption");
		setQueryData({
			page: page,
			size: size,
			query: query,
			querySkills: querySkills,
			type: type,
			sortOption: sortOption
		})
		if (isSearchPage) {
			// 검색 api 호출
			console.log(querySkills);
			projectSearch({ page, size, query, querySkills, type, sortOption }).then((data) => {
				setServerData(data);
			});
		} else {
			// 리스트 출력 api 호출
			getList({ page, size }).then((data) => {
				setServerData(data);
			});
		}
		// refresh 값이 변경되면 데이터를 새로 받아옴.
		// refresh 임시 제거: 이미 데이터를 불러온 페이지 호출 시 이동하지 않는 현상 발생
	}, [page, size, location.search]);

	return (
		<div>
			<div>
				<div>
					<SearchBar 
						queryData = {queryData}
					/>
				</div>
				<div>
					<Container className="mt-4">
						<Row xs={1} sm={2} md={4} className="g-4">
							{serverData.dtoList.map((project, index) => (
								<Col key={index}>
									{project.public && (
										<Card 
											onClick={() => moveToRead(project.id)}
											className="cursor-pointer"
										>
											<Card.Body>
												<Card.Title>{project.title}</Card.Title>
												{/* TODO: 유저 이름 출력 (현재 userId 출력) */}
												<Card.Text>작성자: {project.userName}</Card.Text>
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
				</div>
				<PageComponent
					serverData={serverData}
					queryData = {queryData}
					movePage={
						isSearchPage
							? moveToSearch
							: moveToList
					}
				/>
			</div>
		</div>
	);
}
