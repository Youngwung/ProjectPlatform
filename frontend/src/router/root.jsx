import React, { lazy, Suspense } from "react";
import { createBrowserRouter } from "react-router-dom";
import projectRouter from "./projectRouter";
import portfolioRouter from "./portfolioRouter";
import Login from "../pages/Login";
import Signup from "../pages/Signup";

const Loading = () => <div>Loading....</div>; // JSX 요소로 변경
const Layout = lazy(() => import("../layout/Layout"));
// const ErrorPage = lazy(() => import("../pages/ErrorPage"));
const Main = lazy(() => import("../pages/Main"));
const ProjectIndex = lazy(() => import("../pages/project/IndexPage"));
const Portfolio = lazy(() => import("../pages/portfolio/IndexPage"));
const MyPage = lazy(() => import("../pages/MyPage"));
const Link = lazy(() => import("../pages/Link"));
console.log("portfolioRouter:", portfolioRouter());
console.log("projectRouter:", projectRouter());


const root = createBrowserRouter([
  {
    path: "/",
    element: (
      <Suspense fallback={<Loading />}>
        <Layout />
      </Suspense>
    ),
    // errorElement: (
    //   <Suspense fallback={<Loading />}>
    //     <ErrorPage />
    //   </Suspense>
    // ), // 에러 페이지 처리
    children: [
      {
        path: "/",
        element: (
          <Suspense fallback={<Loading />}>
            <Main />
          </Suspense>
        ),
      },
      {
        path: "/portfolio",
        element: (
          <Suspense fallback={<Loading />}>
            <Portfolio />
          </Suspense>
        ),
        children: portfolioRouter(), // ✅ 함수 호출 확인
      },
      {
        path: "/project",
        element: (
          <Suspense fallback={<Loading />}>
            <ProjectIndex />
          </Suspense>
        ),
        children: projectRouter(), // ✅ 함수 호출 확인
      },
      {
        path: "/mypage",
        element: (
          <Suspense fallback={<Loading />}>
            <MyPage />
          </Suspense>
        ),
      },
      {
        path: "/link",
        element: (
          <Suspense fallback={<Loading />}>
            <Link />
          </Suspense>
        ),
      },
      {
        path: "/login",
        element: (
          <Suspense fallback={<Loading />}>
            <Login />
          </Suspense>
        ),
      },
      {
        path: "/signup",
        element: (
          <Suspense fallback={<Loading />}>
            <Signup />
          </Suspense>
        ),
      },
    ],
  },
]);

export default root;
