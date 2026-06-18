# ── Stage 1 : Build avec Node/Vite ──────────────────────────────────────────
FROM node:18-alpine AS build

WORKDIR /app

# Copier les fichiers de dépendances en premier pour profiter du cache des layers
COPY package.json package-lock.json* ./
RUN npm ci --silent

# Copier le reste des sources et produire le bundle de production
COPY . .
RUN npm run build

# ── Stage 2 : Serveur de production Nginx ────────────────────────────────────
FROM nginx:stable-alpine

# Copier le bundle compilé vers le répertoire servi par Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Configuration Nginx : redirige toutes les routes vers index.html (SPA)
RUN printf 'server {\n\
    listen 3000;\n\
    root /usr/share/nginx/html;\n\
    index index.html;\n\
    location / {\n\
        try_files $uri $uri/ /index.html;\n\
    }\n\
}\n' > /etc/nginx/conf.d/default.conf

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
