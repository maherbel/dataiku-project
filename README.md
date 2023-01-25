# Dataiku Technical Project

This project aims to solve the [Millenium Falcon Challenge](https://github.com/dataiku/millenium-falcon-challenge).

## Prerequisites

To be able to compile/run/build the project, you must have:
- Java 8 or higher and make sure it is added to the path (JAVA_PATH variable defined with selected version).
- Yarn 1.22.18 or higher.
- Gradle 7.6 or higher

## How to Build/Deploy
1) Open your favourite terminal and navigate to the [frontendapp](https://github.com/maherbel/dataiku-project/tree/main/src/main/frontendapp) folder.
2) Type `yarn build` to build the JS/React code which will create the static files under `frontend/build/static` (these will be packaged into the final WAR).
3) Navigate to the project's root folder.
4) Type `gradle clean build` to clean up any temp files and generate the WAR "**millenium-0.0.1-SNAPSHOT.war**".
5) Navigate to the folder "**build/libs**" and copy the generated WAR into the folder "**scripts**".
6) Navigate to the folder "**scripts**" and type [`./install-alias.sh`](https://github.com/maherbel/dataiku-project/blob/main/scripts/install-alias.sh) to generate the launch script called `give-me-the-odds.sh`.

NB: The only condition is to keep the WAR, `./install-alias.sh` and `give-me-the-odds.sh` in the same folder (can be scripts or any another external folder).

## How to use the executable

Once **give-me-the-odds.sh** have been generated, you can open the terminal and execute it with or without parameters to either deploy the App or execute the CLI.

### Deploy App with 0 parameters

Deploys the App locally and can be accessed at [http://localhost:8080](http://localhost:8080).
If you don't specify any parameter, the default config file [config/millenium-falcon.json](https://github.com/maherbel/dataiku-project/blob/main/src/main/resources/config/millennium-falcon.json) will be used automatically.

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

This will enable the CLI mode of the App, use the first/second param to load the Millenium Falcon/Empire data and compute the success probability.
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
You can also move around nodes by dragging them and you can click on the rectangle near the zoom controls to re-center the graph.

- File Upload zone (on the upper-center of the screen):
This is where you can upload or drag and drop a file containing the empire intercepted data. Once that's done, the result will be computed and further details will be provided on the right part.

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

Whenever the app is launched using 2 parameters (please refer to the section above) to load both config files, the CLI mode will be enabled and the mission success probability will be printed on the console as below:

<img width="885" alt="CLI Examples" src="https://user-images.githubusercontent.com/10635526/214306163-9013d442-27fc-4414-92a4-972131b10376.png">

### Logs and Rest API

#### Logs

Logs containing extra details on the configs, route success probability computation, etc.. are being generated on the root of the folder where the script is being launched.
The kind of details you can find are as below:
```
01:50:23.038 [main] INFO  c.d.millenium.cli.CommandLineRunner - Computing mission result from CLI..
01:50:23.118 [main] INFO  c.d.millenium.business.PathOptimizer - Compute mission result started. Departure: [Tatooine], Arrival: [Endor], Countdown: [10], Autonomy: [6]
01:50:23.119 [main] INFO  c.d.millenium.business.PathOptimizer - 2 Possible paths found that lead from Tatooine to Endor in 10 days.
01:50:23.119 [main] INFO  c.d.millenium.business.PathOptimizer - [Planet{name='Tatooine', day='0'}, Planet{name='Dagobah', day='6'}, Planet{name='Dagobah', day='7', Refuel}, Planet{name='Hoth', day='8'}, Planet{name='Endor', day='9'}] is a possible path.
01:50:23.119 [main] INFO  c.d.millenium.business.PathOptimizer - [Planet{name='Tatooine', day='0'}, Planet{name='Hoth', day='6'}, Planet{name='Hoth', day='7', Refuel}, Planet{name='Endor', day='8'}] is a possible path.
01:50:23.119 [main] INFO  c.d.millenium.business.PathOptimizer - Computing the best success probability from all possible paths..
01:50:23.119 [main] INFO  c.d.millenium.business.PathOptimizer - [[Planet{name='Tatooine', day='0'}, Planet{name='Dagobah', day='6'}, Planet{name='Dagobah', day='7', Refuel}, Planet{name='Hoth', day='8', Risky}, Planet{name='Endor', day='9'}]] is the best path so far as it has [1] risky positions and arrives on [Endor] on Day [9].
01:50:23.119 [main] INFO  c.d.millenium.business.PathOptimizer - [[Planet{name='Tatooine', day='0'}, Planet{name='Dagobah', day='7', delay='1'}, Planet{name='Dagobah', day='8', Refuel}, Planet{name='Hoth', day='9'}, Planet{name='Endor', day='10'}]] is the best path so far as it has [0] risky positions and arrives on [Endor] on Day [10].
01:50:23.119 [main] INFO  c.d.millenium.services.RouteService - Compute mission result ended with a success probability of [100.0%].
01:50:23.120 [main] INFO  c.d.m.aspect.MethodDurationAspect - Method computeMissionResult in class com.dataiku.millenium.services.RouteService took 81 ms
01:50:23.120 [main] INFO  c.d.millenium.cli.CommandLineRunner - ****************** Results ******************
01:50:23.120 [main] INFO  c.d.millenium.cli.CommandLineRunner - Mission Success Probability: 100.0
01:50:23.120 [main] INFO  c.d.millenium.cli.CommandLineRunner - *********************************************

```
So please refer to these if you need more details on the result computation.

You can use the command below to stream the application log:
```
tail -f /PATH_TO_THE_LOGS/application.log
```

#### Rest API (Postman)

If you are familiar with Postman, you can import the configuration file from [here](https://github.com/maherbel/dataiku-project/blob/main/Postman_API_config.json) and you'll be able to query the below endpoints:

`/healthcheck` (GET)

Check that app is deployed/running.

`/missionResultSuccess` (POST)

Compute the mission's success probability given the empire data (passed as body).

`/missionData` (GET)

Fetches the current mission's data (extracted from the millenium falcon config file).


### Missing features to roll to PROD
- Plug the logs (backend/frontend) to a log aggregator such as Splunk/Datadog.
- Track the technical performance of the App especially the TP99, TP90, TP50 of the endpoint `/missionResultSuccess` and setup alerts to be aware if at some point the performance is degraded below a certain threshold.
- Add user metrics to have a better understanding of the feature usage/success/failure with a platform like Amplitude.
- Add unit tests and Cypress tests for UI code.
- Enhance the logging to the UI level with a proper logger (using a dedicated Util).
- Add coverage on the Java code using Jacoco.
- Audit the component [GraphUtil.js](https://github.com/maherbel/dataiku-project/blob/main/src/main/frontendapp/src/utils/GraphUtil.js) and move if necessary the heavy computation code to the backend (with multithreading) as the part that ensures the min distance between nodes might not scale correctly.
- Split the class [PathOptimizer.java](https://github.com/maherbel/dataiku-project/blob/main/src/main/java/com/dataiku/millenium/business/PathOptimizer.java) into multiple classes for different responsabilities such as one to compute the possible paths, another one to compute the best path and another one to compute the final success probability.

