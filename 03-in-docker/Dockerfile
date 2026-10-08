# Stacja 03 — debugowanie aplikacji w kontenerze przez JDWP.
#
# CELOWY BŁĄD DYDAKTYCZNY: patrz komentarz przy COPY poniżej.
FROM eclipse-temurin:21-jre

WORKDIR /opt/app

# Kopiujemy zbudowany artefakt. Wygląda niewinnie, prawda?
# (Podpowiedź: sprawdź debuggerem, czy /config/app.properties jest na classpath.)
COPY target/app.jar /opt/app/app.jar

# JDWP nasłuchuje na wszystkich interfejsach w KONTENERZE (nie na Twoim hoście!).
# suspend=n → aplikacja startuje od razu, podpinasz się w dowolnym momencie.
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

EXPOSE 5005
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
