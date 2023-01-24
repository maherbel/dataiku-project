# dataiku-project
Millenium Falcon Rescue Mission

This project aims to solve the [Millenium Falcon Challenge](https://github.com/dataiku/millenium-falcon-challenge).

## Prerequisites
To be able to compile/run/build the project, you must have:
- Java 8 or higher and make sure it is added to the path (JAVA_PATH variable defined with selected version).
- Yarn 1.22.18 or higher.
- Gradle 7.6 or higher

## How to Build/Deploy
1) Open your favourite terminal and navigate to the project's root folder.
2) Type `gradle clean` to get rid of any remaining temp build folders/files.
2) Type `gradle build` to generate the WAR "**millenium-0.0.1-SNAPSHOT.war**" through multiple gradle tasks.
3) Navigate to the folder "**build/libs**" and copy the generated WAR in the folder "**scripts**"
4) Navigate to the folder "**scripts**" and type `./install-alias.sh` to generate the launch script called "**give-me-the-odds.sh**".

## How to use the executable

Once **give-me-the-odds.sh** have been generated, you can open the terminal and execute with or without paremters and you should expect the below outcomes:

### Deploy App with 0 parameters

Deploys the App locally and can be accessed at [http://localhost:8080](http://localhost:8080).
If you don't specify any parameter, the [config file millenium-falcon.json](https://github.com/maherbel/dataiku-project/blob/readme-update/src/main/resources/config/millennium-falcon.json) will be used automatically.

### Deploy App with 1 parameter

Deploys the App locally and can be accessed at [http://localhost:8080](http://localhost:8080).
The specified parameter will be used to initialize the Millenium Falcon configuration.
The parameter can be either: 
- Relative path to the folder [resources/examples](https://github.com/maherbel/dataiku-project/tree/main/src/main/resources/examples) for example: 
```
./give-me-the-odds.sh example1/millennium-falcon.json
```
- Absolute path to any millenium falcon config of your choice for example: 
```
./give-me-the-odds.sh /ROOT_TO_EXTERNAL_CONFIG/millennium-falcon.json
```

### Deploy App with 2 parameters

This will deploy the CLI mode of the App, use the first/second param to load the Millenium Falcon/Empire data and compute the success probability.
For example:
```
./give-me-the-odds.sh example1/millennium-falcon.json example1/empire.json
```
```
./give-me-the-odds.sh /ROOT_TO_EXTERNAL_CONFIG/millennium-falcon.json /ROOT_TO_EXTERNAL_CONFIG/empire.json
```
```
./give-me-the-odds.sh example1/millennium-falcon.json /ROOT_TO_EXTERNAL_CONFIG/empire.json
```
```
./give-me-the-odds.sh /ROOT_TO_EXTERNAL_CONFIG/millennium-falcon.json example1/empire.json
```


