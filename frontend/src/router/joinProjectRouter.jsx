import { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div>Loading....</div>;
const JProjectListPage = lazy(() => import("../pages/joinProject/ListPage"));
const JProjectReadPage = lazy(() => import("../pages/joinProject/ReadPage"));
const JProjectAddPage = lazy(() => import("../pages/joinProject/AddPage"));
const JProjectModifyPage = lazy(() => import("../pages/joinProject/ModifyPage"));

const joinProjectRouter = () => {
	return [
		{
			path: "list",
			element: (
				<Suspense fallback={Loading}><JProjectListPage /></Suspense>
			),
		},{
			// 인덱스 페이지로 이동 시 list 페이지로 리다이렉션
			path: "",
			element: <Navigate replace to="list"/>
		},
		{
			path: "read/:jPno",
			element: <Suspense fallback={Loading}><JProjectReadPage/></Suspense>
		},
		{
			path: "add",
			element: <Suspense fallback={Loading}><JProjectAddPage/></Suspense>
		},
		{
			path: "modify/:jPno",
			element: <Suspense fallback={Loading}><JProjectModifyPage/></Suspense>
		}
	];
};

export default joinProjectRouter;
