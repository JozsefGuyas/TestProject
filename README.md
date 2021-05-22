# TestProject


## System requirements:
- os: windows / linux
- java version: 11

## Build
- git clone https://github.com/JozsefGuyas/TestProject.git
- mvn clean package

## Running
-java -jar TestProject-1.0.1.jar -i ./incoming

or

-java -jar TestProject-1.0.1.jar -i ./incoming -t 5 -n 2 -d 70

### Options
#### Required parameters
- i Incomming folder path (absolute or relative)

#### Optional parameters
- t Number of working threads.
- n Word pairs count.
- d Idle timeout in second

