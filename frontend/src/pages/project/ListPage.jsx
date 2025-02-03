import React from 'react';
import ListComponent from '../../components/project/ListComponent';
import useCustomMove from '../../hooks/useCustomMove';

export default function ListPage() {

	const {page, size} = useCustomMove();

	return (
		<div>
			<div>
				<ListComponent />
			</div>
		</div>
	)
}
