# Stacja 04 — CTF: wyciągnij sekret przez otwarty JDWP.
FROM eclipse-temurin:21-jre
WORKDIR /opt/app
COPY target/ctf.jar /opt/app/ctf.jar

# CELOWO wystawiony debugger — o to właśnie chodzi w tym ćwiczeniu.
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
# Flaga wstrzykiwana przez środowisko (compose/scale nadaje unikalną per instancja).
ENV WORKSHOP_FLAG="FLAG{docker-domyslna-flaga}"

EXPOSE 8080 5005
ENTRYPOINT ["java", "-jar", "/opt/app/ctf.jar"]
