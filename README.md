# Jpg Png Converter
Master:
[![Tests](https://github.com/WorryWarrior/JpgPngConverter/actions/workflows/gradle-tests.yml/badge.svg?branch=master)](https://github.com/WorryWarrior/JpgPngConverter/actions/workflows/gradle-tests.yml) 

Develop:
[![Tests](https://github.com/WorryWarrior/JpgPngConverter/actions/workflows/gradle-tests.yml/badge.svg?branch=develop)](https://github.com/WorryWarrior/JpgPngConverter/actions/workflows/gradle-tests.yml) 

A converter, which takes a number of images, converts `.jpg` files into `.png` and vice versa via HTTP POST request and responds with a number of converted images.

## How to use

Launch at
``` 
0.0.0.0:8081
```
Attach a number of `jpg` or `png` files. All files are recognized automatically.
Actual conversion happens at
``` 
0.0.0.0:8081/convert
```
But since it is impossible to supply a file in any way other than to click 'Attach' button, users cannot access it directly.

## How to run with docker
Clone repository:
``` console
$ git clone https://github.com/WorryWarrior/JpgPngConverter.git
```
CD into app folder:
``` console
$ cd converter
```
Build docker image:
``` console
$ docker build -t JpgPngConverter
```
Run docker image:
``` console
$ docker run -p 8081:8081 JpgPngConverter
```

## Lisense
[Apache ](./LICENSE) licensed.
=======