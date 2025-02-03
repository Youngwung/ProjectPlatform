import React, { useCallback } from "react";
import { Nav, NavDropdown } from "react-bootstrap";
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
			<Nav variant="pills" className="my-3">
				<Nav.Item>
					<Nav.Link eventKey="1" onClick={handleClickList}>
						프로젝트 리스트
					</Nav.Link>
				</Nav.Item>
				<Nav.Item>
					<Nav.Link eventKey="2" onClick={handleClickAdd}>
						프로젝트 등록
					</Nav.Link>
				</Nav.Item>
				<NavDropdown title="Dropdown" id="nav-dropdown">
					{/* 검색 옵션 설정 메뉴 있으면 좋을듯 */}
					<NavDropdown.Item eventKey="4.1">Action</NavDropdown.Item>
					<NavDropdown.Item eventKey="4.2">Another action</NavDropdown.Item>
					<NavDropdown.Item eventKey="4.3">
						Something else here
					</NavDropdown.Item>
					<NavDropdown.Divider />
					<NavDropdown.Item eventKey="4.4">Separated link</NavDropdown.Item>
				</NavDropdown>
			</Nav>
			<div>
				<Outlet />
			</div>
		</div>
	);
};

export default Indexpage;
