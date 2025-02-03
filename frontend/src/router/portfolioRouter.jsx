import { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div>Loading...</div>;
const PortfolioListPage = lazy(() => import("../pages/portfolio/ListPage"));
const PortfolioDetail = lazy(() => import("../pages/portfolio/PortfolioDetail"));
const Createportpolio = lazy(() => import("../pages/portfolio/CreactPortfolio"));
const Modifyportpolio = lazy(() => import("../pages/portfolio/ModifyPortfolio"));

const portfolioRouter = () => {
  return [
    {
      path: "list",
      element: (
        <Suspense fallback={Loading}>
          <PortfolioListPage />
        </Suspense>
      ),
    },
    {
      path: "",
      element: <Navigate replace to="list" />,
    },
    {
      path: "list/:projectId",
      element: (
        <Suspense fallback={Loading}>
          <PortfolioDetail />
        </Suspense>
      ),
    },
    {
      path: "create",
      element: (
        <Suspense fallback={Loading}>
          <Createportpolio />
        </Suspense>
      ),
    },
    {
      path: "modify/:projectId",
      element: (
        <Suspense fallback={Loading}>
          <Modifyportpolio/>
        </Suspense>
      ),
    },
  ];
};

export default portfolioRouter;
