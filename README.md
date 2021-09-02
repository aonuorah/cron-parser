# cron-parser

A command line application which parses a cron string and expands each field
to show the times at which it will run

### Prerequisites

- Java 11
- Maven 3.6.0+

### Running application

1. first build the jar file by running `mvn clean install` in application's parent directory
2. then run application using `java -jar target/cron-parser-1.0-SNAPSHOT.jar "cron expression"` Note: 
cron expression wrapped in quotes

### Features
- Supported fields: minute, hour, day of month, month, day of week
- Supports for all cron special characters: * / , -

### Example

```shell script
$ java -jar target/cron-parser-1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"

minutes        0 15 30 45
hours          0
day of month   1 15
month          1 2 3 4 5 6 7 8 9 10 11 12
day of week    1 2 3 4 5
command        /usr/bin/find
```