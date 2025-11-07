/**
 * Utilidades de autenticación en cliente.
 * Gestiona token y nombre de usuario en almacenamiento local.
 */
export const getToken = () => localStorage.getItem('token') || ''
export const setToken = (token) => localStorage.setItem('token', token || '')
export const clearToken = () => localStorage.removeItem('token')
export const isAuthenticated = () => !!getToken()
export const setUsername = (username) => localStorage.setItem('username', username || '')
export const getUsername = () => localStorage.getItem('username') || ''
export const clearUsername = () => localStorage.removeItem('username')
