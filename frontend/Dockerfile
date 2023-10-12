# Build stage:
FROM node:alpine AS build
WORKDIR /app
# install app dependencies
# (copy _just_ the package.json here so Docker layer caching works)
COPY package.json package-lock.json ./
RUN npm install
# build the application
COPY . ./
RUN npm run build

# Final stage:
FROM node:alpine
WORKDIR /app
# install dependencies
COPY package.json package-lock.json ./
RUN npm install --production
# get the build tree
COPY --from=build /app/build/ ./build/

EXPOSE 3000
# explain how to run the application
ENTRYPOINT ["npx"]
CMD ["serve", "-s", "build"]
