import React, { useEffect, useState } from "react";
import { Alert, Form } from "react-bootstrap";
import { getProjectValid } from "../../api/validationApi";
import {
	getDuplicatedString,
	getTailwindClassByLevel,
	parseSkills,
	validateSkillsString,
} from "./SkillTagUtil";

export default function InputSkillComponent({ onValidationComplete, skills }) {
	const [skillsInput, setSkillsInput] = useState("");
	const [isValidating, setIsValidating] = useState(false);
	const [validationError, setValidationError] = useState("");
	const [isValid, setIsValid] = useState(false);

	// 스킬 입력 창 변경 감지 핸들러
	const handleInputChange = (e) => {
		const newValue = e.target.value;
		setSkillsInput(newValue);
		setIsValid(false); // 입력값이 변경되면 검증 상태 초기화
		setValidationError("");

		// 입력창이 변경될 때 마다 부모 컴포넌트에 알림
		onValidationComplete({
			isValid: false,
			value:newValue
		})
		
	};

	// 통합 검증 처리
	const handleValidation = async (event) => {
		const input = event.target.value;
		setSkillsInput(input);
		// 검사 중인 경우 로딩창을 표시하기 위한 변수 초기화
		setIsValidating(true);
		// 에러메세지 초기화
		setValidationError("");
		try {
			// 입력창이 비어있는 지 검사
			if (!skillsInput.trim()) {
				setValidationError("입력값이 없습니다.");
				return;
			}

			// 정규식 검사
			if (!validateSkillsString(skillsInput)) {
				setValidationError(
					"형식이 올바르지 않습니다. 예: #React:중급, #Spring:고급"
				);
				setIsValid(false);
				return;
			}

			// 중복 검사
			if (getDuplicatedString(skillsInput).length !== 0) {
				setValidationError(`${getDuplicatedString(skillsInput)}: 중복된 기술이 있습니다`);
				setIsValid(false);
				return;
			}

			// DB 단어 검사
			const result = await getProjectValid({ skills: skillsInput });
			console.log(result.isValid)
			if (!result.isValid) {
        setValidationError(`${result.wrongString}: 등록되지 않은 기술이 포함되어 있습니다`);
        setIsValid(false);
        return;
      }

      // 모든 검사 통과
      setIsValid(true);
			// 에러 메세지 초기화
      setValidationError("");

		} catch (error) {
			setValidationError(error.message);
			console.log(error);
			setIsValid(false);
		} finally {
			// 검증 과정 종료
			setIsValidating(false);
		}
	};

	// 검증 결과가 변경될 때마다 부모 컴포넌트에 알림
	useEffect(() => {
		onValidationComplete &&
			onValidationComplete({
				isValid,
				value: skillsInput,
			});
	}, [isValid, skillsInput, onValidationComplete]);

	// 처음 컴포넌트를 마운트 할 때 기존 스킬 데이터를 불러오기 위한 훅
	useEffect(() => {
		if (skills){
			setSkillsInput(skills)
		}
	}, [skills])
	

	return (
		<Form.Group className="mb-3">
			<Form.Label>기술</Form.Label>
			<Form.Control
				type="text"
				name="skills"
				value={skillsInput}
				onChange={handleInputChange}
				onBlur={handleValidation}
				placeholder="#기술:숙련도, #기술:숙련도"
			/>

			{/* 검사 중일 때 표시할 로딩 창 */}
			{isValidating && (
				<div className="absolute right-2 top-2">
					<div className="animate-spin h-5 w-5 border-2 border-blue-500 rounded-full border-t-transparent">검증 중...</div>
				</div>
			)}

			{/* 에러메세지가 존재할 경우 */}
			{validationError && (
				<Alert className="my-2" variant="danger">{validationError}</Alert>
			)}

			{/* 파싱된 스킬 표시 */}
			<div className="d-flex flex-wrap gap-2 mt-2">
				<span className="my-2">태그 미리보기:</span>
				{parseSkills(skillsInput).map((skill, index) => (
					<span
						key={index}
						className={`d-inline-flex align-items-center px-3 py-1 rounded 
						${getTailwindClassByLevel(skill.level)}`}
					>
						<code className="me-2">{skill.language}</code>
					</span>
				))}
			</div>
		</Form.Group>
	);
}
