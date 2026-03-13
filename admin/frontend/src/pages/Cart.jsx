import { useCartStore } from '../store/cartStore';
import './Cart.css';

export function Cart() {
  const { items, removeItem, updateQuantity, clearCart, getTotal } = useCartStore();

  if (items.length === 0) {
    return (
      <div className="cart-empty">
        <h2>Votre panier est vide</h2>
        <p>Ajoutez des produits pour commencer vos achats</p>
      </div>
    );
  }

  return (
    <div className="cart">
      <h1>Panier</h1>
      <div className="cart-items">
        {items.map((item) => (
          <div key={item.id} className="cart-item">
            <div className="item-info">
              <h3>{item.name}</h3>
              <p className="item-price">{item.price}€</p>
            </div>
            <div className="item-actions">
              <input
                type="number"
                min="1"
                value={item.quantity}
                onChange={(e) => updateQuantity(item.id, parseInt(e.target.value))}
                className="quantity-input"
              />
              <button
                onClick={() => removeItem(item.id)}
                className="remove-btn"
              >
                Supprimer
              </button>
            </div>
            <div className="item-total">
              {(item.price * item.quantity).toFixed(2)}€
            </div>
          </div>
        ))}
      </div>
      
      <div className="cart-summary">
        <div className="total">
          <span>Total:</span>
          <span className="total-price">{getTotal().toFixed(2)}€</span>
        </div>
        <button className="checkout-btn">Passer la commande</button>
        <button onClick={clearCart} className="clear-btn">
          Vider le panier
        </button>
      </div>
    </div>
  );
}
