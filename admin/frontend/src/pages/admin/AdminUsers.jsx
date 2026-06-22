import { useEffect, useState } from 'react';
import { getAdminUsers, updateUserRole, deleteAdminUser } from '../../services/api';
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
  const [roleTarget, setRoleTarget] = useState(null); // { id, currentRole }
  const [newRole, setNewRole] = useState('');
  const [roleError, setRoleError] = useState('');

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
                <td className="user-email">{user.email}</td>
                <td>
                  <span className={`badge ${user.role === 'ADMIN' ? 'badge-admin' : user.role === 'EMPLOYER' ? 'badge-info' : 'badge-client'}`}>
                    {user.role === 'ADMIN' ? '🔑 Admin' : user.role === 'EMPLOYER' ? '🧑‍💼 Employé' : '👤 Client'}
                  </span>
                </td>
                <td>{formatDate(user.createdAt)}</td>
                <td>
                  <div className="table-actions">
                    <button className="btn-edit" onClick={() => openRoleModal(user)}>
                      Changer le rôle
                    </button>
                    {user.role !== 'ADMIN' && (
                      <button className="btn-danger" onClick={() => askDelete(user)}>
                        Supprimer
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
    </div>
  );
};

export default AdminUsers;
