import { useEffect, useState } from 'react';
import { getDashboardStats } from '../../services/api';
import './AdminDashboard.css';

const StatCard = ({ icon, label, value, color, subtitle }) => (
  <div className={`stat-card stat-card--${color}`}>
    <div className="stat-card-icon">{icon}</div>
    <div className="stat-card-body">
      <p className="stat-card-label">{label}</p>
      <p className="stat-card-value">{value}</p>
      {subtitle && <p className="stat-card-subtitle">{subtitle}</p>}
    </div>
  </div>
);

const AdminDashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const data = await getDashboardStats();
        setStats(data);
      } catch {
        setError('Impossible de charger les statistiques. Vérifiez la connexion à la base de données.');
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  if (loading) return <div className="admin-loading">Chargement des statistiques...</div>;

  return (
    <div className="admin-dashboard">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">Dashboard</h1>
          <p className="admin-page-subtitle">Vue d'ensemble de votre café</p>
        </div>
      </div>

      {error && <div className="admin-error">{error}</div>}

      {stats && (
        <>
          {/* KPI Cards */}
          <div className="stats-grid">
            <StatCard
              icon="📦"
              label="Produits totaux"
              value={stats.totalProducts}
              color="blue"
              subtitle={`${stats.activeProducts} actifs`}
            />
            <StatCard
              icon="🛒"
              label="Commandes totales"
              value={stats.totalOrders}
              color="purple"
              subtitle={`${stats.pendingOrders} en attente`}
            />
            <StatCard
              icon="✅"
              label="Commandes complétées"
              value={stats.completedOrders}
              color="green"
            />
            <StatCard
              icon="💰"
              label="Revenu total"
              value={`${Number(stats.totalRevenue).toFixed(2)} €`}
              color="orange"
              subtitle="Commandes complétées"
            />
          </div>

          {/* Top Products */}
          {stats.topProducts && stats.topProducts.length > 0 ? (
            <div className="dashboard-section">
              <h2 className="dashboard-section-title">Top produits vendus</h2>
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
                    {stats.topProducts.map((p, index) => (
                      <tr key={p.productId}>
                        <td>
                          <span className={`rank-badge rank-${index + 1}`}>
                            {index === 0 ? '🥇' : index === 1 ? '🥈' : index === 2 ? '🥉' : `#${index + 1}`}
                          </span>
                        </td>
                        <td><strong>{p.productName}</strong></td>
                        <td>
                          <span className="badge badge-info">{p.productCategory}</span>
                        </td>
                        <td>
                          <div className="sales-bar-wrapper">
                            <div
                              className="sales-bar"
                              style={{
                                width: `${Math.min(100, (p.totalQuantitySold / stats.topProducts[0].totalQuantitySold) * 100)}%`
                              }}
                            />
                            <span className="sales-bar-value">{p.totalQuantitySold}</span>
                          </div>
                        </td>
                        <td className="revenue-cell">{Number(p.totalRevenue).toFixed(2)} €</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          ) : (
            <div className="dashboard-section">
              <h2 className="dashboard-section-title">Top produits vendus</h2>
              <div className="empty-state">
                <p>Aucune donnée de vente disponible.</p>
                <p>Les statistiques apparaîtront dès que des commandes seront passées.</p>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default AdminDashboard;
