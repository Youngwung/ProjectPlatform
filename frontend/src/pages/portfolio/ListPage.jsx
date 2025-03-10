import React, { useEffect, useState } from "react";
import { Alert, Card, Col, Container, Row, Spinner } from "react-bootstrap";
import { useLocation } from "react-router-dom";
import portfolioApi from "../../api/portfolioApi";
import PageComponent from "../../components/common/PageComponent";
import SearchBar from "../../components/search/SearchBar";
import SkillTagComponent from "../../components/skill/SkillTagComponent";
import useCustomPortfolioMove from "../../hooks/useCustomPortfolioMove";

const ListPage = () => {
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
	};
	const { page, size, moveToList, moveToPortfolioSearch, moveToRead, refresh } =
		useCustomPortfolioMove();
	const [queryData, setQueryData] = useState(initQueryData);
	const location = useLocation();
	const isSearchPage = location.pathname.includes("/search");
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);
	const [nodata, setNodata] = useState(false); // ✅ 상태 변수 추가
	const [portfolios, setPortfolios] = useState(initState); // 전체 포트폴리오 데이터

	// 전체 프로젝트 가져오기
	useEffect(() => {
		const fetchAllportfolios = async () => {
			try {
				setLoading(true);
				const params = new URLSearchParams(location.search);
				const query = params.get("query");
				const querySkills = params.getAll("querySkills");
				const sortOption = params.get("sortOption");
				setQueryData({
					page: page,
					size: size,
					query: query,
					querySkills: querySkills,
					sortOption: sortOption,
				});
				if (isSearchPage) {
					// 검색 api 호출
					//console.log(querySkills);
					await portfolioApi
						.portfolioSearch({ page, size, query, querySkills, sortOption })
						.then((data) => {
							if (data.dtoList.length === 0) {
								setNodata(true); // ✅ nodata 상태를 true로 변경
							} else {
								setPortfolios(data);
								setNodata(false); // ✅ 데이터가 있으면 false 설정
							}
						});
				} else {
					const response = await portfolioApi.getAllProjects();
					if (response.dtoList.length === 0) {
						setNodata(true); // ✅ nodata 상태를 true로 변경
					} else {
						setPortfolios(response);
						setNodata(false); // ✅ 데이터가 있으면 false 설정
					}

					//console.log("✅ 전체 프로젝트 조회 성공:", response);
				}
			} catch (error) {
				console.error("❌ 전체 프로젝트 조회 실패:", error);
				setError("데이터를 불러오는 중 오류가 발생했습니다.");
			} finally {
				setLoading(false);
			}
		};

		fetchAllportfolios();
	}, [page, size, location.search]);
	

	if (loading) {
		return (
			<Container className="text-center mt-4">
				<Spinner animation="border" variant="primary" />
				<p>로딩 중...</p>
			</Container>
		);
	}

	if (error) {
		return (
			<Container className="text-center mt-4">
				<Alert variant="danger">{error}</Alert>
			</Container>
		);
	}

	if (nodata) {
		return (
			<Container className="text-center mt-4">
				<Alert variant="warning">포트폴리오 데이터가 없습니다.</Alert>
			</Container>
		);
	}

	return (
		<Container>
			<SearchBar queryData={queryData} />
			<Row>
				{portfolios.dtoList.map((portfolio) => (
					<Col md={3} className="mb-4" key={portfolio.id}>
						<Card
							onClick={() => moveToRead(portfolio.id)}
							style={{ cursor: "pointer" }}
						>
							{/* <Card.Img variant="top" src={portfolio.image_url} /> */}
							<Card.Body>
								<Card.Title>{portfolio.title}</Card.Title>
								<Card.Text>
									<strong>작성자:</strong> {portfolio.userName}
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
			<PageComponent
				serverData={portfolios}
				queryData={queryData}
				movePage={isSearchPage ? moveToPortfolioSearch : moveToList}
			/>
		</Container>
	);
};

export default ListPage;
