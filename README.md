# RSocket CLI

## Description

Simple RSocket CLI currently for two main purposes

1. Early testing of new protocol server implementations e.g. websocket
2. Sending some basic traffic to servers built using RSocket e.g. help debug a mobile <=> server integration issue. 

Supports tcp and ws URIs

# Build Status

<a href='https://travis-ci.org/rsocket/rsocket-cli/builds'><img src='https://travis-ci.org/rsocket/rsocket-cli.svg?branch=master'></a> 


## Build and Run

To build the RSocket CLI:
```
./gradlew --console plain installDist
```

To run:
```
./build/install/rsocket-cli/bin/rsocket-cli --help
```

The build and run:
```
$ ./rsocket-cli --help
```


## Install via Homebrew

Use tab completion for help with specifying the operation type.

```
$ brew install yschimke/tap/rsocket-cli
```

## Examples


A request-response interaction:
```
$ rsocket-cli -i "I am a Server" --server --debug tcp://localhost:8765       # window 1
$ rsocket-cli --request -i "I am a Client" --debug tcp://localhost:8765      # window 2
```

A request stream of dictionary words, with frames debugged:

```
$ rsocket-cli --debug -i=@/usr/share/dict/words --server tcp://localhost:8765     # window 1
$ rsocket-cli --stream -i "Word Up" tcp://localhost:8765                          # window 2
```

