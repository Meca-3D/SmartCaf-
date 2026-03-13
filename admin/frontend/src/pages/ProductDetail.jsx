import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useCartStore } from '../store/cartStore';
import { getProduct } from '../services/api';
import './ProductDetail.css';

export function ProductDetail() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [quantity, setQuantity] = useState(1);
  const addToCart = useCartStore((state) => state.addItem);

  useEffect(() => {
    fetchProduct();
  }, [id]);

  const fetchProduct = async () => {
    try {
      setLoading(true);
      const data = await getProduct(id);
      setProduct(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = () => {
    addToCart(product, quantity);
  };

  if (loading) return <div className="loading">Chargement...</div>;
  if (!product) return <div className="error">Produit non trouvé</div>;

  return (
    <div className="product-detail">
      <div className="product-detail-image">
        {product.imageUrl ? (
          <img src={product.imageUrl} alt={product.name} />
        ) : (
          <div className="no-image">Pas d'image</div>
        )}
      </div>
      <div className="product-detail-info">
        <h1>{product.name}</h1>
        <p className="category">{product.category}</p>
        <p className="price">{product.price}€</p>
        <p className="description">{product.description}</p>
        <p className="stock">Stock: {product.stock} unités</p>
        
        <div className="add-to-cart-section">
          <input
            type="number"
            min="1"
            max={product.stock}
            value={quantity}
            onChange={(e) => setQuantity(parseInt(e.target.value))}
            className="quantity-input"
          />
          <button
            onClick={handleAddToCart}
            disabled={product.stock === 0}
            className="add-btn"
          >
            Ajouter au panier
          </button>
        </div>
      </div>
    </div>
  );
}
