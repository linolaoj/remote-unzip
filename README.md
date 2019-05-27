# remote-unzip
The purpose of this project is to retrieve documentation from hotfix/fixpack without having to download the entire zip file.

## How to run the service
* Set up the *liferayAuthURL* and *fixpacksBaseURL* with prefix *sopapo* on **src/main/resources/application.properties**
* Run gradle **appengineRun**

(To know more about configuration properties please see: https://www.baeldung.com/configuration-properties-in-spring-boot)

## How to run tests
* Set up *username* and *password* with prefix *patcher* on **src/test/resources/test.properties**
* Run **gradle appengineRun** to start server
* Run **gradle test** to run tests

To run this project you need to have installed the Google Cloud SDK. Please see https://cloud.google.com/sdk/install to know more about it. 

## How to build Docker image
* Run **gradle buildDocker -x test**
