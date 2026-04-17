WIP
Intro
A basic application to track items an employee may have. Automatically sends email to boss users when an item needs servicing or renewel. 
The website is deployed via digital ocean on the link below.

https://itemhealtherm.tech/

The goal of this project was to familiarize myself with the spring boot platform and to build somthing similar to a business application that might be built at a SIER company. I hope that this app shows I am ready to build business applications.
The app has a cronjob that runs daily after midnight. It checks each registered item in the database and compares it to the new day: if an object requires some form of maintenence then an automatic email is sent to whoever is the boss of the item's owner. These emails are captured via Mailhog so that emails are not actually being sent out.
This app has a working CI/CD pipeline using github actions docker, and unit and integration testing to build test and push each deployed change to the digital ocean's droplet (what digital ocean calls an instance of a server). The database used is postgres, has a mybatis mapper to map sql statements to the service layer, and Flywaydb to run db migrations. It uses the baked in spring routing and secruity for the rest of the backend. The frontend is thymeleaf, JS, html, and css hosted on the same spring-boot build so that cors configuration and all that wouldn't be necessary.

Project Walkthrough System Architecture
The webapp is built in one monolith architecture as the frontend uses the lightweight Thymeleaf framework, some JS scripting, and html and css only. The main purpose for this app was for me to try spring-boot dev for backend usage so a basic frontend was fine.

Packages Used
Security : Bcrypt, Jsonwebtoken, Spring-boot security
Utility: Cron, Spring-mail, Jackson, Mybatis, Flywaydb, Mailhog
Testing: Spring-security testing, Junit
Frontend: Thymeleaf
#Need to lookup json web, logging

Package Explination
Security
Spring-security: Spring-boot's baked in security, utalizing a security chain to authenticate requests. For the app, only boss users are allowed to do crud and view certain pages. This meant a login with password, password hashing, and authentication needs to be setup in the app. Spring-security served this purpose.
bcrypt: Hashing package for encrypting user passwords.
#note to self lookup if not using jsonwebtoken

Utility:
axios: API Request package, used to send HTTP GET request to Lichess dailypuzzle backend. compression: Compresses HTTP responses. cors: Cross-Origin Resource Sharing, so that the backend api is accessable to the frontend.
cron: An automatic script runner used to check for expiring or needing to be updated items. If it finds one, it sends an automatic email to the boss user of the items owner.
Spring-mail: For sending the mail used in the cron-job script.
Mailohg: Captures the emails being sent from the server.
Jackson: JSON Oobjectmapper to process JSOn from front end to backend.
Mybatis: SQL mapper to make and connect SQL commands from backend service-layer to DB
Flywaydb: Run db migrations

Testing
Spring-test: Built in spring testing for unit testing, integration testing.
Junit: Mocking library.

Frontend
Thymeleaf: Lightweight frontend library that handles templatet and fragments.

File structure

|ERM-Employee-Item-Health-App/
├──.devcontainer/ # Build settings for deployment.
├──.github/workflows/ # Github actions.
├──logs/ # Where logs end up.
├──Dockerfile/ # Dockerfile build setup
├──tests/ # Testing setup teardown files
├──docker-compose.yml/ # Docker build settings
|──Everything else in root: Standard spring-boot build files
├──src/ # The source code.
| └──test/ # Where the testing suite is, unit testing and integration testing.
| └──main/ #
|   └────resources/ # Front end and db migration files
|     └────db/migrations/ # Flyway migration files for DB setup
|     └────mappers# MyBatis sql mapping files
|     └────static/js # JS script for frontend
|     └────templates # HTML pages
|     └────application.yml/propertiex/logback # settings setup, logs
|   └────java/com/healthapp/itemhealth/ # Backend
|     └────config # Flyway config file.
|     └────controller # Controller layer.
|     └────exception # Exception handler.
|     └────mapper # Connect Mybatis to backend service layer
|     └────model # Model layer
|     └────security # Spring-security filter chain.
|     └────service # service layer
|       └────health # health template logic for health service
|         └────scheduler # cronjob scripts
|     └────ItemhealthApplication.java # App entry point 
  
Backend
WIP

Cron Job
WIP

Security
WIP

Frontend
WIP

Testing
WIP


CI/CD/Deployment
The website server is hosted through Digital Ocean, where a droplet is setup and configured so that Github actions pushes each repo update to the server for CI/CD deployment. The server docker images are built on each GitHub push that passes linting and testing, ensuring faulty code is never deployed. Website IP masking and security from external attacks are prevented through the use of Cloudflare and a Digital Ocean firewall.
