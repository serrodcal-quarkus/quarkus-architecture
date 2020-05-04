# hr-front

> My badass Nuxt.js project

## Build Setup

```bash
# install dependencies
$ npm install

# serve with hot reload at localhost:3000
$ npm run dev

# build for production and launch server
$ npm run build
$ npm run start

# generate static project
$ npm run generate

# generate static project for NGINX
$ npm run build --spa
```

For detailed explanation on how things work, check out [Nuxt.js docs](https://nuxtjs.org).

## NGINX

For detailed explanation on how deploy on NGINX, check out [NGINX-proxy](https://nuxtjs.org/faq/nginx-proxy/).

## Build Docker Image

```bash
$ docker build -f Dockerfile/Dockerfile -t serrodcal/hr-frontend-server:1.0.0-alpine .
```

Further information in this [page](https://jonathanmh.com/deploying-a-nuxt-js-app-with-docker/)
