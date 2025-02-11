FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/blackjack-api.jar blackjack-api.jar

EXPOSE 8080

CMD ["java", "-jar", "blackjack-api.jar"]