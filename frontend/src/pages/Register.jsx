import React, { useState } from 'react';
import './Register.css';

const Register = ({ onRegister }) => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (password !== confirmPassword) {
      setError('Les mots de passe ne correspondent pas');
      return;
    }

    try {
      const response = await fetch('http://localhost:8081/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ firstName, lastName, email, password }),
      });

      const data = await response.json().catch(() => ({}));

      if (response.ok) {
        setSuccess(data.message || 'Compte créé avec succès !');
        onRegister && onRegister(data);
      } else {
        setError(data.message || 'Erreur lors de l’inscription');
      }
    } catch (err) {
      setError('Erreur serveur');
    }
  };

  return (
    <div className="register-container">
      <form onSubmit={handleSubmit}>
        <h2>Inscription</h2>

        <input
          type="text"
          placeholder="Prénom"
          value={firstName}
          onChange={e => setFirstName(e.target.value)}
          required
        />

        <input
          type="text"
          placeholder="Nom"
          value={lastName}
          onChange={e => setLastName(e.target.value)}
          required
        />

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Mot de passe"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Confirmer le mot de passe"
          value={confirmPassword}
          onChange={e => setConfirmPassword(e.target.value)}
          required
        />

        <button type="submit">S’inscrire</button>

        {error && <div className="error">{error}</div>}
        {success && <div className="success">{success}</div>}
      </form>
    </div>
  );
};

export default Register;
