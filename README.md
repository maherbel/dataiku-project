# Dataiku Technical Project

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

Once **give-me-the-odds.sh** have been generated, you can open the terminal and execute with or without parameters to either deploy the App or execute the CLI.

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

## How to use

### Dashboard Elements

This is the Millenium Falcon Dashboard:

<img width="1725" alt="Main Dashboard" src="https://user-images.githubusercontent.com/10635526/214275884-65d7f521-ac35-445c-9395-8f75024d97ea.png">

- Flow Stepper (on the left):
These are the instructions for further details on how to use the App, please make sure to go through them.

- Routes Graph (in the center of the screen):
This is the graph that represents the Millenium Falcon routes from planet to planet with the travel times, the departure and the arrival of the mission => **this data is computed from the millenium-falcon.json file.**

You can click on the button `Regenerate Graph` to refresh the Graph's UI if the nodes are not well positioned or if the graph is not well rendered.
You can also click on the rectangle near the zoom controls to re-center the graph.

- File Upload zone (on the upper-center of the screen):
This is where you can upload or drag and drop a file containing the empire intercepted data. Once that's done, the result will be computed and further details are provided on the right part.

- Mission Success Probability (on right part of the screen, see next section for screenshot):
This will contain the mission success probability and the detailed route if there is a solution (**meaning that the result is greater than 0%**).

### Mission Result Dashboard

Once the `empire.json`file is uploaded, the app will compute the mission success probability from the Departure to Arrival given the autonomy, routes, countdown and bounty hunters positions as below:

#### No possible solution

<img width="1725" alt="No possible solution" src="https://user-images.githubusercontent.com/10635526/214279451-cb9d17f7-cf70-4ce0-992a-77e84cb0de06.png">

#### Route with 2 risky positions

<img width="1725" alt="Orange Solution" src="https://user-images.githubusercontent.com/10635526/214279132-fedf9a03-9de7-4ddc-8204-b7510bdd029d.png">

#### Route with 1 risky positions

<img width="1725" alt="Green Solution with risky positions" src="https://user-images.githubusercontent.com/10635526/214279276-392a8caf-ccd9-450e-9179-072b1a45c6f4.png">

#### Route with 0 risky positions

<img width="1725" alt="Green solution with no risky positions" src="https://user-images.githubusercontent.com/10635526/214279092-82f3a08b-ebf4-4049-8fdd-0b4ed9447350.png">


### CLI

Whenever the app is launched using 2 parameters (please refer to the section above), the CLI mode will be enabled and the mission success probability will be printed on the console as below:

