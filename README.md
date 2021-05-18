# FileZipper

FileZipper is a service that compresses a list of files into a single zip file.

## Assumptions
- The same service endpoint that is called by the user returns the file. An alternative would be to store it on the upload and have a separate download endpoint, but that didn't seem to be what was asked.

## Potential Improvements

- Could make the `saveWithName` parameter optional and create a unique default name where undefined.
- The exception handling in `FileZipperService` could be stronger. 
- The status code when an error occurs doesn't feel right. `Not Found` in the sense that it can't find the zip file, probably should return different codes depending on different errors.
- In a real world scenario, might be worth storing the zip file in some sort of external storage (S3 or equivalent).
- Temp folder to save files could be configured in `application.properties`.
- Get some tests written for `FileZipperService`, validate stuff like temp folder exists, can create files, mandatory fields are set, if I unzip the result, do I get the same content I've added to the zipfile.
- Size of the uploaded files is fairly small, but could be increased.
- Service could be made asynchronous, especially if we increase size of files and payload we can add.

## Building

From the project root, generate the jar:

```./gradlew build```

## Running tests

From the project root:

```./gradlew test```

## Running it

From the project root, run 

```java -jar build/libs/FileZipper-1.0-SNAPSHOT.jar```

## Running in Docker

This requires the project to have been built, and the jar file to exist.

### Creating image

From the project root (where the Dockerfile is located), run:

```docker build --build-arg JAR_FILE=build/libs/\*.jar -t filezipper .```

### Start container

```docker run -p 8080:8080 filezipper```

## Trying it out

### Curl

```
curl --location --request POST 'http://localhost:8080/files/zipped' \
--form 'saveWithName="myfile.zip"' \
--form 'files=@"/D:/Stuff/American Typewriter Bold.ttf"' \
--form 'files=@"/D:/Stuff/Test.pkg"' \
--form 'files=@"/D:/Stuff/Installer_1.4.3.exe"'
```

### Postman

![postman_sample.png](postman_sample.png)
