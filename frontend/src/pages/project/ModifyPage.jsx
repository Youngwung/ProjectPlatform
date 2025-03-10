import React from 'react';
import { useParams } from 'react-router-dom';
import ModifyComponent from '../../components/project/ModifyComponent';

export default function ModifyPage() {
	const {projectId} = useParams();
	return (
		<div>
			<ModifyComponent projectId = {projectId}/>
		</div>
	)
}
