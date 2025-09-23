necessari:
Docker installato - Java 17+ - Maven

eseguire da terminale prima di runnare l'app:
docker-compose up -d  (per scaricare i servizi)
docker exec ollama-service ollama pull nomic-embed-text (per scaricare il modello che non sono riuscito a far scaricare automaticamente)

infine con ./mvnw spring-boot:run sarà possibile testare l'app su swagger

L'applicazione caricherà automaticamente 5 libri di esempio al primo avvio
