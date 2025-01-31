import React, { useState } from "react";
import { Form } from "react-bootstrap";
import {
	getTailwindClassByLevel,
	parseSkills,
	validateSkillsString,
} from "./SkillTagUtil";

export default function InputSkillComponent({
	onChange,
	onValidation,
	label = "기술",
}) {
	// 원본 저장용 변수
	const [rawValue, setRawValue] = useState("");
	// 출력 용 변수
  const [displayValue, setDisplayValue] = useState('');
	// 정규식 검사 통과 여부 저장용 변수
	const [isValid, setIsValid] = useState(true);

	// 콤마 입력 이벤트 핸들러
	const handleCommaInput = (e) => {
    const inputValue = e.target.value;
    // 원본 값 저장
    setRawValue(inputValue);
    
    // 콤마 입력 시 포맷팅
    if (inputValue.endsWith(',')) {
      const formattedValue = inputValue.slice(0, -1) + ', ';
      setDisplayValue(formattedValue);
    } else {
      setDisplayValue(inputValue);
    }
		onChange(e);
  };

	// Focus out 이벤트 핸들러
	const handleFocusOut = () => {
		const valid = validateSkillsString(displayValue);
		setIsValid(valid);
		if (onValidation) {
			onValidation(valid);
		}
	};

	return (
		<Form.Group className="mb-3">
			<Form.Label>{label}</Form.Label>
			<Form.Control
				type="text"
				name="skill"
				value={rawValue}
				onChange={handleCommaInput}
				onBlur={handleFocusOut}
				placeholder="#기술:숙련도, #기술:숙련도"
				isInvalid={!isValid}
			/>
			<Form.Control.Feedback type="invalid">
				올바른 형식으로 입력해주세요. (예: #React:중급, #Python:고급)
			</Form.Control.Feedback>

			{/* 파싱된 스킬 표시 */}
			<div className="d-flex flex-wrap gap-2 mt-2">
				{parseSkills(displayValue).map((skill, index) => (
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
