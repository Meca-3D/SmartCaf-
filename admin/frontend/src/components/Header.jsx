
import { Link } from 'react-router-dom';
import { useCartStore } from '../store/cartStore';
import { useUserStore } from '../store/userStore';
import './Header.css';

export function Header() {
  const cartItems = useCartStore((state) => state.items);
  const itemCount = cartItems.reduce((total, item) => total + item.quantity, 0);
  const user = useUserStore((state) => state.user);
  const logout = useUserStore((state) => state.logout);

  return (
    <header className="header">
      <div className="header-content">
        <Link to="/" className="logo">
          <h1>SmartCaf</h1>
        </Link>
        <nav className="nav">
          {user ? (
            <>
              <span className="nav-user">Connecté : {user.firstName} {user.lastName}</span>
              {user.role === 'ADMIN' && (
                <Link to="/admin" className="nav-link nav-link--admin">⚙ Admin</Link>
              )}
              {user.role === 'EMPLOYER' && (
                <Link to="/admin" className="nav-link nav-link--admin">👔 Espace Employé</Link>
              )}
              <button className="nav-link" onClick={logout} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'inherit' }}>Déconnexion</button>
            </>
          ) : (
            <Link to="/login" className="nav-link">Connexion</Link>
          )}
        </nav>
      </div>
    </header>
  );
}
