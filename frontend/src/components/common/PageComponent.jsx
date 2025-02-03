import React from "react";

import Pagination from "react-bootstrap/Pagination";

export default function PageComponent({ serverData, movePage }) {
	return (
		<div className="m-6 flex justify-center">
			<Pagination aria-label="Page navigation">
				{/* 페이지 번호 목록을 이용하여 Pagination 컴포넌트 구성 */}
				<Pagination.First
					disabled={!serverData.prev}
					onClick={() => movePage({ page: serverData.prevPage })}
				/>
				<Pagination.Prev
					onClick={() => movePage({ page: serverData.current - 1 })}
				/>
				{serverData.pageNumList.map((pageNum) => (
					<Pagination.Item
						key={pageNum}
						active={pageNum === serverData.current}
						onClick={() => movePage({ page: pageNum })}
					>
						{pageNum}
					</Pagination.Item>
				))}
				<Pagination.Next
					onClick={() => movePage({ page: serverData.current + 1 })}
				/>
				<Pagination.Last
					disabled={!serverData.next}
					onClick={() => movePage({ page: serverData.nextPage })}
				/>
			</Pagination>
		</div>
	);
}