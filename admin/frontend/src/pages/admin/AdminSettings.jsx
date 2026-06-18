import { useState } from 'react';
import './AdminSettings.css';

const AdminSettings = () => {
  const [generalForm, setGeneralForm] = useState({
    cafeName: 'SmartCafé',
    currency: 'EUR',
    timezone: 'Europe/Paris',
  });

  const [orderSettings, setOrderSettings] = useState({
    onSite: true,
    clickAndCollect: true,
    maxItemsPerOrder: 20,
  });

  const [saved, setSaved] = useState(null);

  const saveSection = (section) => {
    setSaved(section);
    setTimeout(() => setSaved(null), 3000);
  };

  return (
    <div className="settings-page">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">Paramètres</h1>
          <p className="admin-page-subtitle">Configuration et préférences du système</p>
        </div>
      </div>

      <div className="settings-layout">

        {/* ===== Général ===== */}
        <section className="settings-card">
          <div className="settings-card-header">
            <div className="settings-card-icon settings-card-icon--blue">🏪</div>
            <div>
              <h2 className="settings-card-title">Général</h2>
              <p className="settings-card-desc">Informations de base de l'établissement</p>
            </div>
          </div>
          <div className="settings-fields">
            <div className="form-group">
              <label>Nom de l'établissement</label>
              <input
                type="text"
                value={generalForm.cafeName}
                onChange={(e) => setGeneralForm((p) => ({ ...p, cafeName: e.target.value }))}
              />
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Devise</label>
                <select
                  value={generalForm.currency}
                  onChange={(e) => setGeneralForm((p) => ({ ...p, currency: e.target.value }))}
                >
                  <option value="EUR">Euro (€)</option>
                  <option value="USD">Dollar ($)</option>
                  <option value="GBP">Livre sterling (£)</option>
                </select>
              </div>
              <div className="form-group">
                <label>Fuseau horaire</label>
                <select
                  value={generalForm.timezone}
                  onChange={(e) => setGeneralForm((p) => ({ ...p, timezone: e.target.value }))}
                >
                  <option value="Europe/Paris">Europe/Paris</option>
                  <option value="Europe/London">Europe/London</option>
                  <option value="America/New_York">America/New_York</option>
                </select>
              </div>
            </div>
          </div>
          <div className="settings-card-footer">
            {saved === 'general' && <span className="save-success">✓ Enregistré</span>}
            <button className="btn-primary" onClick={() => saveSection('general')}>
              Enregistrer
            </button>
          </div>
        </section>

        {/* ===== Commandes ===== */}
        <section className="settings-card">
          <div className="settings-card-header">
            <div className="settings-card-icon settings-card-icon--green">🛒</div>
            <div>
              <h2 className="settings-card-title">Commandes</h2>
              <p className="settings-card-desc">Modes de commande activés sur l'application</p>
            </div>
          </div>
          <div className="settings-fields">
            <div className="settings-toggle-row">
              <div className="settings-toggle-info">
                <p className="settings-toggle-title">Commande sur place</p>
                <p className="settings-toggle-sub">Via scan de QR code depuis une table</p>
              </div>
              <label className="toggle-switch">
                <input
                  type="checkbox"
                  checked={orderSettings.onSite}
                  onChange={(e) => setOrderSettings((p) => ({ ...p, onSite: e.target.checked }))}
                />
                <span className="toggle-thumb" />
              </label>
            </div>
            <div className="settings-toggle-row">
              <div className="settings-toggle-info">
                <p className="settings-toggle-title">Click &amp; Collect</p>
                <p className="settings-toggle-sub">Commande à l'avance, retrait au comptoir</p>
              </div>
              <label className="toggle-switch">
                <input
                  type="checkbox"
                  checked={orderSettings.clickAndCollect}
                  onChange={(e) => setOrderSettings((p) => ({ ...p, clickAndCollect: e.target.checked }))}
                />
                <span className="toggle-thumb" />
              </label>
            </div>
            <div className="form-group" style={{ marginTop: '16px' }}>
              <label>Nombre max d'articles par commande</label>
              <input
                type="number"
                min="1"
                max="100"
                value={orderSettings.maxItemsPerOrder}
                onChange={(e) =>
                  setOrderSettings((p) => ({ ...p, maxItemsPerOrder: parseInt(e.target.value) || 1 }))
                }
                style={{ maxWidth: '120px' }}
              />
            </div>
          </div>
          <div className="settings-card-footer">
            {saved === 'orders' && <span className="save-success">✓ Enregistré</span>}
            <button className="btn-primary" onClick={() => saveSection('orders')}>
              Enregistrer
            </button>
          </div>
        </section>

        {/* ===== Zone de danger ===== */}
        <section className="settings-card settings-card--danger">
          <div className="settings-card-header">
            <div className="settings-card-icon settings-card-icon--red">⚠️</div>
            <div>
              <h2 className="settings-card-title settings-card-title--danger">Zone de danger</h2>
              <p className="settings-card-desc">Actions irréversibles sur les données</p>
            </div>
          </div>
          <div className="settings-fields">
            <div className="danger-action">
              <div>
                <p className="danger-action-title">Purger les anciennes commandes</p>
                <p className="danger-action-desc">
                  Supprime définitivement toutes les commandes complétées de plus de 90 jours.
                </p>
              </div>
              <button className="btn-danger-outline" disabled title="Disponible prochainement">
                Purger
              </button>
            </div>
          </div>
        </section>

      </div>
    </div>
  );
};

export default AdminSettings;
