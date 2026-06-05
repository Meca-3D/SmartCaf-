import { useEffect, useState } from 'react';
import {
  getAdminProducts,
  createProduct,
  updateProduct,
  deleteProduct,
  deleteProductsBatch,
  toggleProductStatus,
  getAdminCategories,
} from '../../services/api';
import './AdminProducts.css';

const EMPTY_FORM = {
  name: '',
  description: '',
  price: '',
  stock: '',
  category: '',
  imageUrl: '',
  isActive: true,
};

const CONFIRM_WORD = 'SUPPRIMER';

const AdminProducts = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editProduct, setEditProduct] = useState(null);
  const [form, setForm] = useState(EMPTY_FORM);
  const [saving, setSaving] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [filterCategory, setFilterCategory] = useState('');

  // Selection
  const [selected, setSelected] = useState(new Set());

  // Delete confirmation modal (single or batch)
  const [deleteTarget, setDeleteTarget] = useState(null); // { type: 'single'|'batch', id?: number }
  const [confirmCode, setConfirmCode] = useState('');
  const [deleteError, setDeleteError] = useState('');

  const load = async () => {
    setLoading(true);
    try {
      const [prods, cats] = await Promise.all([getAdminProducts(), getAdminCategories()]);
      setProducts(prods);
      setCategories(cats);
    } catch {
      setError('Impossible de charger les données.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const openAddModal = () => {
    setEditProduct(null);
    setForm(EMPTY_FORM);
    setShowModal(true);
  };

  const openEditModal = (product) => {
    setEditProduct(product);
    setForm({
      name: product.name || '',
      description: product.description || '',
      price: product.price || '',
      stock: product.stock || '',
      category: product.category || '',
      imageUrl: product.imageUrl || '',
      isActive: product.isActive ?? true,
    });
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setEditProduct(null);
    setForm(EMPTY_FORM);
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const payload = {
        ...form,
        price: parseFloat(form.price),
        stock: parseInt(form.stock, 10),
      };
      if (editProduct) {
        await updateProduct(editProduct.id, payload);
      } else {
        await createProduct(payload);
      }
      closeModal();
      load();
    } catch {
      setError('Erreur lors de la sauvegarde du produit.');
    } finally {
      setSaving(false);
    }
  };

  const handleToggle = async (id) => {
    try {
      await toggleProductStatus(id);
      load();
    } catch {
      setError('Erreur lors du changement de statut.');
    }
  };

  // Open confirmation modal
  const askDeleteSingle = (id) => {
    setDeleteTarget({ type: 'single', id });
    setConfirmCode('');
    setDeleteError('');
  };

  const askDeleteBatch = () => {
    if (selected.size === 0) return;
    setDeleteTarget({ type: 'batch' });
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
      if (deleteTarget.type === 'single') {
        await deleteProduct(deleteTarget.id);
        setSelected((prev) => { const s = new Set(prev); s.delete(deleteTarget.id); return s; });
      } else {
        await deleteProductsBatch([...selected]);
        setSelected(new Set());
      }
      closeDeleteModal();
      load();
    } catch (err) {
      setDeleteError(err?.response?.data?.message || 'Erreur lors de la suppression.');
    }
  };

  // Select logic
  const toggleSelect = (id) => {
    setSelected((prev) => {
      const s = new Set(prev);
      s.has(id) ? s.delete(id) : s.add(id);
      return s;
    });
  };

  const toggleSelectAll = () => {
    if (selected.size === filtered.length) {
      setSelected(new Set());
    } else {
      setSelected(new Set(filtered.map((p) => p.id)));
    }
  };

  const filterCategoryList = [...new Set(products.map((p) => p.category).filter(Boolean))];

  const filtered = products.filter((p) => {
    const matchSearch = p.name?.toLowerCase().includes(searchQuery.toLowerCase());
    const matchCat = filterCategory ? p.category === filterCategory : true;
    return matchSearch && matchCat;
  });

  const getStatusBadge = (isActive) =>
    isActive
      ? <span className="badge badge-success">Actif</span>
      : <span className="badge badge-gray">Inactif</span>;

  if (loading) return <div className="admin-loading">Chargement des produits...</div>;

  return (
    <div className="admin-products">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">Produits</h1>
          <p className="admin-page-subtitle">{products.length} produit{products.length > 1 ? 's' : ''} au total</p>
        </div>
        <div className="header-actions">
          {selected.size > 0 && (
            <button className="btn-danger" onClick={askDeleteBatch}>
              🗑 Supprimer la sélection ({selected.size})
            </button>
          )}
          <button className="btn-primary" onClick={openAddModal}>
            + Nouveau produit
          </button>
        </div>
      </div>

      {error && <div className="admin-error">{error}</div>}

      {/* Filters */}
      <div className="products-filters">
        <input
          type="text"
          placeholder="Rechercher un produit..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="filter-input"
        />
        <select
          value={filterCategory}
          onChange={(e) => setFilterCategory(e.target.value)}
          className="filter-select"
        >
          <option value="">Toutes les catégories</option>
          {filterCategoryList.map((cat) => (
            <option key={cat} value={cat}>{cat}</option>
          ))}
        </select>
      </div>

      {/* Table */}
      <div className="admin-table-wrapper">
        <table className="admin-table">
          <thead>
            <tr>
              <th>
                <input
                  type="checkbox"
                  checked={filtered.length > 0 && selected.size === filtered.length}
                  onChange={toggleSelectAll}
                  title="Tout sélectionner"
                />
              </th>
              <th>Image</th>
              <th>Nom</th>
              <th>Catégorie</th>
              <th>Prix</th>
              <th>Stock</th>
              <th>Statut</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.length === 0 ? (
              <tr>
                <td colSpan={8} className="empty-row">Aucun produit trouvé</td>
              </tr>
            ) : filtered.map((product) => (
              <tr key={product.id} className={selected.has(product.id) ? 'row-selected' : ''}>
                <td>
                  <input
                    type="checkbox"
                    checked={selected.has(product.id)}
                    onChange={() => toggleSelect(product.id)}
                  />
                </td>
                <td>
                  {product.imageUrl
                    ? <img src={product.imageUrl} alt={product.name} className="product-thumb" />
                    : <div className="product-thumb-placeholder">📦</div>
                  }
                </td>
                <td>
                  <div className="product-name">{product.name}</div>
                  {product.description && (
                    <div className="product-desc">{product.description.slice(0, 50)}{product.description.length > 50 ? '…' : ''}</div>
                  )}
                </td>
                <td><span className="badge badge-info">{product.category}</span></td>
                <td><strong>{Number(product.price).toFixed(2)} €</strong></td>
                <td>
                  <span className={`stock-badge ${product.stock <= 5 ? 'stock-low' : ''}`}>
                    {product.stock}
                  </span>
                </td>
                <td>
                  <button
                    className="status-toggle"
                    onClick={() => handleToggle(product.id)}
                    title="Cliquer pour changer le statut"
                  >
                    {getStatusBadge(product.isActive)}
                  </button>
                </td>
                <td>
                  <div className="table-actions">
                    <button className="btn-edit" onClick={() => openEditModal(product)}>Modifier</button>
                    <button className="btn-danger" onClick={() => askDeleteSingle(product.id)}>Supprimer</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Add/Edit Modal */}
      {showModal && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">{editProduct ? 'Modifier le produit' : 'Nouveau produit'}</h2>
              <button className="modal-close" onClick={closeModal}>✕</button>
            </div>
            <form className="admin-form" onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>Nom *</label>
                  <input name="name" value={form.name} onChange={handleChange} required placeholder="Ex: Café Latte" />
                </div>
                <div className="form-group">
                  <label>Catégorie *</label>
                  <select name="category" value={form.category} onChange={handleChange} required>
                    <option value="">— Choisir une catégorie —</option>
                    {categories.map((cat) => (
                      <option key={cat.id} value={cat.name}>{cat.name}</option>
                    ))}
                  </select>
                </div>
              </div>
              <div className="form-group">
                <label>Description</label>
                <textarea name="description" value={form.description} onChange={handleChange} placeholder="Description du produit..." />
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Prix (€) *</label>
                  <input name="price" type="number" step="0.01" min="0" value={form.price} onChange={handleChange} required placeholder="0.00" />
                </div>
                <div className="form-group">
                  <label>Stock *</label>
                  <input name="stock" type="number" min="0" value={form.stock} onChange={handleChange} required placeholder="0" />
                </div>
              </div>
              <div className="form-group">
                <label>URL de l'image</label>
                <input name="imageUrl" value={form.imageUrl} onChange={handleChange} placeholder="https://..." />
              </div>
              <div className="form-group form-group--checkbox">
                <label className="checkbox-label">
                  <input name="isActive" type="checkbox" checked={form.isActive} onChange={handleChange} />
                  Produit actif (visible sur la boutique)
                </label>
              </div>
              <div className="form-actions">
                <button type="button" className="btn-secondary" onClick={closeModal}>Annuler</button>
                <button type="submit" className="btn-primary" disabled={saving}>
                  {saving ? 'Enregistrement...' : editProduct ? 'Mettre à jour' : 'Créer le produit'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Delete Confirmation Modal */}
      {deleteTarget && (
        <div className="modal-overlay" onClick={closeDeleteModal}>
          <div className="modal modal--small" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">⚠️ Confirmer la suppression</h2>
              <button className="modal-close" onClick={closeDeleteModal}>✕</button>
            </div>
            <p className="delete-warning">
              {deleteTarget.type === 'batch'
                ? `Vous allez supprimer ${selected.size} produit(s). Cette action est irréversible.`
                : 'Ce produit sera définitivement supprimé. Cette action est irréversible.'
              }
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
                Confirmer la suppression
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminProducts;
