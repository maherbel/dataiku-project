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
2) Type `gradle build` to generate the WAR "**millenium-0.0.1-SNAPSHOT.war**" through multiple gradle tasks.
3) Navigate to the folder "**build/libs**" and copy the generated WAR in the folder "**scripts**"
4) Navigate to the folder "**scripts**" and type `./install-alias.sh` to generate the executable called "**give-me-the-odds.sh**".

## How to use the executable

Once **give-me-the-odds.sh** have been generated.
