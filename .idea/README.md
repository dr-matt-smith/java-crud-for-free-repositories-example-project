1. install dependences after pasting them into `pom.xml`

mvn clean dependency:copy-dependencies package

2. execute the biuld app

mvn clean compile exec:java

+ 
need to add this to properties in pox.xml - use fully qualified class name

  <properties>
      <exec.mainClass>com.mycompany.app.App</exec.mainClass>
  </properties>


