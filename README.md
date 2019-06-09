# WhaToSpotify

A Java application to convert a Whatsapp chat to a curated spotify playlist with all songs sended over the chat to your most loved ones.

## Install

For installing from latest jar, just open a new Java instance and load the jar File provided on [Releases](https://github.com/santiboub/WhatSpotify/releases) with the dependencies and Fullfill the configuration file with the values needed for the use of the application.

```bash
java -jar whatSpot-0.5.0-Alpha.jar -input chat.txt -playlist 09FQMYeOKyHUam8x6kxU75
```

With a working `config.txt`:

```bash
java -jar whatSpot-0.5.0-Alpha.jar
```

- PlaylistID : ID of the playlist obtained by means of sharing, for example: `09FQMYeOKyHUam8x6kxU75`
- Input File: File from whatsapp conversation with the exported chat in `.txt UTF-8` formatting.

## Config File 

Example of config.txt file: 

```txt
Whatsapp file: test.txt
PLaylist id to add: 4fZ7COxJog8mKg4ARohEbA
```
The file will be generated at first call when providing the method with the inline options `-input ` and `-playlist`, saving also a refresh token to avoid the recurrence login in case of multiple playlist being created.

## Obtain Chats

For obtaining the WhatsApp chat you must go to the app and on profile of current chat click on Export > Export without attached files.

You'll need to send the compressed file to your computer and locate it in the same folder as the application.

## Building from Source

The app was developed using Eclipse IDE for java and the latest version of Maven compiler and assembler, as stated in the `pom.xml` file of the project. As a result, for compiling a new version of the app using Maven you must update the POM file regarding the new changes and place through the maven cycle of developing, the package target will generate a new jar file including sources which can be later used for production.

Navigate to the folder where `pom.xml` is:

```bash
mvn compile
mvn package
```

## About

This app was integrately developed by [santiboub](https://github.com/santiboub) for making possible the easy creation of playlists based on songs you have shared over WhatsApp with one person over time, creating a soundtrack of all moments together.

## Used Maven Modules

* [Spotify-web-api for Java](https://github.com/thelinmichael/spotify-web-api-java) by [thelinmichael](https://github.com/thelinmichael)
* [Selenium for Java](https://www.seleniumhq.org/download/maven.jsp) by [SeleniumHQ](https://www.seleniumhq.org)
* [WebDriverManager](https://github.com/bonigarcia/webdrivermanager) by [Boni Garcia](https://github.com/bonigarcia)
