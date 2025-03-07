import React, { useEffect, useState } from "react";
import { Alert, Button, Container, Form, Spinner } from "react-bootstrap";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import portfolioApi from "../../api/portfolioApi";
import { getUserSkill, putUserSkill } from "../../api/skillApi";
import SkillModalComponent from "../../components/skill/SkillModalComponent";
import SkillTagComponent from "../../components/skill/SkillTagComponent";
import SkillTagGuideComponent from "../../components/skill/SkillTagGuideComponent";

const ModifyPortfolio = () => {
	const { portfolioId } = useParams(); // URL에서 portfolioId 가져오기
	const location = useLocation(); // 전달받은 state 데이터
	const navigate = useNavigate();

	// 초기 상태 설정
	const portfolioInit = {
		id: null,
		title: "",
		description: "",
		github_url: "",
	};

	const [portfolio, setPortfolio] = useState(
		location.state?.portfolio || portfolioInit
	);
	const [loading, setLoading] = useState(!location.state?.portfolio); // state 없으면 로딩 필요
	const [error, setError] = useState(null);
	const [title, setTitle] = useState(portfolio.title);
	const [description, setDescription] = useState(portfolio.description);
	const [githubUrl, setGithubUrl] = useState(portfolio.github_url);

	// `location.state`에 데이터가 없는 경우 API에서 직접 불러오기
	useEffect(() => {
		if (!location.state?.portfolio) {
			const fetchPortfolio = async () => {
				try {
					setLoading(true);
					setError(null);
					const data = await portfolioApi.getOne(portfolioId);
					if (!data || !data.id) {
						throw new Error("해당 포트폴리오 데이터를 찾을 수 없습니다.");
					}
					setPortfolio(data);
					setTitle(data.title);
					setDescription(data.description);
					setGithubUrl(data.github_url);
				} catch (err) {
					console.error("❌ 포트폴리오 불러오기 실패:", err);
					setError(err.message);
				} finally {
					setLoading(false);
				}
			};
			fetchPortfolio();
		}
	}, [portfolioId, location.state]);

	// 폼 제출 시 업데이트 요청
	const handleSubmit = async (e) => {
		e.preventDefault();
		try {
			setLoading(true);
			setError(null);

			const updatedData = {
				id: portfolioId,
				title,
				description,
				github_url: githubUrl,
			};

			//console.log("📌 업데이트 요청 데이터:", updatedData);

			await portfolioApi.updateProject(portfolioId, updatedData);
			alert("포트폴리오가 성공적으로 수정되었습니다.");
			navigate("/portfolio/list");
		} catch (err) {
			console.error("❌ 포트폴리오 수정 실패:", err);
			setError("포트폴리오 수정 중 오류가 발생했습니다.");
		} finally {
			setLoading(false);
		}
	};
	// 스킬 컴포넌트 관련 로직
	const [userSkills, setUserSkills] = useState("");
	const [showModal, setShowModal] = useState(false);

	useEffect(() => {
		getUserSkill()
			.then((result) => {
				//console.log(result);
				setUserSkills(result);
			})
			.catch((e) => {
				console.error(e);
			})
			.finally(() => {});
	}, []);

	const handleSkillModifyClick = () => {
		setShowModal(true);
	};

	// 취소 클릭 시
	const handleClose = () => {
		setShowModal(false);
	};

	// 수정 모달 "확인" 클릭 시
	const handleModifyConfirm = (exSkills) => {
		//console.log(exSkills);
		// 유저 스킬 수정 api 호출
		putUserSkill(exSkills).then((result) => {
			//console.log(result);
			setUserSkills(exSkills);
		});
		setShowModal(false);
	};

	// 로딩 중일 때 UI
	if (loading) {
		return (
			<Container className="text-center mt-4">
				<Spinner animation="border" variant="primary" />
				<p>로딩 중...</p>
			</Container>
		);
	}

	// 에러 발생 시 UI
	if (error) {
		return (
			<Container className="text-center mt-4">
				<Alert variant="danger">{error}</Alert>
				<Button variant="secondary" onClick={() => navigate("/portfolio/list")}>
					목록으로 돌아가기
				</Button>
			</Container>
		);
	}

	return (
		<Container className="mt-4">
			<h1>포트폴리오 수정</h1>
			<Form onSubmit={handleSubmit}>
				<Form.Group className="mb-3">
					<Form.Label>제목</Form.Label>
					<Form.Control
						type="text"
						value={title}
						onChange={(e) => setTitle(e.target.value)}
					/>
				</Form.Group>

				<Form.Group className="mb-3">
					<Form.Label>설명</Form.Label>
					<Form.Control
						as="textarea"
						value={description}
						onChange={(e) => setDescription(e.target.value)}
					/>
				</Form.Group>

				<Form.Group className="mb-3">
					<SkillTagGuideComponent />
					<Form.Group className="mb-1 border rounded px-2 py-2">
						<SkillTagComponent skills={userSkills} />
					</Form.Group>
					<Button className="mb-2" onClick={handleSkillModifyClick}>
						기술 수정
					</Button>
				</Form.Group>

				<Form.Group className="mb-3">
					<Form.Label>GitHub 링크</Form.Label>
					<Form.Control
						type="text"
						value={githubUrl}
						onChange={(e) => setGithubUrl(e.target.value)}
					/>
				</Form.Group>

				<Button variant="primary" type="submit" disabled={loading}>
					{loading ? "수정 중..." : "수정 완료"}
				</Button>
			</Form>
			<SkillModalComponent
				show={showModal}
				handleClose={() => handleClose()}
				handleConfirm={handleModifyConfirm}
				skills={userSkills}
			/>
		</Container>
	);
};

export default ModifyPortfolio;
