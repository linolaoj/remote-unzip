# remote-unzip
The purpose of this project is to retrieve documentation from hotfix/fixpack without having to download the entire zip file.

## How to run tests
* Run **gradle bootRun** to start server
* Add your config on **src/test/resource/test.properties**
* Run **gradle test** to run tests

## How to build Docker image
* Run **gradle buildDocker -x test**
