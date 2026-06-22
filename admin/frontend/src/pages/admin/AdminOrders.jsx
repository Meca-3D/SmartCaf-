import { useEffect, useState } from 'react';
import { getAdminOrders, updateOrderStatus } from '../../services/api';
import './AdminOrders.css';

const STATUS_OPTIONS = ['PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'];

const STATUS_LABELS = {
  PENDING: 'En attente',
  IN_PROGRESS: 'En cours',
  COMPLETED: 'Complétée',
  CANCELLED: 'Annulée',
};

const STATUS_BADGE_CLASS = {
  PENDING: 'badge-warning',
  IN_PROGRESS: 'badge-info',
  COMPLETED: 'badge-success',
  CANCELLED: 'badge-danger',
};

const formatDate = (dateStr) => {
  if (!dateStr) return '—';
  return new Date(dateStr).toLocaleString('fr-FR', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
  });
};

const AdminOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filterStatus, setFilterStatus] = useState('');
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [updatingId, setUpdatingId] = useState(null);

  // Silent refresh (no loading spinner)
  const refresh = async () => {
    try {
      const data = await getAdminOrders();
      setOrders(data);
    } catch { /* ignore */ }
  };

  // Poll every 5s for real-time updates
  useEffect(() => {
    const interval = setInterval(refresh, 5000);
    return () => clearInterval(interval);
  }, []);

  // Keep detail modal in sync with server updates
  useEffect(() => {
    if (selectedOrder) {
      const updated = orders.find((o) => o.id === selectedOrder.id);
      if (updated) setSelectedOrder(updated);
    }
  }, [orders]);

  const load = async () => {
    setLoading(true);
    try {
      const data = await getAdminOrders();
      setOrders(data);
    } catch {
      setError('Impossible de charger les commandes. Vérifiez la connexion à la base de données.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleStatusChange = async (orderId, newStatus) => {
    setUpdatingId(orderId);
    try {
      await updateOrderStatus(orderId, newStatus);
      setOrders((prev) =>
        prev.map((o) => o.id === orderId ? { ...o, status: newStatus } : o)
      );
      if (selectedOrder?.id === orderId) {
        setSelectedOrder((prev) => ({ ...prev, status: newStatus }));
      }
    } catch {
      setError('Erreur lors de la mise à jour du statut.');
    } finally {
      setUpdatingId(null);
    }
  };

  const filtered = filterStatus
    ? orders.filter((o) => o.status === filterStatus)
    : orders;

  const totalRevenue = orders
    .filter((o) => o.status === 'COMPLETED')
    .reduce((sum, o) => sum + (Number(o.totalPrice) || 0), 0);

  if (loading) return <div className="admin-loading">Chargement des commandes...</div>;

  return (
    <div className="admin-orders">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">Commandes</h1>
          <p className="admin-page-subtitle">
            {orders.length} commande{orders.length > 1 ? 's' : ''} — Revenu total : <strong>{totalRevenue.toFixed(2)} €</strong>
          </p>
        </div>
      </div>

      {error && <div className="admin-error">{error}</div>}

      {/* Status filters */}
      <div className="orders-filters">
        <button
          className={`filter-chip ${filterStatus === '' ? 'active' : ''}`}
          onClick={() => setFilterStatus('')}
        >
          Toutes ({orders.length})
        </button>
        {STATUS_OPTIONS.map((s) => (
          <button
            key={s}
            className={`filter-chip filter-chip--${s.toLowerCase()} ${filterStatus === s ? 'active' : ''}`}
            onClick={() => setFilterStatus(s)}
          >
            {STATUS_LABELS[s]} ({orders.filter((o) => o.status === s).length})
          </button>
        ))}
      </div>

      {/* Orders table */}
      <div className="admin-table-wrapper">
        <table className="admin-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Client</th>
              <th>Date</th>
              <th>Type</th>
              <th>Articles</th>
              <th>Total</th>
              <th>Statut</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.length === 0 ? (
              <tr>
                <td colSpan={8} className="empty-row">Aucune commande trouvée</td>
              </tr>
            ) : filtered.map((order) => (
              <tr key={order.id}>
                <td><span className="order-id">#{order.id}</span></td>
                <td>{order.customerName || `Client #${order.userId}` || '—'}</td>
                <td className="date-cell">{formatDate(order.createdAt)}</td>
                <td>
                  <div className="order-type-cell">
                    {order.orderType && (
                      <span className="badge badge-gray">
                        {order.orderType === 'ON_SITE' ? '🪑 Sur place'
                          : order.orderType === 'CLICK_AND_COLLECT' ? '🥡 Click & Collect'
                          : order.orderType}
                      </span>
                    )}
                    {(order.tableNumber ?? order.tableId) != null && (
                      <span className="table-number-badge">Table #{order.tableNumber ?? order.tableId}</span>
                    )}
                  </div>
                </td>
                <td>
                  <span className="items-count">{order.items?.length ?? 0} article{(order.items?.length ?? 0) > 1 ? 's' : ''}</span>
                </td>
                <td><strong>{order.totalPrice ? `${Number(order.totalPrice).toFixed(2)} €` : '—'}</strong></td>
                <td>
                  <span className={`badge ${STATUS_BADGE_CLASS[order.status] || 'badge-gray'}`}>
                    {STATUS_LABELS[order.status] || order.status || '—'}
                  </span>
                </td>
                <td>
                  <div className="table-actions">
                    <button className="btn-edit" onClick={() => setSelectedOrder(order)}>Détails</button>
                    <select
                      className="status-select"
                      value={order.status || ''}
                      onChange={(e) => handleStatusChange(order.id, e.target.value)}
                      disabled={updatingId === order.id}
                    >
                      <option value="" disabled>Statut</option>
                      {STATUS_OPTIONS.map((s) => (
                        <option key={s} value={s}>{STATUS_LABELS[s]}</option>
                      ))}
                    </select>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Order Detail Modal */}
      {selectedOrder && (
        <div className="modal-overlay" onClick={() => setSelectedOrder(null)}>
          <div className="modal modal--large" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">Commande #{selectedOrder.id}</h2>
              <button className="modal-close" onClick={() => setSelectedOrder(null)}>✕</button>
            </div>

            <div className="order-detail-grid">
              <div className="order-detail-info">
                <div className="order-detail-row">
                  <span className="detail-label">Client</span>
                  <span>{selectedOrder.customerName || `Utilisateur #${selectedOrder.userId}` || '—'}</span>
                </div>
                <div className="order-detail-row">
                  <span className="detail-label">Date</span>
                  <span>{formatDate(selectedOrder.createdAt)}</span>
                </div>
                <div className="order-detail-row">
                  <span className="detail-label">Type</span>
                  <span>{selectedOrder.orderType || '—'}</span>
                </div>
                {(selectedOrder.tableNumber ?? selectedOrder.tableId) != null && (
                  <div className="order-detail-row">
                    <span className="detail-label">Table</span>
                    <span className="table-number">🪑 Table #{selectedOrder.tableNumber ?? selectedOrder.tableId}</span>
                  </div>
                )}
                <div className="order-detail-row">
                  <span className="detail-label">Statut</span>
                  <div className="status-change">
                    <span className={`badge ${STATUS_BADGE_CLASS[selectedOrder.status] || 'badge-gray'}`}>
                      {STATUS_LABELS[selectedOrder.status] || selectedOrder.status}
                    </span>
                    <select
                      className="status-select"
                      value={selectedOrder.status || ''}
                      onChange={(e) => handleStatusChange(selectedOrder.id, e.target.value)}
                      disabled={updatingId === selectedOrder.id}
                    >
                      {STATUS_OPTIONS.map((s) => (
                        <option key={s} value={s}>{STATUS_LABELS[s]}</option>
                      ))}
                    </select>
                  </div>
                </div>
              </div>
            </div>

            {/* Items */}
            <h3 className="order-items-title">Articles commandés</h3>
            {selectedOrder.items && selectedOrder.items.length > 0 ? (
              <div className="order-items-list">
                {selectedOrder.items.map((item) => (
                  <div key={item.id} className="order-item-row">
                    <div className="order-item-info">
                      <span className="order-item-name">{item.product?.name || `Produit #${item.product?.id}`}</span>
                      <span className="order-item-category">{item.product?.category}</span>
                    </div>
                    <div className="order-item-qty">x{item.quantity}</div>
                    <div className="order-item-price">{Number(item.price).toFixed(2)} € / unité</div>
                    <div className="order-item-total">
                      {(Number(item.price) * item.quantity).toFixed(2)} €
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p className="no-items">Aucun article dans cette commande.</p>
            )}

            <div className="order-total-bar">
              <span>Total</span>
              <strong>{selectedOrder.totalPrice ? `${Number(selectedOrder.totalPrice).toFixed(2)} €` : '—'}</strong>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminOrders;
