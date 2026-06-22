import { useEffect, useState } from 'react';
import { getAdminUsers, updateUserRole, deleteAdminUser, banAdminUser, getAdminUserOrders } from '../../services/api';
import './AdminUsers.css';

const CONFIRM_WORD = 'SUPPRIMER';

const AdminUsers = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [filterRole, setFilterRole] = useState('');

  // Delete confirmation
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [confirmCode, setConfirmCode] = useState('');
  const [deleteError, setDeleteError] = useState('');

  // Role change
  const [roleTarget, setRoleTarget] = useState(null);
  const [newRole, setNewRole] = useState('');
  const [roleError, setRoleError] = useState('');

  // Ban
  const [banTarget, setBanTarget] = useState(null);
  const [banError, setBanError] = useState('');

  // Profile
  const [profileTarget, setProfileTarget] = useState(null);
  const [profileOrders, setProfileOrders] = useState([]);
  const [profileLoading, setProfileLoading] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const data = await getAdminUsers();
      setUsers(data);
    } catch {
      setError('Impossible de charger la liste des clients.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const filtered = users.filter((u) => {
    const name = `${u.firstName} ${u.lastName}`.toLowerCase();
    const matchSearch = name.includes(searchQuery.toLowerCase()) ||
      u.email?.toLowerCase().includes(searchQuery.toLowerCase());
    const matchRole = filterRole ? u.role === filterRole : true;
    return matchSearch && matchRole;
  });

  const formatDate = (dateStr) => {
    if (!dateStr) return '—';
    return new Date(dateStr).toLocaleDateString('fr-FR', {
      day: '2-digit', month: '2-digit', year: 'numeric',
    });
  };

  // ===== Role change =====
  const openRoleModal = (user) => {
    setRoleTarget(user);
    setNewRole(user.role);
    setRoleError('');
  };

  const closeRoleModal = () => {
    setRoleTarget(null);
    setNewRole('');
    setRoleError('');
  };

  const confirmRoleChange = async () => {
    if (newRole === roleTarget.role) { closeRoleModal(); return; }
    try {
      await updateUserRole(roleTarget.id, newRole);
      closeRoleModal();
      load();
    } catch (e) {
      setRoleError(e?.response?.data?.message || 'Erreur lors du changement de rôle.');
    }
  };

  // ===== Ban =====
  const openBanModal = (user) => {
    setBanTarget(user);
    setBanError('');
  };

  const closeBanModal = () => {
    setBanTarget(null);
    setBanError('');
  };

  const confirmBan = async () => {
    try {
      await banAdminUser(banTarget.id);
      closeBanModal();
      load();
    } catch (e) {
      setBanError(e?.response?.data?.message || 'Erreur lors de l\'opération.');
    }
  };

  // ===== Profile =====
  const openProfile = async (user) => {
    setProfileTarget(user);
    setProfileOrders([]);
    setProfileLoading(true);
    try {
      const orders = await getAdminUserOrders(user.id);
      setProfileOrders(orders);
    } catch {
      setProfileOrders([]);
    } finally {
      setProfileLoading(false);
    }
  };

  const closeProfile = () => {
    setProfileTarget(null);
    setProfileOrders([]);
  };

  const statusLabel = (s) => {
    switch (s) {
      case 'PENDING': return { text: 'En attente', cls: 'status-pending' };
      case 'IN_PROGRESS': return { text: 'En cours', cls: 'status-in-progress' };
      case 'COMPLETED': return { text: 'Terminée', cls: 'status-completed' };
      case 'CANCELLED': return { text: 'Annulée', cls: 'status-cancelled' };
      default: return { text: s, cls: '' };
    }
  };

  // ===== Delete =====
  const askDelete = (user) => {
    setDeleteTarget(user);
    setConfirmCode('');
    setDeleteError('');
  };

  const closeDeleteModal = () => {
    setDeleteTarget(null);
    setConfirmCode('');
    setDeleteError('');
  };

  const confirmDelete = async () => {
    if (confirmCode !== CONFIRM_WORD) {
      setDeleteError(`Veuillez saisir exactement "${CONFIRM_WORD}" pour confirmer.`);
      return;
    }
    try {
      await deleteAdminUser(deleteTarget.id);
      closeDeleteModal();
      load();
    } catch (e) {
      setDeleteError(e?.response?.data?.message || 'Erreur lors de la suppression.');
    }
  };

  if (loading) return <div className="admin-loading">Chargement des clients...</div>;

  return (
    <div className="admin-users">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">Clients</h1>
          <p className="admin-page-subtitle">{users.length} compte{users.length > 1 ? 's' : ''} au total</p>
        </div>
      </div>

      {error && <div className="admin-error">{error}</div>}

      {/* Filters */}
      <div className="users-filters">
        <input
          type="text"
          placeholder="Rechercher par nom ou email..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="filter-input"
        />
        <select
          value={filterRole}
          onChange={(e) => setFilterRole(e.target.value)}
          className="filter-select"
        >
          <option value="">Tous les rôles</option>
          <option value="CLIENT">Client</option>
          <option value="EMPLOYER">Employé</option>
          <option value="ADMIN">Admin</option>
        </select>
      </div>

      {/* Table */}
      <div className="admin-table-wrapper">
        <table className="admin-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Nom</th>
              <th>Email</th>
              <th>Rôle</th>
              <th>Membre depuis</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.length === 0 ? (
              <tr>
                <td colSpan={6} className="empty-row">Aucun client trouvé</td>
              </tr>
            ) : filtered.map((user) => (
              <tr key={user.id}>
                <td className="user-id">{user.id}</td>
                <td>
                  <div className="user-name">{user.firstName} {user.lastName}</div>
                </td>
                <td className="user-email">
                  {user.email}
                  {user.banned && <span className="badge badge-banned" style={{ marginLeft: 6 }}>🚫 Banni</span>}
                </td>
                <td>
                  <span className={`badge ${user.role === 'ADMIN' ? 'badge-admin' : user.role === 'EMPLOYER' ? 'badge-info' : 'badge-client'}`}>
                    {user.role === 'ADMIN' ? '🔑 Admin' : user.role === 'EMPLOYER' ? '🧑‍💼 Employé' : '👤 Client'}
                  </span>
                </td>
                <td>{formatDate(user.createdAt)}</td>
                <td>
                  <div className="table-actions">
                    <button className="btn-secondary btn-sm" onClick={() => openProfile(user)}>
                      Voir profil
                    </button>
                    <button className="btn-edit" onClick={() => openRoleModal(user)}>
                      Rôle
                    </button>
                    {user.role === 'CLIENT' && (
                      <button
                        className={user.banned ? 'btn-unban' : 'btn-ban'}
                        onClick={() => openBanModal(user)}
                      >
                        {user.banned ? '✅ Débannir' : '🚫 Bannir'}
                      </button>
                    )}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Role change modal */}
      {roleTarget && (
        <div className="modal-overlay" onClick={closeRoleModal}>
          <div className="modal modal--small" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">Changer le rôle</h2>
              <button className="modal-close" onClick={closeRoleModal}>✕</button>
            </div>
            <p className="modal-subtitle">
              Utilisateur : <strong>{roleTarget.firstName} {roleTarget.lastName}</strong>
            </p>
            <div className="form-group">
              <label>Nouveau rôle</label>
              <select value={newRole} onChange={(e) => setNewRole(e.target.value)} className="filter-select" style={{ width: '100%' }}>
                <option value="CLIENT">Client</option>
                <option value="EMPLOYER">Employé</option>
                <option value="ADMIN">Admin</option>
              </select>
            </div>
            {roleError && <p className="delete-error">{roleError}</p>}
            <div className="form-actions">
              <button className="btn-secondary" onClick={closeRoleModal}>Annuler</button>
              <button className="btn-primary" onClick={confirmRoleChange}>Confirmer</button>
            </div>
          </div>
        </div>
      )}

      {/* Delete confirmation modal */}
      {deleteTarget && (
        <div className="modal-overlay" onClick={closeDeleteModal}>
          <div className="modal modal--small" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">⚠️ Supprimer le compte</h2>
              <button className="modal-close" onClick={closeDeleteModal}>✕</button>
            </div>
            <p className="delete-warning">
              Le compte de <strong>{deleteTarget.firstName} {deleteTarget.lastName}</strong> ({deleteTarget.email}) sera définitivement supprimé.
            </p>
            <div className="form-group">
              <label className="confirm-label">
                Tapez <strong>{CONFIRM_WORD}</strong> pour confirmer :
              </label>
              <input
                type="text"
                value={confirmCode}
                onChange={(e) => setConfirmCode(e.target.value.toUpperCase())}
                placeholder={CONFIRM_WORD}
                className="confirm-input"
                autoFocus
              />
            </div>
            {deleteError && <p className="delete-error">{deleteError}</p>}
            <div className="form-actions">
              <button className="btn-secondary" onClick={closeDeleteModal}>Annuler</button>
              <button
                className="btn-danger"
                onClick={confirmDelete}
                disabled={confirmCode !== CONFIRM_WORD}
              >
                Supprimer le compte
              </button>
            </div>
          </div>
        </div>
      )}
      {/* Ban confirm modal */}
      {banTarget && (
        <div className="modal-overlay" onClick={closeBanModal}>
          <div className="modal modal--small" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">{banTarget.banned ? '✅ Débannir le compte' : '🚫 Bannir le compte'}</h2>
              <button className="modal-close" onClick={closeBanModal}>✕</button>
            </div>
            <p className="delete-warning">
              {banTarget.banned
                ? <>Le compte de <strong>{banTarget.firstName} {banTarget.lastName}</strong> sera débanni. Il pourra se reconnecter.</>
                : <>Le compte de <strong>{banTarget.firstName} {banTarget.lastName}</strong> ({banTarget.email}) sera banni. Il ne pourra plus se connecter.</>
              }
            </p>
            {banError && <p className="delete-error">{banError}</p>}
            <div className="form-actions">
              <button className="btn-secondary" onClick={closeBanModal}>Annuler</button>
              <button className={banTarget.banned ? 'btn-primary' : 'btn-ban'} onClick={confirmBan}>
                {banTarget.banned ? 'Débannir' : 'Bannir'}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Profile modal */}
      {profileTarget && (
        <div className="modal-overlay" onClick={closeProfile}>
          <div className="modal modal--profile" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">👤 Profil — {profileTarget.firstName} {profileTarget.lastName}</h2>
              <button className="modal-close" onClick={closeProfile}>✕</button>
            </div>

            <div className="profile-info-grid">
              <div className="profile-info-item">
                <span className="profile-info-label">Email</span>
                <span className="profile-info-value">{profileTarget.email}</span>
              </div>
              <div className="profile-info-item">
                <span className="profile-info-label">Rôle</span>
                <span className={`badge ${profileTarget.role === 'ADMIN' ? 'badge-admin' : profileTarget.role === 'EMPLOYER' ? 'badge-info' : 'badge-client'}`}>
                  {profileTarget.role === 'ADMIN' ? '🔑 Admin' : profileTarget.role === 'EMPLOYER' ? '🧑‍💼 Employé' : '👤 Client'}
                </span>
              </div>
              <div className="profile-info-item">
                <span className="profile-info-label">Membre depuis</span>
                <span className="profile-info-value">{formatDate(profileTarget.createdAt)}</span>
              </div>
              <div className="profile-info-item">
                <span className="profile-info-label">Statut</span>
                {profileTarget.banned
                  ? <span className="badge badge-banned">🚫 Banni</span>
                  : <span className="badge badge-client">✅ Actif</span>}
              </div>
            </div>

            <h3 className="profile-orders-title">Historique des commandes</h3>
            {profileLoading ? (
              <p className="profile-orders-empty">Chargement...</p>
            ) : profileOrders.length === 0 ? (
              <p className="profile-orders-empty">Aucune commande pour ce client.</p>
            ) : (
              <div className="profile-orders-list">
                {profileOrders.map((order) => {
                  const s = statusLabel(order.status);
                  return (
                    <div key={order.id} className="profile-order-row">
                      <span className="profile-order-id">#{order.id}</span>
                      <span className="profile-order-date">{formatDate(order.createdAt)}</span>
                      <span className={`badge order-status-badge ${s.cls}`}>{s.text}</span>
                      <span className="profile-order-total">{Number(order.totalPrice ?? 0).toFixed(2)} €</span>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminUsers;
