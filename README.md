# Project Title

General purpose utilities that deploy into maven archive or can be used in
AWS Lambda when packaged

## Getting Started

Clone the code, update pom file for your s3 maven repo, update your settings
the prerequisites section.

$ mvn deploy sends it up to s3 utilizing
    maven extension https://github.com/jcaddel/maven-s3-wagon
$ mvn package will package the jar into your class folder
    that can be utilized for AWS Lambda;
    See src/main/kotlin/cloud/LambdaHandler for example;
    Logging is also done to cloud watch; ensure that your
    permissions are set as well


### Prerequisites

Requires that AWS credentials be set in order to deploy to maven repository
~/.m2/settings.xml;  ensure that credentials have policy associated with
then in order to write to your bucket.

```
    <servers>
        <server>
            <id>dqn.maven</id>
            <username>secretAccessId</username>
            <password>secretAccessKey</password>
        </server>
        <server>
            <id>dqn.maven.snapshot</id>
            <username>secretAccessId</username>
            <password>secretAccessKey</password>
        </server>
        <server>
            <id>dqn.maven.release</id>
            <username>secretAccessId</username>
            <password>secretAccessKey</password>
        </server>
    </servers>
```

### Installing

Not really an installable; but deploys to private s3 maven repo to be utilized
by author in other projects

## Running the tests

Should be run in build cycle when packaging or deploying jar to AWS s3 repo

### Break down into end to end tests

?

### And coding style tests

Testing is in test folder utilizing junit

## Deployment

Utilizes com.github.edwgiz for packaging log4j to be utilized for AWS Lambda

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

n/a

## Versioning

none yet

## Authors

* Dan Quoc Nguyen

## License

No licences for now; TODO: pick out a license
*https://help.github.com/articles/licensing-a-repository/*

## Acknowledgments

* README.md formatting from: https://gist.github.com/PurpleBooth/109311bb0361f32d87a2
