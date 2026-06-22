import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import AdminLayout from './pages/admin/AdminLayout';
import AdminDashboard from './pages/admin/AdminDashboard';
import AdminProducts from './pages/admin/AdminProducts';
import AdminOrders from './pages/admin/AdminOrders';
import AdminSales from './pages/admin/AdminSales';
import AdminUsers from './pages/admin/AdminUsers';
import AdminSettings from './pages/admin/AdminSettings';
import { ProtectedAdminRoute } from './pages/admin/ProtectedAdminRoute';
import { ProtectedAdminOnlyRoute } from './pages/admin/ProtectedAdminOnlyRoute';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        {/* Routes admin — layout dédié, sans Header client */}
        <Route
          path="/admin"
          element={
            <ProtectedAdminRoute>
              <AdminLayout />
            </ProtectedAdminRoute>
          }
        >
          <Route index element={<AdminDashboard />} />
          <Route path="products" element={<ProtectedAdminOnlyRoute><AdminProducts /></ProtectedAdminOnlyRoute>} />
          <Route path="orders" element={<AdminOrders />} />
          <Route path="sales" element={<AdminSales />} />
          <Route path="clients" element={<ProtectedAdminOnlyRoute><AdminUsers /></ProtectedAdminOnlyRoute>} />
          <Route path="settings" element={<ProtectedAdminOnlyRoute><AdminSettings /></ProtectedAdminOnlyRoute>} />
        </Route>

        <Route path="/login" element={<Login />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}

export default App;
