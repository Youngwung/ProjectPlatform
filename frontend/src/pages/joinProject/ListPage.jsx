import React from 'react';
import ListComponent from '../../components/joinProject/ListComponent';
import useCustomMove from '../../hooks/useCustomMove';

export default function ListPage() {

	const {page, size} = useCustomMove();

	return (
		<div>
			ListPage --- page = {page} --- size = {size}
			<div>
				<ListComponent />
			</div>
		</div>
	)
}
