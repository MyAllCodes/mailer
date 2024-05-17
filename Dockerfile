# Use an official OpenJDK 8 runtime as a base image
FROM openjdk:8-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/mailer.jar /app/mailer.jar

# Expose the port that your application runs on
EXPOSE 8050

# Command to run the application
CMD ["java", "-jar", "mailer.jar"]
