import React, { useEffect, useState } from "react";
import { Alert, Button, Card, Container, Spinner } from "react-bootstrap";
import { Link, useNavigate, useParams } from "react-router-dom";
import portfolioApi from "../../api/portfolioApi";
import SkillTagComponent from "../../components/skill/SkillTagComponent";
import SkillTagGuideComponent from "../../components/skill/SkillTagGuideComponent";

const PortfolioDetail = () => {
	const { portfolioId } = useParams(); // URL에서 portfolioId 가져오기
	const navigate = useNavigate();

	// 초기 포트폴리오 상태 정의
	const portfolioInit = {
		id: null,
		title: "",
		description: "",
		userId: null,
		links: "",
		createdAt: "",
		updatedAt: "",
		image_url: "",
		skills: "",
		github_url: "",
	};

	const [portfolio, setPortfolio] = useState(portfolioInit);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);

	useEffect(() => {
		if (!portfolioId) {
			setError("올바른 프로젝트 ID가 아닙니다.");
			setLoading(false);
			return;
		}

		const fetchPortfolio = async () => {
			try {
				setLoading(true);
				setError(null);
				const data = await portfolioApi.getOne(portfolioId);
				if (!data || !data.id) {
					throw new Error("데이터가 존재하지 않습니다.");
				}
				console.log("📌 프로젝트 데이터:", data);
				setPortfolio(data);
			} catch (error) {
				console.error("❌ 프로젝트 조회 실패:", error);
				setError(error.message);
			} finally {
				setLoading(false);
			}
		};

		fetchPortfolio();
	}, [portfolioId]);

	// 🔥 삭제 버튼 핸들러
	const handleDelete = async () => {
		if (!window.confirm("정말 이 포트폴리오를 삭제하시겠습니까?")) {
			return; // 취소 시 아무 동작도 하지 않음
		}

		try {
			setLoading(true);
			await portfolioApi.deleteProject(portfolioId);
			alert("포트폴리오가 성공적으로 삭제되었습니다.");
			navigate("/portfolio/list"); // 삭제 후 목록으로 이동
		} catch (error) {
			console.error("❌ 포트폴리오 삭제 실패:", error);
			setError("포트폴리오 삭제 중 오류가 발생했습니다.");
		} finally {
			setLoading(false);
		}
	};

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
				<Link to="/portfolio/list">
					<Button variant="secondary">목록으로 돌아가기</Button>
				</Link>
			</Container>
		);
	}

	return (
		<Container className="mt-4">
			<Card>
				<Card.Img
					variant="top"
					src={portfolio.image_url || "/default-image.png"}
					alt="포트폴리오 이미지"
				/>
				<Card.Body>
					<Card.Title>{portfolio.title || "제목 없음"}</Card.Title>
					<Card.Text>{portfolio.description || "설명이 없습니다."}</Card.Text>
					<Card.Text>
            {/* 스킬 출력 컴포넌트 추가 */}
						<SkillTagGuideComponent />
						<SkillTagComponent skills={portfolio.skills} />
					</Card.Text>
					{portfolio.github_url && (
						<Card.Link href={portfolio.github_url} target="_blank">
							GitHub 링크
						</Card.Link>
					)}
					<Button
						variant="primary"
						onClick={() =>
							navigate(`/portfolio/modify/${portfolioId}`, {
								state: { portfolio },
							})
						}
					>
						수정
					</Button>
					<Button
						variant="danger"
						className="ms-2"
						onClick={handleDelete} // 🔥 삭제 버튼 핸들러 연결
					>
						삭제
					</Button>
				</Card.Body>
			</Card>
			<Link to="/portfolio/list">
				<Button variant="secondary" className="mt-4">
					목록으로 돌아가기
				</Button>
			</Link>
		</Container>
	);
};

export default PortfolioDetail;
//TODO 포트폴리오 상세페이지에서 초대 버튼 만들기 초대 버튼은 프로젝트 초대 버튼 초대 버튼을 누를때는 selectbar에서 초대할 프로젝트를 선택할수있게 만들기. 내가 만든 프로젝트는 내가초대가 안되게 만들어야하기도함
