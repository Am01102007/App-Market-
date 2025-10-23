import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './index.css'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import ForgotPassword from './pages/ForgotPassword.jsx'
import Dashboard from './pages/Dashboard.jsx'
import Catalog from './pages/Catalog.jsx'
import Chat from './pages/Chat.jsx'
import Publish from './pages/Publish.jsx'
import ProductDetail from './pages/ProductDetail.jsx'
import RequireAuth from './components/RequireAuth.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot" element={<ForgotPassword />} />
        <Route path="/dashboard" element={<RequireAuth><Dashboard /></RequireAuth>} />
        <Route path="/catalog" element={<RequireAuth><Catalog /></RequireAuth>} />
        <Route path="/product/:id" element={<RequireAuth><ProductDetail /></RequireAuth>} />
        <Route path="/chat" element={<RequireAuth><Chat /></RequireAuth>} />
        <Route path="/publish" element={<RequireAuth><Publish /></RequireAuth>} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
)