import React from 'react';
import { useParams } from 'react-router-dom';
import ModifyComponent from '../../components/joinProject/ModifyComponent';

export default function ModifyPage() {
	const {jpNo} = useParams();
	return (
		<div>
			<ModifyComponent jpNo = {jpNo}/>
		</div>
	)
}
