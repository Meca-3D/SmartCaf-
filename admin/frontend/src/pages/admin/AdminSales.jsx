import { useEffect, useState } from 'react';
import { getSalesByProduct } from '../../services/api';
import './AdminSales.css';

const AdminSales = () => {
  const [sales, setSales] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filterCategory, setFilterCategory] = useState('');
  const [sortBy, setSortBy] = useState('quantity');

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

  const maxQty = filtered.length > 0 ? filtered[0].totalQuantitySold : 1;
  const maxRev = filtered.reduce((m, s) => Math.max(m, Number(s.totalRevenue)), 1);

  const totalProducts = sales.length;
  const totalUnits = sales.reduce((s, i) => s + (i.totalQuantitySold || 0), 0);
  const totalRevenue = sales.reduce((s, i) => s + Number(i.totalRevenue || 0), 0);
  const avgRevenue = totalProducts > 0 ? totalRevenue / totalProducts : 0;

  const top3 = filtered.slice(0, 3);

  if (loading) return <div className="admin-loading">Chargement des statistiques de vente…</div>;

  return (
    <div className="admin-sales">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">Statistiques des ventes</h1>
          <p className="admin-page-subtitle">
            {totalProducts} produit{totalProducts > 1 ? 's' : ''} vend{totalProducts > 1 ? 'us' : 'u'} —{' '}
            <strong>{totalUnits}</strong> unités —{' '}
            <strong>{totalRevenue.toFixed(2)} €</strong> de revenu total
          </p>
        </div>
      </div>

      {error && <div className="admin-error">{error}</div>}

      {/* ===== KPI strip ===== */}
      <div className="sales-kpi-row">
        <div className="sales-kpi-card">
          <p className="sales-kpi-label">Produits différents vendus</p>
          <p className="sales-kpi-value">{totalProducts}</p>
        </div>
        <div className="sales-kpi-card">
          <p className="sales-kpi-label">Unités totales vendues</p>
          <p className="sales-kpi-value">{totalUnits}</p>
        </div>
        <div className="sales-kpi-card sales-kpi-card--green">
          <p className="sales-kpi-label">Revenu total</p>
          <p className="sales-kpi-value sales-kpi-value--green">{totalRevenue.toFixed(2)} €</p>
        </div>
        <div className="sales-kpi-card">
          <p className="sales-kpi-label">Revenu moyen / produit</p>
          <p className="sales-kpi-value">{avgRevenue.toFixed(2)} €</p>
        </div>
      </div>

      {/* ===== Controls ===== */}
      <div className="sales-controls">
        <select
          className="sales-filter-select"
          value={filterCategory}
          onChange={(e) => setFilterCategory(e.target.value)}
        >
          <option value="">Toutes les catégories</option>
          {categories.map((cat) => (
            <option key={cat} value={cat}>{cat}</option>
          ))}
        </select>
        <div className="sort-buttons">
          <span className="sort-label">Trier par :</span>
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
        <div className="sales-empty-state">
          <p>Aucune donnée de vente disponible.</p>
          <p>Les statistiques apparaîtront dès que des commandes seront complétées.</p>
        </div>
      ) : (
        <>
          {/* ===== Top-3 Podium ===== */}
          {top3.length >= 1 && (
            <div className="sales-podium-section">
              <h2 className="sales-section-title">🏆 Top 3</h2>
              <div className="sales-podium">
                {top3.map((item, i) => (
                  <div key={item.productId} className={`podium-card podium-card--${i + 1}`}>
                    <div className="podium-medal">{i === 0 ? '🥇' : i === 1 ? '🥈' : '🥉'}</div>
                    <p className="podium-name">{item.productName}</p>
                    {item.productCategory && (
                      <span className="badge badge-info podium-cat">{item.productCategory}</span>
                    )}
                    <div className="podium-stats">
                      <span className="podium-qty">{item.totalQuantitySold} ventes</span>
                      <span className="podium-rev">{Number(item.totalRevenue).toFixed(2)} €</span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* ===== Ranking table ===== */}
          <div className="admin-table-wrapper">
            <table className="admin-table">
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
                    <td>
                      <span className="sales-rank">
                        {index === 0 ? '🥇' : index === 1 ? '🥈' : index === 2 ? '🥉' : `#${index + 1}`}
                      </span>
                    </td>
                    <td><strong className="sales-product-name">{item.productName}</strong></td>
                    <td>
                      <span className="badge badge-info">{item.productCategory || '—'}</span>
                    </td>
                    <td>
                      <div className="sales-bar-row">
                        <div className="sales-bar-track">
                          <div
                            className="sales-bar-fill sales-bar-fill--blue"
                            style={{ width: `${Math.max(4, (item.totalQuantitySold / maxQty) * 100)}%` }}
                          />
                        </div>
                        <span className="sales-bar-label">{item.totalQuantitySold} u.</span>
                      </div>
                    </td>
                    <td>
                      <div className="sales-bar-row">
                        <div className="sales-bar-track">
                          <div
                            className="sales-bar-fill sales-bar-fill--green"
                            style={{ width: `${Math.max(4, (Number(item.totalRevenue) / maxRev) * 100)}%` }}
                          />
                        </div>
                        <span className="sales-bar-label sales-bar-label--green">{Number(item.totalRevenue).toFixed(2)} €</span>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
};

export default AdminSales;

