# LoginDemo

How to start the LoginDemo application
---

1. Run `mvn clean install` to build your application
2. Go to working directory by running `cd cd googleOAuthLoginDemo/`
3. Run migration before running server with `java -jar target/googleOAuthLoginDemo-1.0-SNAPSHOT.jar migrate configuration.yml -a migrate`
4. Start application with `java -jar target/googleOAuthLoginDemo-1.0-SNAPSHOT.jar server configuration.yml`
5. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
