import React, { lazy, Suspense } from "react";
import { createBrowserRouter as Router } from "react-router-dom";
import findProjectRouter from "./findProjectRouter";
import projectRouter from "./projectRouter";

const Loading = <div>Loading....</div>; // 로딩 중에 보여줄 요소
const Layout = lazy(() => import("../layout/Layout"));
// const ErrorPage = lazy(() => import("../pages/ErrorPage"));
const Main = lazy(() => import("../pages/Main"));
const FindProject = lazy(() => import("../pages/findProject/IndexPage"));
const ProjectIndex = lazy(() => import("../pages/project/IndexPage"));
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
				path: "/findProject",
				element: (
					<Suspense fallback={Loading}><FindProject /></Suspense>
				),
				children: findProjectRouter(),
			},
			{
				path: "/project",
				element: (
					<Suspense fallback={Loading}><ProjectIndex /></Suspense>
				),
				children: projectRouter(),
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
