# STEP 1: Define the base image
# Using the stable, widely-supported Eclipse Temurin JRE 17 image (based on Debian)
FROM eclipse-temurin:17-jre-focal

# STEP 2: Install X11 dependencies and the critical font packages
# This installs the required libs for remote GUI rendering and the fonts (ttf-dejavu) 
# needed for your custom Swing Font objects (e.g., new Font("Arial", ...)).
RUN apt-get update && \
    apt-get install -y \
    # X11 Libraries for AWT/Swing rendering over DISPLAY
    libxext6 \
    libxtst6 \
    libxi6 \
    # CRITICAL FIX: Font packages for Java/Swing to find and render fonts
    fontconfig \
    ttf-dejavu \
    x11-utils && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Stage 2: Copy Application Files and Dependencies
# This copies your compiled JAR and its dependencies.
COPY dist/lib /app/lib
COPY dist/InventoryManagmentSystem.jar /app/app.jar

# Stage 3: Define the Entry Point
# We use the standard 'java' command to launch the app, 
# relying on the host VcXsrv for the display connection.
ENTRYPOINT ["java", "-cp", "app.jar:lib/*", "main.main"]
