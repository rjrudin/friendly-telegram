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

