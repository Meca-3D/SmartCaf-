import React, { useState } from 'react';
import './Login.css';

import { useUserStore } from '../store/userStore';

const Login = ({ onLogin }) => {
    const setUser = useUserStore((state) => state.setUser);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('http://localhost:8081/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ email, password }),
      });

      // On gère le cas où le backend renvoie autre chose que du JSON (HTML, vide, etc.)
      const contentType = response.headers.get('content-type') || '';
      let data = {};

      if (contentType.includes('application/json')) {
        data = await response.json();
      } else {
        const text = await response.text();
        // si le serveur renvoie du texte/html, on le garde pour debug
        data = { message: text };
      }

      if (response.ok) {
        setUser(data.user);
        onLogin && onLogin(data);
      } else {
        // On donne un message utile, sans bloquer si message absent
        setError(data?.message || `Erreur de connexion (HTTP ${response.status})`);
        console.error('Login failed:', {
          status: response.status,
          data,
        });
      }
    } catch (err) {
      // Erreur réseau / CORS / serveur down
      console.error('Login network/server error:', err);
      setError(
        err?.message
          ? `Erreur serveur : ${err.message}`
          : 'Erreur serveur (réseau ou backend indisponible)'
      );
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit}>
        <h2>Connexion</h2>
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
        <button type="submit">Se connecter</button>
        {error && <div className="error">{error}</div>}
      </form>
    </div>
  );
};

export default Login;
