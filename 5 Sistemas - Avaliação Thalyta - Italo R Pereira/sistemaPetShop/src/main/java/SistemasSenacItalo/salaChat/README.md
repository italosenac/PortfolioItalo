SalaChat - Console-based Spring Boot application
================================================

Estrutura e instruções rápidas:

- Projeto baseado no POM enviado por você; está localizado na raiz.
- Para compilar e executar:
    mvn clean package
    mvn spring-boot:run
- OU:
    mvn package
    java -jar target/sistemaPetShop-0.0.1-SNAPSHOT.jar

Observações:
- App roda no console via CommandLineRunner.
- Limite de usuários logados: 10 (configurado em InMemoryChatRepository).
- Pacotes seguem estrutura DDD (domain / infrastructure).
