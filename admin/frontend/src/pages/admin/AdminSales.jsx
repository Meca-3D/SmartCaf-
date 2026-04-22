import { useEffect, useState } from 'react';
import { getSalesByProduct } from '../../services/api';
import './AdminSales.css';

const AdminSales = () => {
  const [sales, setSales] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filterCategory, setFilterCategory] = useState('');
  const [sortBy, setSortBy] = useState('quantity'); // 'quantity' | 'revenue'

  useEffect(() => {
    const load = async () => {
      try {
        const data = await getSalesByProduct();
        setSales(data);
      } catch {
        setError('Impossible de charger les statistiques de vente. Vérifiez la connexion à la base de données.');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const categories = [...new Set(sales.map((s) => s.productCategory).filter(Boolean))];

  const filtered = sales
    .filter((s) => filterCategory ? s.productCategory === filterCategory : true)
    .sort((a, b) => sortBy === 'quantity'
      ? b.totalQuantitySold - a.totalQuantitySold
      : b.totalRevenue - a.totalRevenue
    );

  const maxQuantity = filtered.length > 0 ? filtered[0].totalQuantitySold : 1;
  const maxRevenue = filtered.reduce((max, s) => Math.max(max, Number(s.totalRevenue)), 1);

  const totalQuantity = sales.reduce((sum, s) => sum + (s.totalQuantitySold || 0), 0);
  const totalRevenue = sales.reduce((sum, s) => sum + (Number(s.totalRevenue) || 0), 0);

  if (loading) return <div className="admin-loading">Chargement des statistiques de vente...</div>;

  return (
    <div className="admin-sales">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">Statistiques des ventes</h1>
          <p className="admin-page-subtitle">
            {sales.length} produit{sales.length > 1 ? 's' : ''} vendus —
            {' '}<strong>{totalQuantity}</strong> unités —
            {' '}<strong>{totalRevenue.toFixed(2)} €</strong> de revenu total
          </p>
        </div>
      </div>

      {error && <div className="admin-error">{error}</div>}

      {/* Controls */}
      <div className="sales-controls">
        <select
          className="filter-select"
          value={filterCategory}
          onChange={(e) => setFilterCategory(e.target.value)}
        >
          <option value="">Toutes les catégories</option>
          {categories.map((cat) => (
            <option key={cat} value={cat}>{cat}</option>
          ))}
        </select>
        <div className="sort-buttons">
          <span className="sort-label">Trier par :</span>
          <button
            className={`sort-btn ${sortBy === 'quantity' ? 'active' : ''}`}
            onClick={() => setSortBy('quantity')}
          >
            Quantité vendue
          </button>
          <button
            className={`sort-btn ${sortBy === 'revenue' ? 'active' : ''}`}
            onClick={() => setSortBy('revenue')}
          >
            Revenu généré
          </button>
        </div>
      </div>

      {filtered.length === 0 ? (
        <div className="empty-state">
          <p>Aucune donnée de vente disponible.</p>
          <p>Les statistiques apparaîtront dès que des commandes seront complétées.</p>
        </div>
      ) : (
        <>
          {/* Summary Cards */}
          <div className="sales-summary">
            <div className="summary-card">
              <p className="summary-label">Produits différents vendus</p>
              <p className="summary-value">{filtered.length}</p>
            </div>
            <div className="summary-card">
              <p className="summary-label">Unités totales vendues</p>
              <p className="summary-value">{filtered.reduce((s, i) => s + i.totalQuantitySold, 0)}</p>
            </div>
            <div className="summary-card">
              <p className="summary-label">Revenu filtré</p>
              <p className="summary-value summary-value--green">
                {filtered.reduce((s, i) => s + Number(i.totalRevenue), 0).toFixed(2)} €
              </p>
            </div>
          </div>

          {/* Sales Chart Table */}
          <div className="sales-chart-section">
            <div className="admin-table-wrapper">
              <table className="admin-table sales-table">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Produit</th>
                    <th>Catégorie</th>
                    <th>Quantité vendue</th>
                    <th>Revenu généré</th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map((item, index) => (
                    <tr key={item.productId}>
                      <td className="rank-cell">
                        <span className="rank">
                          {index === 0 ? '🥇' : index === 1 ? '🥈' : index === 2 ? '🥉' : `#${index + 1}`}
                        </span>
                      </td>
                      <td>
                        <strong className="product-name-cell">{item.productName}</strong>
                      </td>
                      <td>
                        <span className="badge badge-info">{item.productCategory || '—'}</span>
                      </td>
                      <td className="bar-cell">
                        <div className="bar-container">
                          <div
                            className="bar bar--blue"
                            style={{ width: `${(item.totalQuantitySold / maxQuantity) * 100}%` }}
                          />
                          <span className="bar-label">{item.totalQuantitySold} unité{item.totalQuantitySold > 1 ? 's' : ''}</span>
                        </div>
                      </td>
                      <td className="bar-cell">
                        <div className="bar-container">
                          <div
                            className="bar bar--green"
                            style={{ width: `${(Number(item.totalRevenue) / maxRevenue) * 100}%` }}
                          />
                          <span className="bar-label revenue-value">{Number(item.totalRevenue).toFixed(2)} €</span>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default AdminSales;
