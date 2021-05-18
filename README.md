# FileZipper

FileZipper is a service that compresses a list of files into a single zip file.

## Assumptions
- The same service endpoint that is called by the user returns the file. An alternative would be to store it on the upload and have a separate download endpoint, but that didn't seem to be what was asked.

## Potential Improvements

- I feel bad that I didn't write tests for `FileZipperService`, but since it's mostly IO operations and this is just an exercise, I cut a corner here. I'd usually validate stuff like temp folder exists, can create files, mandatory fields are set, if I unzip the result, do I get the same content I've added to the zipfile. Might have led me to redesign a bit the class too.
- Could make the `saveWithName` parameter optional and create a unique default name where undefined.
- The exception handling in `FileZipperService` could be stronger. 
- The status code when an error occurs doesn't feel right. `Not Found` in the sense that it can't find the zip file, probably should return different codes depending on different errors.
- In a real world scenario, might be worth storing the zip file in some sort of external storage (S3 or equivalent).
- Temp folder to save files could be configured in `application.properties`.
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


# Software development process

I never really thought about the SaaS development lifecycle in formal terms, but I'll have a go.

1. Evaluate Business Requirements
2. Design and Architecture
3. Story Planning and Grooming
4. Development (and Testing)
5. Provisioning and Deployment
6. Monitoring, Alerting, and Support

Repeat. Repeat. Repeat.

Step 6 might feed work to earlier steps, and I get a feeling I should bring some of the step 5 activities to a bit earlier, but overall, this should represent it close enough.

## Evaluate Business Requirements

Keeping this one very generic.

Someone at business level decides what the long term goals are and communicates it to the product team, which starts breaking it down to workable chunks of work and prioritizing.

## Design and Architecture

Activities:
- Design the several components that make up the system and how they interact with each other.
- Evaluate technology choices in order to solve the problems at hand.
- Likely some prototyping.
- Define SLAs.
- Evaluate costs of material and man-hours and consider it in the technology decisions.

Techs used:
- White board and Diagram Tools (eg: Miro, LucidCharts, Visio)
- Documentation tools (Confluence, Google Docs)
- Any techs under evaluations

## Story Planning and Grooming

Activities:
- Translate business requirements to stories with specific acceptance criteria.
- Clear up unclear requirements and make sure the team understands the goal.
- Estimate effort for individual stories.
- Plan short term work for the team (sprint).  
- Some teams may define test plans at this stage.
- Spiking unclear stories to get a better idea of the effort.

Techs Used:
- Agile Board / Planning Tools (Jira, Trello)
- Documentation tools (Confluence, Google Docs)

## Development (and testing)

Activities:
- Write the code to implement the functionality.
- Write automated tests (unit, functional, contract, e2e, etc) to validate acceptance criteria.
- Code review by peers.
- Write any documentation necessary.
- Participate in all agile ceremonies.
- Do any additional testing in integration environments.

Techs used:
- An IDE, a compiler/interpreter for used languages, a runtime, and build tools.
- Test frameworks
- Git for code versioning
- Github / Bitbucket / etc
- OpenApi / Swagger (for API documentation)
- Markdown (for general documentation)
- Continuous Integration / Delivery Tools (CircleCI, Jenkins, etc)

## Provisioning and Deployment

Activities include:
- Creation of infrastructure needed to support the several environments (if not in place already) or spin up new instances.
- Build release artifact for deployment and push it to a repository.
- Deploy artifact in all the required instances.

Techs used:
- Continuous Integration / Delivery Tools (CircleCI, Jenkins, etc)
- Artifact Repository (Nexus, GitRelease, DockerHub)
- In-house Infrastructure or Cloud Platform (AWS, GCP, Azure)
- Container Orchestrator (Kubernetes, Mesos, etc) + Engine (Docker)
- Infrastructure as Code Tool (Terraform, Ansible)
- Secret management (Vault)

## Monitoring, Alerting, and Support

Activities include:
- Creation of alerts when certain metrics go under/over a certain threshold.
- Creation of visualization dashboards that provide a quick-glance view of the systems health.
- Maintain SLAs and availability of the system.
- Manage infrastructure to meet SLAs.

Techs used:
- Application Monitoring Tool (New Relic, AppDynamics)
- Distributed logging (ELK stack)
- Metrics and Alerts (Grafana, Prometheus)  

