import { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div>Loading...</div>;
const FindProjectListPage = lazy(() => import("../pages/findProject/ListPage"));
const FindProjectPortfolioDetail = lazy(() => import("../pages/findProject/PortfolioDetail"));
const FindProjectAddPage = lazy(() => import("../pages/findProject/CreactPortfolio"));
const FindProjectModifyPage = lazy(() => import("../pages/findProject/ModifyPortfolio"));

const findProjectRouter = () => {
  return [
    {
      path: "list",
      element: (
        <Suspense fallback={Loading}>
          <FindProjectListPage />
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
          <FindProjectPortfolioDetail />
        </Suspense>
      ),
    },
    {
      path: "add",
      element: (
        <Suspense fallback={Loading}>
          <FindProjectAddPage />
        </Suspense>
      ),
    },
    {
      path: "modify/:projectId",
      element: (
        <Suspense fallback={Loading}>
          <FindProjectModifyPage />
        </Suspense>
      ),
    },
  ];
};

export default findProjectRouter;
