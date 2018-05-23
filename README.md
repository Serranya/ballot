# Condorcat

A java web app for holding ballots with the [condorcet method](https://en.wikipedia.org/wiki/Condorcet_method).

## Building
The project can be build with [maven](https://maven.apache.org/):

`mvn -Prelease,fatjar package` for the standalone .jar or `mvn -Prelease,war package` for the .war file.

Instead of `mvn` you can also use `mvnw`. This installs a local maven version in the project directory so that you
don't need a global maven installation.

## Configuration
None. Just start the app.

## Developing
Instead of the `release` maven profile use the `dev` profile.

## Creating project reports
You can create a report containing the test results as well as static code analysis and style checks with:

`mvn -Pdev,fatjar package site:site`
