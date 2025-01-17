import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import { RouterProvider } from 'react-router-dom';
import root from './router/root';
import './style/style.css';

const App = () => {
  return (
    <RouterProvider router={root}/>
  )
}
export default App