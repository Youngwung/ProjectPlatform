import React, { lazy, Suspense } from "react";
import { createBrowserRouter as Router } from "react-router-dom";
import joinProjectRouter from "./joinProjectRouter";
import portfolioRouter from "./portfolioRouter";

const Loading = <div>Loading....</div>; // 로딩 중에 보여줄 요소
const Layout = lazy(() => import("../layout/Layout"));
// const ErrorPage = lazy(() => import("../pages/ErrorPage"));
const Main = lazy(() => import("../pages/Main"));
const Portfolio = lazy(() => import("../pages/portfolio/IndexPage"));
const JoinProjectIndex = lazy(() => import("../pages/joinProject/IndexPage"));
const MyPage = lazy(() => import("../pages/MyPage"));
const Link = lazy(() => import("../pages/Link"));
const root = Router([
	{
		path: "/",
		element: (
			<Suspense fallback={Loading}><Layout /></Suspense>
		),
		// errorElement: (
		// 	<Suspense fallback={Loading}><ErrorPage /></Suspense>
		// ), // 에러 페이지 처리
		children: [
			{
				path: "/",
				element: (
					<Suspense fallback={Loading}><Main /></Suspense>
				),
			},
			{
				path: "/portfolio",
				element: (
					<Suspense fallback={Loading}><Portfolio /></Suspense>
				),
				children: portfolioRouter(),
			},
			{
				path: "/joinProject",
				element: (
					<Suspense fallback={Loading}><JoinProjectIndex /></Suspense>
				),
				children: joinProjectRouter(),
			},
			{
				path: "/mypage",
				element: (
					<Suspense fallback={Loading}><MyPage /></Suspense>
				),
			},
			{
				path: "/link",
				element: (
					<Suspense fallback={Loading}><Link /></Suspense>
				),
			}
		],
	},
]);

export default root;
