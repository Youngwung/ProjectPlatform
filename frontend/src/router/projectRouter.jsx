import { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div>Loading....</div>;
const ProjectListPage = lazy(() => import("../pages/project/ListPage"));
const ProjectReadPage = lazy(() => import("../pages/project/ReadPage"));
const ProjectAddPage = lazy(() => import("../pages/project/AddPage"));
const ProjectModifyPage = lazy(() => import("../pages/project/ModifyPage"));

const projectRouter = () => {
	return [
		{
			path: "list",
			element: (
				<Suspense fallback={Loading}><ProjectListPage /></Suspense>
			),
		},{
			// 인덱스 페이지로 이동 시 list 페이지로 리다이렉션
			path: "",
			element: <Navigate replace to="list"/>
		},
		{
			path: "read/:projectId",
			element: <Suspense fallback={Loading}><ProjectReadPage/></Suspense>
		},
		{
			path: "add",
			element: <Suspense fallback={Loading}><ProjectAddPage/></Suspense>
		},
		{
			path: "modify/:projectId",
			element: <Suspense fallback={Loading}><ProjectModifyPage/></Suspense>
		}
	];
};

export default projectRouter;
