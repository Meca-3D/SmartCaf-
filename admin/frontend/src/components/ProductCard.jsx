import './ProductCard.css';

export function ProductCard({ product, onAddToCart }) {
  return (
    <div className="product-card">
      <div className="product-image">
        {product.imageUrl ? (
          <img src={product.imageUrl} alt={product.name} />
        ) : (
          <div className="no-image">Pas d'image</div>
        )}
      </div>
      <div className="product-info">
        <h3 className="product-name">{product.name}</h3>
        <p className="product-description">{product.description}</p>
        <div className="product-footer">
          <span className="product-price">{product.price}€</span>
          <button 
            className="add-to-cart-btn"
            onClick={() => onAddToCart(product)}
            disabled={product.stock === 0}
          >
            {product.stock > 0 ? 'Ajouter au panier' : 'Rupture de stock'}
          </button>
        </div>
      </div>
    </div>
  );
}
