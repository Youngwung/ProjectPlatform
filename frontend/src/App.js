import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { RouterProvider } from 'react-router-dom';
import './index.css';
import root from './router/root';
import './style/style.css';

const App = () => {
  return (
    <RouterProvider router={root}/>
  )
}
export default App