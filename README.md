# A* algorithm

## Howto execute solution

### Docker

Simply execute 

```bash
docker run kczulko/intel-a-star:0.1 9.txt
```

Sample files from the task definition tarball were packaged to docker container so
it is possible to choose one of them as the last argument (as it was done in the example).

### sbt from bash console

1. Install [sbt](https://www.scala-sbt.org)
1. Go to project's root directory.
1. Execute `sbt 'run ./src/universal/10.txt'` to run sample no. 10


## Build

```bash
sbt clean compile
``` 

## Tests

```aidl
sbt test
```