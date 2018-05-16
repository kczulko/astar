# A* algorithm

## Description

This repository contains A* algorithm implementation in Scala.

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

## Sample files

Sample files used to execute an algorithm are located under `src/universal` directory.

## Build

```bash
$ sbt clean compile
``` 

## Docker package

1. Run `sbt docker:publishLocal`
2. Tag recently created image, e.g. `docker tag intel-astar:0.1 <username>/intel-astar:0.1`
3. Publish tagged image to docker hub, e.g. 
```
$ docker login <username>
$ docker push <username>/intel-a-star:0.1
```

## Tests

```bash
$ sbt test
```
