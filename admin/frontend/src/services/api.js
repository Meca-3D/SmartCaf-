import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Products
export const getProducts = async () => {
  const response = await api.get('/products');
  return response.data;
};

export const getProduct = async (id) => {
  const response = await api.get(`/products/${id}`);
  return response.data;
};

export const createProduct = async (product) => {
  const response = await api.post('/products', product);
  return response.data;
};

export const updateProduct = async (id, product) => {
  const response = await api.put(`/products/${id}`, product);
  return response.data;
};

export const deleteProduct = async (id) => {
  await api.delete(`/products/${id}`);
};

export const searchProducts = async (searchTerm) => {
  const response = await api.get(`/products/search?name=${searchTerm}`);
  return response.data;
};

export const getProductsByCategory = async (category) => {
  const response = await api.get(`/products/category/${category}`);
  return response.data;
};

// ===== ADMIN - Dashboard =====
export const getDashboardStats = async () => {
  const response = await api.get('/admin/dashboard');
  return response.data;
};

// ===== ADMIN - Products =====
export const getAdminProducts = async () => {
  const response = await api.get('/admin/products');
  return response.data;
};

export const toggleProductStatus = async (id) => {
  const response = await api.put(`/admin/products/${id}/toggle-status`);
  return response.data;
};

// ===== ADMIN - Orders =====
export const getAdminOrders = async () => {
  const response = await api.get('/admin/orders');
  return response.data;
};

export const getAdminOrderById = async (id) => {
  const response = await api.get(`/admin/orders/${id}`);
  return response.data;
};

export const updateOrderStatus = async (id, status) => {
  const response = await api.put(`/admin/orders/${id}/status`, { status });
  return response.data;
};

// ===== ADMIN - Sales =====
export const getSalesByProduct = async () => {
  const response = await api.get('/admin/sales/by-product');
  return response.data;
};

// ===== ADMIN - Categories =====
export const getAdminCategories = async () => {
  const response = await api.get('/admin/categories');
  return response.data;
};

export const createAdminCategory = async (category) => {
  const response = await api.post('/admin/categories', category);
  return response.data;
};

export const updateAdminCategory = async (id, category) => {
  const response = await api.put(`/admin/categories/${id}`, category);
  return response.data;
};

export const deleteAdminCategory = async (id) => {
  await api.delete(`/admin/categories/${id}`);
};

// ===== ADMIN - Batch delete products =====
export const deleteProductsBatch = async (ids) => {
  const response = await api.delete('/admin/products/batch', { data: ids });
  return response.data;
};

// ===== ADMIN - Users =====
export const getAdminUsers = async () => {
  const response = await api.get('/admin/users');
  return response.data;
};

export const updateUserRole = async (id, role) => {
  const response = await api.put(`/admin/users/${id}/role`, { role });
  return response.data;
};

export const deleteAdminUser = async (id) => {
  await api.delete(`/admin/users/${id}`);
};

export const banAdminUser = async (id) => {
  const response = await api.put(`/admin/users/${id}/ban`);
  return response.data;
};

export const getAdminUserOrders = async (userId) => {
  const response = await api.get(`/admin/users/${userId}/orders`);
  return response.data;
};

// ===== ADMIN - Change password =====
export const changeAdminPassword = async (email, currentPassword, newPassword) => {
  const response = await api.put('/auth/change-password', { email, currentPassword, newPassword });
  return response.data;
};

// ===== ADMIN - Create employer account =====
export const createEmployerAccount = async (data) => {
  const response = await api.post('/admin/users/employer', data);
  return response.data;
};

// ===== ADMIN - Purge old orders =====
export const purgeOldOrders = async (days = 90) => {
  const response = await api.delete(`/admin/orders/purge?days=${days}`);
  return response.data;
};

