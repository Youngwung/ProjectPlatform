import React, { useEffect, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import portfolioApi from "../../api/portfolioApi";
import { getUserSkill, putUserSkill } from "../../api/skillApi";
import SkillModalComponent from "../../components/skill/SkillModalComponent";
import SkillTagComponent from "../../components/skill/SkillTagComponent";
import SkillTagGuideComponent from "../../components/skill/SkillTagGuideComponent";

//TODO link입력폼 없애고조회할때 유저의 링크를 받아오기
const CreatePortfolio = () => {
	const navigate = useNavigate();
	const [title, setTitle] = useState("");
	const [description, setDescription] = useState("");
	const [skills, setSkills] = useState("");
	const [links, setLinks] = useState("");

	const handleSubmit = async (e) => {
		e.preventDefault();
		const portfolioData = {
			title,
			description,
			links,
		};

		//console.log("포트폴리오 데이터:", portfolioData);

		try {
			// ✅ JWT 기반 인증이므로 userId 제거하고 요청
			const response = await portfolioApi.createProject(portfolioData);
			//console.log("포트폴리오 저장 성공:", response);
			alert("포트폴리오가 성공적으로 저장되었습니다!");
			navigate("/portfolio/list");
		} catch (error) {
			console.error("포트폴리오 저장 실패:", error);
			alert("포트폴리오 저장 중 문제가 발생했습니다. 다시 시도해주세요.");
		}
	};

	// 스킬 가져오기 관련 로직
	const [userSkills, setUserSkills] = useState("");
	const [showModal, setShowModal] = useState(false);
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
    })
    setShowModal(false);
  };

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


	return (
		<Container className="mt-4">
			<h1 className="text-center mb-4">포트폴리오 작성</h1>
			<Form onSubmit={handleSubmit}>
				<Form.Group className="mb-3">
					<Form.Label>제목</Form.Label>
					<Form.Control
						type="text"
						placeholder="포트폴리오 제목"
						value={title}
						onChange={(e) => setTitle(e.target.value)}
					/>
				</Form.Group>

				<Form.Group className="mb-3">
					<Form.Label>설명</Form.Label>
					<Form.Control
						as="textarea"
						rows={4}
						placeholder="포트폴리오 설명"
						value={description}
						onChange={(e) => setDescription(e.target.value)}
					/>
				</Form.Group>

				<SkillTagGuideComponent />
				<Form.Group className="mb-1 border rounded px-2 py-2">
					<SkillTagComponent skills={userSkills} />
				</Form.Group>
				<Button className="mb-2" onClick={handleSkillModifyClick}>
					기술 수정
				</Button>

				<Form.Group className="mb-3">
					<Form.Label>링크 ID</Form.Label>
					<Form.Control
						type="text"
						placeholder="링크 ID 입력 (선택)"
						value={links}
						onChange={(e) => setLinks(e.target.value)}
					/>
				</Form.Group>

				<Button variant="primary" type="submit">
					저장
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

export default CreatePortfolio;
