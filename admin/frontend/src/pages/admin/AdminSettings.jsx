import { useState } from 'react';
import { useUserStore } from '../../store/userStore';
import { changeAdminPassword, createEmployerAccount, purgeOldOrders } from '../../services/api';
import './AdminSettings.css';

const LS_KEY = 'smartcafe_settings';

const defaultSettings = {
  cafeName: 'SmartCafé',
  cafeAddress: '',
  cafePhone: '',
  currency: 'EUR',
  timezone: 'Europe/Paris',
  openingFrom: '07:00',
  openingTo: '20:00',
  openToday: true,
  stockAlertThreshold: 5,
  onSite: true,
  clickAndCollect: true,
};

const loadSettings = () => {
  try {
    const stored = localStorage.getItem(LS_KEY);
    return stored ? { ...defaultSettings, ...JSON.parse(stored) } : defaultSettings;
  } catch { return defaultSettings; }
};

const AdminSettings = () => {
  const user = useUserStore((state) => state.user);
  const [settings, setSettings] = useState(loadSettings);
  const [saved, setSaved] = useState(null);

  // Password change
  const [pwForm, setPwForm] = useState({ currentPassword: '', newPassword: '', confirmPassword: '' });
  const [pwStatus, setPwStatus] = useState(null);
  const [pwLoading, setPwLoading] = useState(false);

  // Create employer
  const [empForm, setEmpForm] = useState({ firstName: '', lastName: '', email: '', password: '' });
  const [empStatus, setEmpStatus] = useState(null);
  const [empLoading, setEmpLoading] = useState(false);

  // Purge
  const [purgeDays, setPurgeDays] = useState(90);
  const [purgeStatus, setPurgeStatus] = useState(null);
  const [purgeLoading, setPurgeLoading] = useState(false);
  const [showPurgeConfirm, setShowPurgeConfirm] = useState(false);

  const set = (key, value) => setSettings((prev) => ({ ...prev, [key]: value }));

  const saveSection = (section) => {
    localStorage.setItem(LS_KEY, JSON.stringify(settings));
    setSaved(section);
    setTimeout(() => setSaved(null), 3000);
  };

  const handlePasswordChange = async (e) => {
    e.preventDefault();
    if (pwForm.newPassword !== pwForm.confirmPassword) {
      setPwStatus({ type: 'error', msg: 'Les mots de passe ne correspondent pas.' });
      return;
    }
    if (pwForm.newPassword.length < 6) {
      setPwStatus({ type: 'error', msg: 'Le mot de passe doit faire au moins 6 caractères.' });
      return;
    }
    setPwLoading(true);
    setPwStatus(null);
    try {
      await changeAdminPassword(user.email, pwForm.currentPassword, pwForm.newPassword);
      setPwStatus({ type: 'success', msg: 'Mot de passe modifié avec succès.' });
      setPwForm({ currentPassword: '', newPassword: '', confirmPassword: '' });
    } catch (err) {
      setPwStatus({ type: 'error', msg: err?.response?.data?.message || 'Erreur lors du changement de mot de passe.' });
    } finally {
      setPwLoading(false);
    }
  };

  const handleCreateEmployer = async (e) => {
    e.preventDefault();
    setEmpLoading(true);
    setEmpStatus(null);
    try {
      await createEmployerAccount(empForm);
      setEmpStatus({ type: 'success', msg: `Compte employé créé pour ${empForm.email}.` });
      setEmpForm({ firstName: '', lastName: '', email: '', password: '' });
    } catch (err) {
      setEmpStatus({ type: 'error', msg: err?.response?.data?.message || 'Erreur lors de la création du compte.' });
    } finally {
      setEmpLoading(false);
      setTimeout(() => setEmpStatus(null), 5000);
    }
  };

  const handlePurge = async () => {
    setPurgeLoading(true);
    setShowPurgeConfirm(false);
    try {
      const res = await purgeOldOrders(purgeDays);
      setPurgeStatus({ type: 'success', msg: `${res.count ?? 0} commande(s) purgée(s) avec succès.` });
    } catch {
      setPurgeStatus({ type: 'error', msg: 'Erreur lors de la purge.' });
    } finally {
      setPurgeLoading(false);
      setTimeout(() => setPurgeStatus(null), 5000);
    }
  };

  const statusMsg = (s) => s && (
    <p style={{ fontSize: '0.85rem', color: s.type === 'success' ? '#059669' : '#dc2626', fontWeight: 600, margin: '10px 0 0' }}>
      {s.type === 'success' ? '✓ ' : '✗ '}{s.msg}
    </p>
  );

  return (
    <div className="settings-page">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">Paramètres</h1>
          <p className="admin-page-subtitle">Configuration et préférences du système</p>
        </div>
      </div>

      <div className="settings-layout">

        {/* ===== Informations café ===== */}
        <section className="settings-card">
          <div className="settings-card-header">
            <div className="settings-card-icon settings-card-icon--blue">🏪</div>
            <div>
              <h2 className="settings-card-title">Informations du café</h2>
              <p className="settings-card-desc">Nom, coordonnées et préférences régionales</p>
            </div>
          </div>
          <div className="settings-fields">
            <div className="form-row">
              <div className="form-group">
                <label>Nom de l'établissement</label>
                <input type="text" value={settings.cafeName} onChange={(e) => set('cafeName', e.target.value)} placeholder="SmartCafé" />
              </div>
              <div className="form-group">
                <label>Téléphone</label>
                <input type="text" value={settings.cafePhone} onChange={(e) => set('cafePhone', e.target.value)} placeholder="+33 1 23 45 67 89" />
              </div>
            </div>
            <div className="form-group">
              <label>Adresse</label>
              <input type="text" value={settings.cafeAddress} onChange={(e) => set('cafeAddress', e.target.value)} placeholder="123 rue de la Paix, 75001 Paris" />
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Devise</label>
                <select value={settings.currency} onChange={(e) => set('currency', e.target.value)}>
                  <option value="EUR">Euro (€)</option>
                  <option value="USD">Dollar ($)</option>
                  <option value="GBP">Livre sterling (£)</option>
                </select>
              </div>
              <div className="form-group">
                <label>Fuseau horaire</label>
                <select value={settings.timezone} onChange={(e) => set('timezone', e.target.value)}>
                  <option value="Europe/Paris">Europe/Paris</option>
                  <option value="Europe/London">Europe/London</option>
                  <option value="America/New_York">America/New_York</option>
                </select>
              </div>
            </div>
          </div>
          <div className="settings-card-footer">
            {saved === 'general' && <span className="save-success">✓ Enregistré</span>}
            <button className="btn-primary" onClick={() => saveSection('general')}>Enregistrer</button>
          </div>
        </section>

        {/* ===== Sécurité ===== */}
        <section className="settings-card">
          <div className="settings-card-header">
            <div className="settings-card-icon settings-card-icon--blue">🔐</div>
            <div>
              <h2 className="settings-card-title">Sécurité</h2>
              <p className="settings-card-desc">Changer le mot de passe du compte {user?.email}</p>
            </div>
          </div>
          <form className="settings-fields admin-form" onSubmit={handlePasswordChange}>
            <div className="form-group">
              <label>Mot de passe actuel</label>
              <input type="password" value={pwForm.currentPassword} onChange={(e) => setPwForm((p) => ({ ...p, currentPassword: e.target.value }))} required placeholder="••••••••" autoComplete="current-password" />
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Nouveau mot de passe</label>
                <input type="password" value={pwForm.newPassword} onChange={(e) => setPwForm((p) => ({ ...p, newPassword: e.target.value }))} required placeholder="••••••••" autoComplete="new-password" />
              </div>
              <div className="form-group">
                <label>Confirmer</label>
                <input type="password" value={pwForm.confirmPassword} onChange={(e) => setPwForm((p) => ({ ...p, confirmPassword: e.target.value }))} required placeholder="••••••••" autoComplete="new-password" />
              </div>
            </div>
            {statusMsg(pwStatus)}
            <div className="settings-card-footer" style={{ padding: '16px 0 0', background: 'none', border: 'none' }}>
              <button type="submit" className="btn-primary" disabled={pwLoading}>
                {pwLoading ? 'Modification...' : 'Changer le mot de passe'}
              </button>
            </div>
          </form>
        </section>

        {/* ===== Gestion des employés ===== */}
        <section className="settings-card">
          <div className="settings-card-header">
            <div className="settings-card-icon settings-card-icon--green">👥</div>
            <div>
              <h2 className="settings-card-title">Gestion des employés</h2>
              <p className="settings-card-desc">Créer un nouveau compte employé (accès Dashboard / Commandes / Statistiques)</p>
            </div>
          </div>
          <form className="settings-fields admin-form" onSubmit={handleCreateEmployer}>
            <div className="form-row">
              <div className="form-group">
                <label>Prénom *</label>
                <input value={empForm.firstName} onChange={(e) => setEmpForm((p) => ({ ...p, firstName: e.target.value }))} required placeholder="Jean" />
              </div>
              <div className="form-group">
                <label>Nom *</label>
                <input value={empForm.lastName} onChange={(e) => setEmpForm((p) => ({ ...p, lastName: e.target.value }))} required placeholder="Dupont" />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Email *</label>
                <input type="email" value={empForm.email} onChange={(e) => setEmpForm((p) => ({ ...p, email: e.target.value }))} required placeholder="jean@smartcafe.fr" />
              </div>
              <div className="form-group">
                <label>Mot de passe *</label>
                <input type="password" value={empForm.password} onChange={(e) => setEmpForm((p) => ({ ...p, password: e.target.value }))} required placeholder="••••••••" autoComplete="new-password" />
              </div>
            </div>
            {statusMsg(empStatus)}
            <div className="settings-card-footer" style={{ padding: '16px 0 0', background: 'none', border: 'none' }}>
              <button type="submit" className="btn-primary" disabled={empLoading}>
                {empLoading ? 'Création...' : 'Créer le compte employé'}
              </button>
            </div>
          </form>
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
              <div style={{ flex: 1 }}>
                <p className="danger-action-title">Purger les anciennes commandes</p>
                <p className="danger-action-desc">
                  Supprime définitivement toutes les commandes complétées de plus de{' '}
                  <input
                    type="number" min="7" max="365" value={purgeDays}
                    onChange={(e) => setPurgeDays(parseInt(e.target.value) || 90)}
                    style={{ width: '60px', display: 'inline-block', padding: '2px 6px', borderRadius: '6px', border: '1px solid #475569', background: '#0f172a', color: '#f8fafc', textAlign: 'center' }}
                  />{' '}jours.
                </p>
                {statusMsg(purgeStatus)}
              </div>
              {showPurgeConfirm ? (
                <div style={{ display: 'flex', gap: '8px', flexShrink: 0 }}>
                  <button className="btn-secondary" onClick={() => setShowPurgeConfirm(false)}>Annuler</button>
                  <button className="btn-danger" onClick={handlePurge} disabled={purgeLoading}>
                    {purgeLoading ? 'Purge...' : 'Confirmer'}
                  </button>
                </div>
              ) : (
                <button className="btn-danger-outline" onClick={() => setShowPurgeConfirm(true)}>
                  Purger
                </button>
              )}
            </div>
          </div>
        </section>

      </div>
    </div>
  );
};

export default AdminSettings;
