import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { RouterProvider } from 'react-router-dom';
import './index.css';
import root from './router/root';
import './style/style.css';
import { AlertProvider } from './context/AlertContext';

const App = () => {
  return (
    <AlertProvider>
    <RouterProvider router={root} />
    </AlertProvider>
  )
}
export default App