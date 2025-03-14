import { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = () => <div>Loading...</div>; // ✅ 함수형 컴포넌트로 변경
const PortfolioListPage = lazy(() => import("../pages/portfolio/ListPage"));
const PortfolioDetail = lazy(() =>
	import("../pages/portfolio/PortfolioDetail")
);
const CreatePortfolio = lazy(() =>
	import("../pages/portfolio/CreatePortfolio")
);
const ModifyPortfolio = lazy(() =>
	import("../pages/portfolio/ModifyPortfolio")
);

const portfolioRouter = () => {
	return [
		{
			path: "list",
			element: (
				<Suspense fallback={<Loading />}>
					<PortfolioListPage />
				</Suspense>
			),
		},
		{
			path: "",
			element: <Navigate replace to="list" />,
		},
		{
			path: "list/:portfolioId",
			element: (
				<Suspense fallback={<Loading />}>
					<PortfolioDetail />
				</Suspense>
			),
		},
		{
			path: "create",
			element: (
				<Suspense fallback={<Loading />}>
					<CreatePortfolio />
				</Suspense>
			),
		},
		{
			path: "modify/:portfolioId",
			element: (
				<Suspense fallback={<Loading />}>
					<ModifyPortfolio />
				</Suspense>
			),
		},
		{
			path: "search",
			element: (
				<Suspense fallback={<Loading />}>
					<PortfolioListPage />
				</Suspense>
			),
		},
	];
};

export default portfolioRouter;
