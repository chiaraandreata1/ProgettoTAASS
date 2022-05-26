FROM node:12.20-alpine as node

WORKDIR /app

COPY package.json .
RUN npm install

COPY . .
EXPOSE 4200

CMD ["npm", "start"]

