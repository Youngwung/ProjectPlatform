import React, { useEffect, useState } from "react";
import { Star, StarFill } from "react-bootstrap-icons";
import authApi from "../../api/authApi";
import {
  checkBookmarkProject,
  deleteBookmarkProjectOne,
  postBookmarkProjectAdd,
} from "../../api/bookmarkProjectApi";
export default function BookmarkProjectBtn({ projectId }) {
	const [bookmarkId, setBookmarkId] = useState(null);
	const [refresh, setRefresh] = useState(false);
	const [isLoading, setIsLoading] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

	// ✅ 로그인 상태 확인
	useEffect(() => {
		const checkAuth = async () => {
			try {
				const rs = await authApi.getAuthenticatedUser(); // 🔥 사용자 정보 가져오기
				console.log(rs);
				if (rs === null) {
					setIsAuthenticated(false);
				} else {
					setIsAuthenticated(true);
				}
			} catch (error) {
				console.log(error);
			}
		};
		checkAuth();
	}, []);

	useEffect(() => {
		if (!isAuthenticated) return;
		const checkBookmarkStatus = async () => {
			try {
				const response = checkBookmarkProject(projectId);
				response.then((result) => {
					console.log(result);
					setBookmarkId(result || null);
				});
			} catch (error) {
				console.log(error);
			}
		};
		checkBookmarkStatus();
	}, [projectId, isAuthenticated, refresh]);
	// 즐겨찾기 토글 처리
	const handleFavoriteToggle = async () => {
		if (isLoading) return;

		setIsLoading(true);
		const previousState = bookmarkId;

		try {
			if (!previousState) {
				await postBookmarkProjectAdd(projectId);
				setRefresh(!refresh);
			} else {
				await deleteBookmarkProjectOne(bookmarkId);
				setRefresh(!refresh);
			}
		} catch (error) {
			console.error("Error toggling favorite:", error);
			setBookmarkId(previousState); // 실패 시 상태 복원
			alert("처리 중 오류가 발생했습니다.");
		} finally {
			setIsLoading(false);
		}
	};

	return (
		<button
			onClick={handleFavoriteToggle}
			disabled={isLoading}
			className="p-1 hover:opacity-75 transition-opacity"
			aria-label={bookmarkId ? "Remove from favorites" : "Add to favorites"}
		>
			{bookmarkId ? (
				<StarFill className="text-yellow-400 w-6 h-6" />
			) : (
				<Star className="text-gray-400 w-6 h-6" />
			)}
		</button>
	);
}
