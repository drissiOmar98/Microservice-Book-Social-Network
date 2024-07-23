# Use the official Node.js image to build the Angular project
FROM node:18 as build-stage

# Set the working directory inside the container to /app
WORKDIR /app

# Copy the package.json and package-lock.json files to the working directory
COPY package*.json ./

# Install the project dependencies
RUN npm install

# Copy the entire project to the working directory
COPY . .

# Build the project
RUN npm run build --prod

# Use the official Nginx image with Alpine Linux as the base image for the runtime stage
FROM nginx:alpine


# Copy the built Angular project from the build stage
COPY --from=build-stage /app/dist/book-network-ui /usr/share/nginx/html


# Copy the NGINX configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80 to allow traffic to the Nginx server
EXPOSE 80

# Start NGINX
CMD ["nginx", "-g", "daemon off;"]
