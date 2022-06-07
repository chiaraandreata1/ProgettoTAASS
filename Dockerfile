#FROM node:12.20-alpine as node
#
#WORKDIR /app
#
#COPY package.json .
#RUN npm install
#
#COPY . .
#EXPOSE 4200
#
#CMD ["npm", "start"]
#

FROM nginx:1.17.1-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY /dist/tennis-padel-angular-frontend /usr/share/nginx/html
EXPOSE 4200
