# frontend/Dockerfile

FROM node:18-alpine

WORKDIR /app

# package.json 파일 복사 및 의존성 설치

COPY package.json package-lock.json ./
RUN npm install

# 소스 코드 복사

COPY . .

EXPOSE 3000

CMD ["npm", "start"]

