import React from 'react'
import { Container } from 'react-bootstrap'
import { Outlet } from 'react-router-dom'
import Footer from './Footer'
import Top from './Top'

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