import React, { useCallback } from "react";
import { Outlet, useNavigate } from "react-router-dom";

const Indexpage = () => {
	// 동적 데이터 처리(페이징 처리 관련 변수 유지 등)
	const navigate = useNavigate();

	const handleClickList = useCallback(() => {
		navigate({ pathname: "list" });
	}, [navigate]);

	const handleClickAdd = useCallback(() => {
		navigate({ pathname: "add" });
	}, [navigate]);

	return (
		// JoinProject 페이지에서만 보여줄 전체 레이아웃을 적용
		<div>
			<div>
				<div
					onClick={handleClickList}
				>
					LIST
				</div>
				<div
					onClick={handleClickAdd}
				>
					ADD
				</div>
			</div>
			<div>
				<Outlet />
			</div>
		</div>
	);
};

export default Indexpage;
