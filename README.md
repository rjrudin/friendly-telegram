Prototype of an ML tool on Spring Batch.

To test this out, first deploy a simple app that you can run tests against (the config for this app is at 
./mlcp-core/src/test/ml-config; it's a vanilla REST API app with user/role for running tests):

    ./gradlew -i mlDeploy

You can then run all of the tests to verify that the test app was deployed and that the tests are all passing:

    ./gradlew test

It's usually more helpful though to run a test individually via an IDE like Intellij so that you can e.g. use a debugger
to see exactly what is going on.

What you're probably more interested in though is building an mlcp-like application and trying that out. To do that, 
first build the entire project (which consists of several Gradle subprojects, which are documented below); "-x test" 
is used to skip running the tests, though you're welcome to run them too:

    ./gradlew clean build -x test

This will create a tar file at ./mlcp-cli/build/distributions/mlcp2.tar; you can extract that to test it out:

    tar xvf mlcp-cli/build/distributions/mlcp2.tar

The extracted tar will be in the gitignored "mlcp2" directory. You can run the mlcp2 application without any args to 
get the built-in help (courtesy of the [JCommander CLI library](https://jcommander.org/)) :

    ./mlcp2/bin/mlcp2 

That will list each of the commands supported by this prototype - e.g.

```
Usage: java -jar <name of jar> [command] [command options]
  Commands:
    custom      Allows for a custom Configuration class to be used
      Usage: custom [options]
      (... details omitted ...)
      
    ingestFiles     
      Usage: ingestFiles [options]
      (... details omitted ...)

    ingestRows      
      Usage: ingestRows [options]
      (... details omitted ...)
```

To try ingesting files, run the following:

```
    ./mlcp2/bin/mlcp2 ingestFiles --host localhost --port 8003 --username mlcp2-test-user --password password 
      --input_file_path "data/csv/**/*.csv"
```

You can also ingest rows from a small [H2](https://h2database.com/html/main.html) database; note that this requires 
specifying the location of a JDBC driver jar, which will then be dynamically loaded from the filesystem:

```
    ./mlcp2/bin/mlcp2 ingestRows --host localhost --port 8003 --username mlcp2-test-user --password password 
      --jdbc_driver org.h2.Driver --jdbc_url "jdbc:h2:file:./data/h2/h2-sample-db" --jdbc_username sa --sql "SELECT * FROM CUSTOMER" 
      --jdbc_driver_path "data/h2/h2-1.4.193.jar"
```
