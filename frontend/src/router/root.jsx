import React from 'react'
import { createBrowserRouter as Router} from 'react-router-dom'
import FindProject from '../pages/FindProject'
import JoinProject from '../pages/JoinProject'
import Main from '../pages/Main'
import MyPage from '../pages/MyPage'
import ErrorPage from '../pages/ErrorPage'
import Layout from '../Layout/Layout'

const root = Router([
    {
        path: '/',
        element: <Layout />,
        errorElement: <ErrorPage />, // 에러 페이지 처리
        children: [
            {
                path: '/',
                element: <Main/>
            },
            {
                path: '/findProject',
                element: <FindProject />
            },
            {
                path: '/joinProject',
                element: <JoinProject />
            },
            {
                path: '/mypage',
                element: <MyPage />
            }
        ],
    },
])

export default root