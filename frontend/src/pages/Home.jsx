import { Link } from 'react-router-dom';
import './Home.css';

export function Home() {
  return (
    <div className="home">
      <div className="hero">
        <h1>Bienvenue sur SmartCaf</h1>
        <p>Découvrez nos produits de qualité</p>
        <Link to="/products" className="cta-button">
          Voir les produits
        </Link>
      </div>
      
      <div className="features">
        <div className="feature">
          <h3>🚀 Livraison rapide</h3>
          <p>Livraison en 24-48h</p>
        </div>
        <div className="feature">
          <h3>💳 Paiement sécurisé</h3>
          <p>Vos données sont protégées</p>
        </div>
        <div className="feature">
          <h3>📞 Support client</h3>
          <p>Disponible 7j/7</p>
        </div>
      </div>
    </div>
  );
}
