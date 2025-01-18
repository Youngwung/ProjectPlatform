import React from 'react';
import { useParams } from 'react-router-dom';

export default function ModifyPage() {
	const {jPno} = useParams();
	return (
		<div>ModifyPage 수정할 글 번호: {jPno}</div>
	)
}
