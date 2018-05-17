# A* algorithm

table of contents
=================
* [description](#description)
  * [external dependencies](#external-dependencies)
  * [questions & answers](#questions-&-answers)
* [HOWTO run the algorithm](#howto-run-the-algorithm)
  * [docker](#docker)
  * [Sbt](#sbt)
* [sample files](#sample-files)
* [packaging docker image](#docker-packaging)
* [build](#build)
* [tests](#tests)

description
===========

This repository contains A* algorithm implementation in Scala which is my
resolution for the given recruitment task. A* algorithm implementation follows
[this pseudocode](https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode).

The program is written in pure functional style
(except of console io effect control in main function...).
Program is [referentially transparent](https://en.wikipedia.org/wiki/Referential_transparency)
and this was satisfied because of:
* immutability - no var or setters
* pure functions - for the given input they return always the same output
* recursion | `@tailrecursion` - thanks to that algorithms/functions are strict,
safe and self explaining

## remark!

Diagonal moves within the maze (next to horizontal|vertical ones) are acceptable since
such constraint wasn't pointed out within the task definition. However,
additional docker image `kczulko/intel-a-star:0.2` was released which satisfies
such constraint of vertical|horizontal moves only.
Though it was not tested, it seems to work really well.

external dependencies
---------------------

Project contains two external dependencies and one plugin used for docker packaging.

External dependencies:

1. [Scalaz](https://github.com/scalaz/scalaz) -
    *An extension to the core Scala library for functional programming.* Across this
    project were used following structures from Scalaz library:
    * State monad - more information can be found [here](http://timperrett.com/2013/11/25/understanding-state-monad/)
    * ListT monad transformer - more information about MT can be found [here](http://eed3si9n.com/learning-scalaz/Monad+transformers.html)
    * sequenceS | sequenceU - in general it allows to change the type structure of `F[G[A]]` into `G[F[A]]`
    * [Disjunction (`-\/` or `\/-`)](http://appliedscala.com/blog/2016/scalaz-disjunctions/)
1. [scalatest](http://www.scalatest.org/) - testing library for Scala

questions & answers
-------------------

1) How to build and run your code.

   Answers are [here](#build) and [here](#howto-run-the-algorithm)

2) The heuristic that you used.

   [Euclidean distance](https://en.wikipedia.org/wiki/Euclidean_distance) was used as heuristic function.
   Within the code it is implemented at [`Cell::distanceTo`](./src/main/scala/com/github/kczulko/a/star/model/Cell.scala) function.

3) What you used for tie-breakers when you had two nodes in your priority queue with the same priority.

   Priority queue wasn't used in this algorithm. `TraversableOnce[A+]::minBy` function on `Set` structure was used instead.

4) What are the advantages of having a more sophisticated heuristic?  Are there any disadvantages?

5) How do you know that a heuristic is admissable?  How do you know that a heuristic is monotonic?

   Heuristic is admissable when following equation is satisfied (heuristic satisfies consistency rule):

   h(x) <= d(x,y) + h(y)

   , where:
     * h(x)   - heuristic value for point `x`
     * h(y)   - heuristic value for point `y`
     * d(x,y) - real distance between `x` and `y`

   Since Euclidean distance was used as heuristic, this equation in fact satisfies
   [triangle inequality](https://en.wikipedia.org/wiki/Triangle_inequality)
   between `x`, `y` and `goal`.

6) Does the way you break ties matter?

   It doesn't.

howto run the algorithm
=======================

docker
------

There are prepared two versions of the docker image:
1. `0.1` - supports moves for both diagonal and vertical|horizontal directions
1. `0.2` - supports moves only for vertical|horizontal direction (it has not been tested)

In order to run algorithm against provided samples, please execute:

```bash
$ docker run kczulko/intel-a-star:0.1 9.txt
```

Sample files from the task definition tarball were packaged to docker container so
it is possible to choose one of them as the last argument (as it was done in the example).

sbt
---

1. Install [sbt](https://www.scala-sbt.org)
1. Go to project's root directory.
1. Execute `sbt 'run ./src/universal/10.txt'` to run sample no. 10

sample files
============

Sample files used to execute an algorithm are located under `src/universal` directory.

build
=====

```bash
$ sbt clean compile
``` 

docker packaging
================

1. Run `sbt docker:publishLocal`
2. Tag recently created image, e.g. `docker tag intel-a-star:0.1 <username>/intel-a-star:0.1`
3. Publish tagged image to docker hub, e.g. 
```
$ docker login <username>
$ docker push <username>/intel-a-star:0.1
```

tests
=====

```bash
$ sbt test
```
