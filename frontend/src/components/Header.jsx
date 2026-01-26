import { Link } from 'react-router-dom';
import { useCartStore } from '../store/cartStore';
import './Header.css';

export function Header() {
  const cartItems = useCartStore((state) => state.items);
  const itemCount = cartItems.reduce((total, item) => total + item.quantity, 0);

  return (
    <header className="header">
      <div className="header-content">
        <Link to="/" className="logo">
          <h1>SmartCaf</h1>
        </Link>
        <nav className="nav">
          <Link to="/" className="nav-link">Accueil</Link>
          <Link to="/products" className="nav-link">Produits</Link>
          <Link to="/cart" className="nav-link cart-link">
            Panier
            {itemCount > 0 && <span className="cart-badge">{itemCount}</span>}
          </Link>
        </nav>
      </div>
    </header>
  );
}
