
# EasyCreds - Credit Card Management System

A simple, easy-to-use and secure application for managing credit card related actions.


## Features

- #### User Registration: New accounts can be created by users.
- #### Email Validation: Secure email validation codes are used to activate accounts.
- #### User authentication: Current users are able to safely access their accounts.
- #### Role Based Access : The application has role based access to specific endpoints.
- #### Dynamic Data population : Data to State and City entities are fetch from external api. (Rapid API)
- #### Credit Card application : Logged-in users can apply for a credit which will be reviewed by Admin, and then it'll be either rejected or approved.




## Technologies Used

- Java (Version : 21)
- Spring Boot (Version : 3.3.3)
- Spring Data Jpa
- MySql Database
- Spring Security (Version : 6+)
- JWT Token Authentications and Email Based Activation
- OpenAPI and Swagger UI Documentation
- GMail for mailing




## Run Locally

1. #### Clone the EasyCredsSpringBoot

```bash
    git clone https://github.com/dilip0418/EasyCredsSpringBoot
```

2. #### Ensure you have the following installed:

Check for Prerequisites
```bash
    java -version
```

Java Development Kit (JDK):
Version compatible with the project (usually JDK 21 or higher):
```bash
    java -version
```

Maven:
```bash
    mvn -version
```

3. #### Import the Project into Your IDE
Use an IDE like IntelliJ IDEA(Suggested), Eclipse, or VS Code.
Open the project directory in your IDE to manage and run it more effectively.

4. #### Install Dependencies
Run the following command to download all dependencies specified in pom.xml (for Maven):
```bash
    mvn clean install 
```

5. #### Configure Application Properties
Check the application.yml file in the `src/main/resources` directory.
Update configurations like `database URLs`, `credentials`, or `API keys`.

6. #### Run the Application
To start the Spring Boot application:
```bash
    mvn spring-boot:run
```
or just use the run button in the IDE you're using.

7. Access the Swagger-UI using
   [EasyCreds-Swagger](http://localhost:8088/api/v1/swagger-ui/index.html)
## Contributing

Contributions are always welcome! 🤘

Any suggestions and improvements to this project are openly welcome 😊.

#### Bonus:
If you want to code frontend for this project. Ping me and we can collaborate and code. 🤝

## Feedback

If you have any feedback, please reach out to us at sudheer0418@gmail.com

## 🌐 Connect with Me

Let’s connect! Feel free to check out my work or reach out via:

[![Portfolio](https://img.shields.io/badge/Portfolio-%230A66C2?style=for-the-badge&logo=About.me&logoColor=white)]([https://your-portfolio-link.com](https://dilip-sudheer.netlify.app/))
[![LinkedIn](https://img.shields.io/badge/LinkedIn-%230A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/dilip-kumar-bk/)
[![Dev.to](https://img.shields.io/badge/Dev.to-%230A66C2?style=for-the-badge&logo=dev.to&logoColor=white)](https://dev.to/dilipkumar_0418)



