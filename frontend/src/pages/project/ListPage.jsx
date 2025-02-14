import React from 'react';
import ListComponent from '../../components/project/ListComponent';
import SearchBar from '../../components/search/SearchBar';

export default function ListPage() {

	return (
		<div>
			<div>
				<SearchBar />
			</div>
			<div>
				<ListComponent />
			</div>
		</div>
	)
}
