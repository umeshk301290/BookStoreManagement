# BookStoreManagement

## environment details
Dev, Qa  and Prod environments are supported on this application.

databse for dev:-  jdbc:h2:file:C:/data/BookStoreDev
databse for qa:-  jdbc:h2:file:C:/data/BookStoreQa
databse for prod:- jdbc:h2:file:C:/data/BookStoreProd


## instructions to launch

After runnig maven install , just run the jar file generated under targets folder using java -jar name of jar -- name of spring active spring profile 
for eg:- java -jar bookstore-0.01-SNAPSHOT.jar --spring.profiles.active=dev

## database dependencies :- 
H2 database is used for the application.

