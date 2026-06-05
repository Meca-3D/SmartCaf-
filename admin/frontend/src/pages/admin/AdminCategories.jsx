import { useEffect, useState } from 'react';
import {
  getAdminCategories,
  createAdminCategory,
  updateAdminCategory,
  deleteAdminCategory,
} from '../../services/api';
import './AdminCategories.css';

const EMPTY_FORM = { name: '', description: '' };

const AdminCategories = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Add/Edit modal
  const [showModal, setShowModal] = useState(false);
  const [editCat, setEditCat] = useState(null);
  const [form, setForm] = useState(EMPTY_FORM);
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState('');

  // Delete modal
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [deleteError, setDeleteError] = useState('');
  const [deleting, setDeleting] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const data = await getAdminCategories();
      setCategories(data);
    } catch {
      setError('Impossible de charger les catégories.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const openAdd = () => {
    setEditCat(null);
    setForm(EMPTY_FORM);
    setFormError('');
    setShowModal(true);
  };

  const openEdit = (cat) => {
    setEditCat(cat);
    setForm({ name: cat.name || '', description: cat.description || '' });
    setFormError('');
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setEditCat(null);
    setForm(EMPTY_FORM);
    setFormError('');
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.name.trim()) { setFormError('Le nom est obligatoire.'); return; }
    setSaving(true);
    setFormError('');
    try {
      if (editCat) {
        await updateAdminCategory(editCat.id, form);
      } else {
        await createAdminCategory(form);
      }
      closeModal();
      load();
    } catch (err) {
      setFormError(err?.response?.data?.message || 'Erreur lors de la sauvegarde.');
    } finally {
      setSaving(false);
    }
  };

  const askDelete = (cat) => {
    setDeleteTarget(cat);
    setDeleteError('');
  };

  const closeDelete = () => {
    setDeleteTarget(null);
    setDeleteError('');
  };

  const confirmDelete = async () => {
    setDeleting(true);
    setDeleteError('');
    try {
      await deleteAdminCategory(deleteTarget.id);
      closeDelete();
      load();
    } catch (err) {
      setDeleteError(err?.response?.data?.message || 'Erreur lors de la suppression.');
    } finally {
      setDeleting(false);
    }
  };

  if (loading) return <div className="admin-loading">Chargement des catégories...</div>;

  return (
    <div className="admin-categories">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">Catégories</h1>
          <p className="admin-page-subtitle">{categories.length} catégorie{categories.length > 1 ? 's' : ''} au total</p>
        </div>
        <button className="btn-primary" onClick={openAdd}>
          + Nouvelle catégorie
        </button>
      </div>

      {error && <div className="admin-error">{error}</div>}

      {categories.length === 0 ? (
        <div className="empty-state">
          <p className="empty-state-icon">🏷️</p>
          <p className="empty-state-text">Aucune catégorie pour l'instant.</p>
          <button className="btn-primary" onClick={openAdd}>Créer la première catégorie</button>
        </div>
      ) : (
        <div className="categories-grid">
          {categories.map((cat) => (
            <div key={cat.id} className="category-card">
              <div className="category-card-icon">🏷️</div>
              <div className="category-card-body">
                <h3 className="category-card-name">{cat.name}</h3>
                {cat.description && (
                  <p className="category-card-desc">{cat.description}</p>
                )}
              </div>
              <div className="category-card-actions">
                <button className="btn-edit btn-sm" onClick={() => openEdit(cat)}>Modifier</button>
                <button className="btn-danger btn-sm" onClick={() => askDelete(cat)}>Supprimer</button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Add/Edit Modal */}
      {showModal && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal modal--small" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">{editCat ? 'Modifier la catégorie' : 'Nouvelle catégorie'}</h2>
              <button className="modal-close" onClick={closeModal}>✕</button>
            </div>
            <form className="admin-form" onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Nom *</label>
                <input
                  name="name"
                  value={form.name}
                  onChange={handleChange}
                  placeholder="Ex: Boissons chaudes"
                  autoFocus
                  required
                />
              </div>
              <div className="form-group">
                <label>Description</label>
                <textarea
                  name="description"
                  value={form.description}
                  onChange={handleChange}
                  placeholder="Description optionnelle..."
                  rows={3}
                />
              </div>
              {formError && <p className="form-error">{formError}</p>}
              <div className="form-actions">
                <button type="button" className="btn-secondary" onClick={closeModal}>Annuler</button>
                <button type="submit" className="btn-primary" disabled={saving}>
                  {saving ? 'Enregistrement...' : editCat ? 'Mettre à jour' : 'Créer'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Delete Modal */}
      {deleteTarget && (
        <div className="modal-overlay" onClick={closeDelete}>
          <div className="modal modal--small" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">⚠️ Supprimer la catégorie</h2>
              <button className="modal-close" onClick={closeDelete}>✕</button>
            </div>
            <p className="delete-warning">
              Voulez-vous supprimer la catégorie <strong>« {deleteTarget.name} »</strong> ?<br />
              Cette action est irréversible. Si des produits utilisent cette catégorie, la suppression sera bloquée.
            </p>
            {deleteError && <p className="delete-error-msg">{deleteError}</p>}
            <div className="form-actions">
              <button className="btn-secondary" onClick={closeDelete}>Annuler</button>
              <button className="btn-danger" onClick={confirmDelete} disabled={deleting}>
                {deleting ? 'Suppression...' : 'Supprimer'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminCategories;
