# BlackJack Tasca S5.01 (niveles 1, 2 y 3)

### Albert Marin



## Descripción


_Esta es una aplicación desarrollada con Spring Boot en modo reactivo que 
desarrola la logica de un juego de BlackJack en linea.
Se han usado: **Spring WebFlux + R2DBC + Docker**
**Proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar)**
y a mas a mas operaciones del propio modelo de negocio del blackjack
**Proporciona operaciones de negocio (Hacer apuesta, Jugar turno(pedir carta o plantarse,
Añadir fondos a la wallet, y ver Ranking de puntuacion general)**
- para manejar los datos de Players se hace a través de una API REST. (Mediante MySQL) con r2dbc
- para manejar los datos de Game se hace a través de una API REST. (MongoDB) con ReactiveMongo _


## Estructura del Proyecto
```
S5T1BlackJack
├───src
    ├───main
    │   ├───java
    │   │   └───S5T1BlackJack
    │   │       │   S05T01N01Application.java
    │   │       │
    │   │       ├───config
    │   │       │       SwaggerConfig.java
    │   │       │
    │   │       ├───controllers
    │   │       │       GameController.java
    │   │       │
    │   │       ├───DTO
    │   │       │       AmountDTO.java
    │   │       │       PlayerDTO.java
    │   │       │       PlayerRankDTO.java
    │   │       │
    │   │       ├───entities
    │   │       │   │   Card.java
    │   │       │   │   Deck.java
    │   │       │   │
    │   │       │   ├───enumsEntities
    │   │       │   │       ActionType.java
    │   │       │   │       CardSuit.java
    │   │       │   │       CardValue.java
    │   │       │   │       statusGame.java
    │   │       │   │
    │   │       │   ├───mongoDb
    │   │       │   │       Game.java
    │   │       │   │       Hand.java
    │   │       │   │
    │   │       │   └───sql
    │   │       │           Player.java
    │   │       │
    │   │       ├───exceptions
    │   │       │       ActionNotAvailableException.java
    │   │       │       DeckIsEmptyException.java
    │   │       │       DuplicatedPlayerException.java
    │   │       │       GameHasFInishException.java
    │   │       │       GameHasNotBetException.java
    │   │       │       GameNotFoundException.java
    │   │       │       GlobalExceptionHandler.java
    │   │       │       PlayerNameIsNullException.java
    │   │       │       PlayerNotFoundException.java
    │   │       │
    │   │       ├───repository
    │   │       │       GameRepository.java
    │   │       │       PlayerRepository.java
    │   │       │
    │   │       └───service
    │   │           ├───gameService
    │   │           │       GameService.java
    │   │           │       GameServiceInterface.java
    │   │           │       LogicGameService.java
    │   │           │       LogicGameServiceInterface.java
    │   │           │
    │   │           └───playerService
    │   │                   PlayerService.java
    │   │                   PlayerServiceInteface.java
    │   │
    │   └───resources
    │           application-games-test.properties
    │           application.properties
    │           log4j2.xml
    │           schema.sql
    │           schema_test.sql
    │
    └───test
        └───java
            └───S5T1_BlackJack
                └───S5T1_BlackJack
                    │   S5T1BlackJackApplicationTests.java
                    │
                    ├───ControllerTests
                    │       PhpResponseTests.java
                    │
                    └───DtoTestsValidations
                            AmountDtoTestValidations.java
                            PlayerDtoTestsValidations.java
            
```

## Links

Enlace de github del codigo: [Almami fruitsAPI repository]([https://github.com/Almami679/S04T02N03](https://github.com/Almami679/S5T1_BlackJack)).

## Funcionalidades


 **Endpoints**
>
>New, MakeBet, PlayTurn, GetGame(id), GetRanking, AddAmount.


**Excepciones Personalizadas**
_ Gestionadas desde el **GlobalEceptionHandler** _
>ActionNotAvailableException.java
>DeckIsEmptyException.java
>DuplicatedPlayerException.java
>GameHasFInishException.java
>GameHasNotBetException.java
>GameNotFoundException.java
>GlobalExceptionHandler.java
>PlayerNameIsNullException.java
>PlayerNotFoundException.java

**Integración con Base de Datos**
>Se han creado dos tipos de bases de datos, una para salir a produccion y otra para test
>estan estructradas con dos archibos properties diferentes, que en el caso de test,
>el repository ya trabajaria sobre las de test
>
>Ya que en mongo no tenemos forma de gestionar el Id en autoincrement, he aadido un servicio
>que nos devuelve el ultimo Id de la base de datos para gestionarlo en el constructor de game/new
>
>Es completamente funcional con wagger accediendo a la url local:
```
http://localhost:8080/swagger-ui/index.html
```


## Tables

| Requisitos Previos  | 
| ------------- |
| Java 17 o superior      | 
| Maven 3.8 o superior      | 
| Docker Compose (https://docs.docker.com/compose/)|
| Docker (https://www.docker.com/)| 
| MongoDB Compass     |


## Instalación

### Clona el repositorio:
```
git clone <repository-url>
```

### Navega al directorio del proyecto:
```
cd <project-directory>
```
### Compila el proyecto:
```
./mvnw clean install
```
### Se debera iniciar el Servicio mongoDB ubicado en:
```
C:\Program Files\MongoDB\Server\8.0\bin\mongod
```
### Al ser en Reactivo se deberan crear las bases de datos antes en local.
```
S5T1BlackJack\src\main\resources\schema.sql
S5T1BlackJack\src\main\resources\schema_test.sql
```
