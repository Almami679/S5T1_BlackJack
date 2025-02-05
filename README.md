# Creació d'ApiRest amb SpringBoot i WebFlux, per gestionar partides de BlackJack
En aquest exercici pràctic, creem una API en Java amb Spring Boot per a un joc de Blackjack. L'API està dissenyada per connectar-se i gestionar informació en dues bases de dades diferents: MongoDB i MySQL. El joc de Blackjack s'implementa amb totes les funcionalitats necessàries per jugar, com la gestió de jugadors, mans de cartes i regles del joc. L'aplicació dissenyada és reactiva amb WebFlux.

Creem un projecte Spring amb Spring Initializr (https://start.spring.io/), amb gestor de dependències Maven i afegim les dependències necessàries relacionades en l'apartat "Requisits tècnics" més avall, les que no estan disponibles en el Initializr s'afegiran manualment en el projecte a posteriori.

### Bases de dades:
Fem servir mySQL per persistir dades dels jugadors que han participat en algún moment en algún joc del BlackJack, i MongoDb per als detalls dels jocs (jugador, mans, estat del joc). Implementem un controlador per a cada base de dades:
### El PlayerController gestiona dos endpoints:
- Modificar nom del jugador: Mètode PUT, endpoint /player/{playerId} on playerId és l'identificador del jugador, body conté el nom del nou jugador, retorna resposta 200OK amb informació actualitzada del jugador.
- Obtenir el rànquing dels jugadors per puntuació descendent: Mètode GET, endpoint /ranking, i retorna resposta 200 OK amb llista de jugadors ordenada.
El GameController gestiona 4 endpoints:
- Crear una nova partida de Blackjack: Mètode POST, endpoint /game/new, body conté el nom del jugador, retorna resposta 201 Created amb informació sobre la partida creada.
- Obtenir els detalls d'una partida específica: Mètode GET, endpoint /game/{id} on id és l'id del joc, retorna resposta 200 OK amb informació detallada sobre la partida.
- Eliminar una partida de Blackjack existent: Mètode DELETE, endpoint /game/{id}/delete on id és l'id del joc, retorna resposta 204 No Content si la partida s'elimina correctament.
- Realitzar una jugada en una partida: Mètode POST, endpoint /game/{id}/play on id és l'id del joc, body conté un objecte amb l'acció a realitar (BET, HIT, STAND, DOUBLE), i l'import de l'aposta (només es té en compte amb l'acció BET).

### Serveis
Corresponents a aquests dos controladors, disposem de 2 serveis que a més, realitzen altres operacions:
- Crear un nou jugador en cas de què el nom indicat en el body de l'endpoint "game/new" no existeixi en la base de dades mySQL, en cas contrari es recuperen les seves dades.
- Actualitzar la puntuació de un jugador al final de la partida (en cas de victòria del jugador)
- Dur a terme cada acció del joc amb les seves particularitats

### Control d'excepcions
El control d'excepcions està centralitzat en la classe GlobalExceptionHandler i retorna per cada excepció capturada una resposta HTTP adecuada.

### Documentació de l'API
Documentem l'API de l'aplicació mitjançant l'eina Swagger, fent servir les annotacions necessàries per proporcionar la informació pertinent a l'usuari. Aquesta documentació està disponible a la url http://localhost:8080/swagger-ui/webjars/swagger-ui/index.html, i també permet provar tots els endpoints, com a alternativa a Postman.

### Tests
S'han implementat testos dels mètodes del controlador PlayerController i del servei PlayerServiceImpl, fent servir JUnit5 i Mockito per generar mocks 
## Requisits tècnics
- MySQL WorkBench 8.0
- MongoDb 8.0
- Maven: Apache Maven 3.9.9
- Java version 22
- Sistema operatiu: Windows

Projecte Maven amb les dependències següents:
- Aplicació reactiva: org.springframework.boot:spring-boot-starter-webflux i io.projectreactor.netty:reactor-netty (servidor HTTP reactivo)
- DevTools: org.springframework.boot:spring-boot-devtools
- Connexions a bases de dades: 
  SQL (R2DBC)--> dev.miku:r2dbc-mysql, org.springframework.boot:spring-boot-starter-data-r2dbc i com.mysql:mysql-connector-j
  MongoDb --> org.springframework.boot:spring-boot-starter-data-mongodb-reactive
- Documentació Swagger --> io.swagger.core.v3:swagger-annotations, org.springdoc:springdoc-openapi-starter-webflux-ui
- Validaciones: jakarta.validation:jakarta.validation-api
- Logger --> org.apache.logging.log4j:log4j-core, org.apache.logging.log4j:log4j-api
- Tests --> org.springframework.boot:spring-boot-starter-test, io.projectreactor:reactor-test
## Instalación: 
1. Clonar el repositorio de Github
git clone https://github.com/g-lemoing/S05T01N01.git
2. Abrir el IDE e importar el proyecto desde el repositorio local desde File > Open.
3. Abrir el MySQLWorkbench (descargarlo e instalarlo si necesario desde https://dev.mysql.com/downloads/) y crear una conexión si no n'existeix cap. Importar el fitxer db_blackjack_players.sql per crear la base de dades i la taula players.
4. Arrancar el servidor MongoDb, abrir el MongoDb Compass, instalarlo previamente si necesario (mongodb.com/es). Crear una base de dades 'blackjack' amb una col·lecció 'game'
5. En la consola, navegar fins el directori del projecte S05T01N01, i executar mvn clean install (o .\mvnw.cmd clean install) per garantir la instal·lació correcta de les dependències.
6. Actualitzar el fitxer application.properties amb els paràmetres de connexió a les bases de dades MySQL i MongoDb en cas de diferir de les establertes en aquest projecte

## Ejecución:
Localizar la clase Main y ejecutar S05T01N01Application.
Abrir el Swagger (http://localhost:8080/swagger-ui/webjars/swagger-ui/index.html) y ejecutar las peticiones tot seguint les instruccions:
- Crear nova partida: http://localhost:8080/game/new, entrar nom del jugador en el body
- Consultar dades de la partida: http://localhost:8080/game/{id}, on {id} és l'identificador únic del joc
- Realitzar una jugada dins de la partida: http://localhost:8080/game/{id}/play, on {id} és l'identificador únic del joc. Informar en el body l'acció a dur a terme i l'aposta si s'escau
- Eliminar una partida: http://localhost:8080/game/{id}/delete, on {id} és l'identificador únic del joc
- Canviar el nom d'un jugador: http://localhost:8080/player/{playerId}, on {playerId} és el seu identificador únic.
- Obtenir el rànquing dels jugadors segons la seva puntuació decreixent: http://localhost:8080/ranking

## Contribucions:
1. Crear un fork del repositorio: 
2. Clonar el repositorio hacia el directorio local marcado por git bash
 git clone https://github.com/YOUR-USERNAME/S05T01N01
3. Crear una rama
git branch BRANCH-NAME
git checkout BRANCH-NAME
4. Realizar cambios o comentarios, y hacer un commit: git commit -m 'mensaje cambios'
5. Subir cambios a tu nueva rama: git push origin BRANCH-NAME
6. Hacer un pull request
