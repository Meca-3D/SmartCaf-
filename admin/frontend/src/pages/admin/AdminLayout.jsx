import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useUserStore } from '../../store/userStore';
import './AdminLayout.css';

const AdminLayout = () => {
  const { user, logout } = useUserStore();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <div className="admin-logo">
          <span className="admin-logo-icon">☕</span>
          <span className="admin-logo-text">SmartCafé</span>
          <span className="admin-logo-badge">Admin</span>
        </div>

        <nav className="admin-nav">
          <NavLink to="/admin" end className={({ isActive }) => isActive ? 'admin-nav-link active' : 'admin-nav-link'}>
            <span className="nav-icon">📊</span>
            <span>Dashboard</span>
          </NavLink>
          {user?.role === 'ADMIN' && (
            <NavLink to="/admin/products" className={({ isActive }) => isActive ? 'admin-nav-link active' : 'admin-nav-link'}>
              <span className="nav-icon">📦</span>
              <span>Produits</span>
            </NavLink>
          )}
          <NavLink to="/admin/orders" className={({ isActive }) => isActive ? 'admin-nav-link active' : 'admin-nav-link'}>
            <span className="nav-icon">🛒</span>
            <span>Commandes</span>
          </NavLink>
          <NavLink to="/admin/sales" className={({ isActive }) => isActive ? 'admin-nav-link active' : 'admin-nav-link'}>
            <span className="nav-icon">📈</span>
            <span>Statistiques</span>
          </NavLink>
          {user?.role === 'ADMIN' && (
            <NavLink to="/admin/clients" className={({ isActive }) => isActive ? 'admin-nav-link active' : 'admin-nav-link'}>
              <span className="nav-icon">👥</span>
              <span>Clients</span>
            </NavLink>
          )}
          {user?.role === 'ADMIN' && (
            <NavLink to="/admin/settings" className={({ isActive }) => isActive ? 'admin-nav-link active' : 'admin-nav-link'}>
              <span className="nav-icon">⚙️</span>
              <span>Paramètres</span>
            </NavLink>
          )}
        </nav>

        <div className="admin-sidebar-footer">
          <div className="admin-user-info">
            <span className="admin-user-avatar">👤</span>
            <div>
              <p className="admin-user-name">{user?.firstName} {user?.lastName}</p>
              <p className="admin-user-role">{user?.role === 'EMPLOYER' ? 'Employé' : 'Administrateur'}</p>
            </div>
          </div>
          <button className="admin-logout-btn" onClick={handleLogout}>
            Déconnexion
          </button>
        </div>
      </aside>

      <main className="admin-main">
        <Outlet />
      </main>
    </div>
  );
};

export default AdminLayout;
