# comp3100-project
Repository for COMP3100 Project

|Student Name|Student ID|GitHub Profile|
|------------|----------|---------------|
|Jack Davenport|45946396|[mq45946396](https://github.com/mq45946396)|
|Lucas Turnbull|45947155|[EccentricTortoise](https://github.com/EccentricTortoise)|
|Elliot Holt|45955964|[AuzEll](https://github.com/AuzEll)|

## Helpful Links
[Stage 1 specification (via iLearn)](https://ilearn.mq.edu.au/mod/resource/view.php?id=6384214)

[ds-sim User Guide and Protocol](https://github.com/distsys-MQ/ds-sim/blob/master/docs/ds-sim_user-guide.pdf)

[Download ds-server executable](https://github.com/distsys-MQ/ds-sim/blob/master/src/pre-compiled/ds-server)

## Getting Started

```sh
$ git clone https://github.com/mq45946396/comp3100-project.git
$ cd comp3100-project
$ ./compile.sh # compile all Java classes
$ ds-server    # start a ds-server instance
$ ./run.sh     # run the client
```

Command line usage
```
Usage: java [..vm] comp3100.Client [-n|-v|-h]

        -n : use newline mode
        -v : use verbose mode (print when job is scheduled)
        -h : show this help message
```

To remove built class files from the project:
```sh
$ make clean
```

To run the automated test files (stage 1):
```sh
$ ./compile.sh
$ cd tests-s1
$ ./run-test.sh
```
