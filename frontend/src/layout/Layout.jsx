import React from 'react'
import { Container} from 'react-bootstrap'
import Top from './Top'
import Footer from './Footer'
import { Outlet } from 'react-router-dom'

const Layout = () => {
  return (
    <>
        <Top/>
        <Container>
          <Outlet/>
        </Container>
        <Footer/>
    </>
  )
}

export default Layout