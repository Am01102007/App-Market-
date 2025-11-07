import api from './api'

export async function fetchAllProducts() {
  const { data } = await api.get('/products')
  return data
}

export async function fetchActiveProducts() {
  const { data } = await api.get('/products/active')
  return data
}

export async function fetchProductById(id) {
  const { data } = await api.get(`/products/${id}`)
  return data
}

export async function fetchProductsByCategory(category) {
  const { data } = await api.get(`/products/category/${category}`)
  return data
}

export async function fetchLikesCount(id) {
  const { data } = await api.get(`/products/${id}/likes/count`)
  return data
}

export async function fetchComments(id) {
  const { data } = await api.get(`/products/${id}/comments`)
  return data
}

export async function createProduct(product, username) {
  const { data } = await api.post('/products/create', product, { params: { username } })
  return data
}

export async function updateProduct(id, product) {
  const { data } = await api.put(`/products/${id}`, product)
  return data
}

export async function deleteProduct(id) {
  const { data } = await api.delete(`/products/${id}`)
  return data
}

export async function fetchCategories() {
  const { data } = await api.get('/products/categories')
  return data
}

export async function searchProducts(searchTerm) {
  const { data } = await api.get('/products/search', { 
    params: { q: searchTerm } 
  })
  return data
}

export async function createCategory(category) {
  const { data } = await api.post('/products/categories', category)
  return data
}

export async function updateCategory(id, category) {
  const { data } = await api.put(`/products/categories/${id}`, category)
  return data
}

export async function deleteCategory(id) {
  const { data } = await api.delete(`/products/categories/${id}`)
  return data
}
/**
 * Utilidades de productos.
 * Funciones para obtener y procesar datos de productos.
 */
