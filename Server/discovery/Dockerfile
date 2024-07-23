# Build stage
FROM maven:3.8.7-openjdk-18 AS build

# Set the working directory inside the container for the build process
WORKDIR /build

# Copy the pom.xml file to the working directory
COPY pom.xml .

# Download the project dependencies and go offline
RUN mvn dependency:go-offline

# Copy the source code to the working directory
COPY src ./src

# Build the project and package it into a JAR file, skipping tests
RUN mvn clean package -DskipTests

# Runtime stage
# Use Amazon Corretto JDK 17 for running the application
FROM amazoncorretto:17

# Define build arguments for profile and application version
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

# Set the working directory inside the container for runtime
WORKDIR /app

# Copy the built JAR file from the build stage to the runtime stage
COPY --from=build /build/target/discovery-*.jar /app/discovery-${APP_VERSION}.jar

# Expose the port the application will run on
EXPOSE 8761

# Set environment variables for the active Spring profile and JAR version
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

# Command to run the application with the specified Spring profile
CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} discovery-${JAR_VERSION}.jar
