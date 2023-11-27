# Struggling with Java


## Prerequisites
> - https://docs.docker.com/desktop/dev-environments/create-dev-env/#prerequisites

You need:
- Docker Desktop
- Git
- VSCode
- [Visual Studio Code Remote Containers Extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)

### Getting started

First, clone this project.

**👋 you need to specify the architecture of the host machine**: if needed, change the value of the `WORKSPACE_ARCH` variable in this file: `compose-dev.yaml` (for example, if you work on a Macbook Intel, use `amd64`, on a Macbook M1, use `arm64` - it's the same with Linux - not yet tested on Windows)

Then:
1. Open Docker Desktop
2. Go to the **Dev Environments** option menu
3. Click on the <kbd>Create</kbd> button, then on the <kbd>Get Started</kbd> button
4. Choose **Local directory** as the source
5. Select the directory of this cloned repository
6. Click on the <kbd>Continue</kbd> button, and wait for a moment
7. Once the build finished, Click on the <kbd>Continue</kbd> button
8. 🎉 and now, you can open your new Dev Environment in **VSCode**

Or you can test it like this: [🌍 Open the ARM version of this Dev Environment directly from GitLab](https://open.docker.com/dashboard/dev-envs?url=https://gitlab.com/k33g-twitch/s02e01-wasi-intro/tree/main)

## Setup the project

### Get and build Chicory

```bash
git clone https://github.com/dylibso/chicory
cd chicory
mvn clean install
```

### Create a Java application

#### Generate a project

```bash
mvn archetype:generate -DgroupId=garden.bots.app -DartifactId=hello -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

#### Download the wasm file

```bash
cd hello
curl https://raw.githubusercontent.com/dylibso/chicory/main/runtime/src/test/resources/wasm/iterfact.wat.wasm > factorial.wasm
```

#### Update pom.xml

```xml
<dependency>
  <groupId>com.dylibso.chicory</groupId>
  <artifactId>runtime</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

and:

```xml
<build>  
    <plugins>  
        <plugin>  
            <!-- Build an executable JAR -->  
            <groupId>org.apache.maven.plugins</groupId>  
            <artifactId>maven-jar-plugin</artifactId>  
            <version>3.1.0</version>  
            <configuration>  
                <archive>  
                    <manifest>  
                        <mainClass>garden.bots.app.App</mainClass>  
                    </manifest>  
                </archive>  
            </configuration>  
        </plugin>  
    </plugins>  
</build> 
```

#### Update App.java

```java
package garden.bots.app;

import com.dylibso.chicory.wasm.types.Value;

import java.io.File;

import com.dylibso.chicory.runtime.*;
import com.dylibso.chicory.runtime.Module;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        File wasmFile = new File("../factorial.wasm");
        Module module = Module.build(wasmFile);
        Instance instance = module.instantiate();

        ExportFunction iterFact = instance.getExport("iterFact");
        Value result = iterFact.apply(Value.i32(5))[0];
        
        System.out.println(result.asInt());

    }
}
```

#### Build and run

> this does not work
```bash
mvn package
java -jar target/hello-1.0-SNAPSHOT.jar 
```

> this works
```bash
mvn compile exec:java -Dexec.mainClass="garden.bots.app.App"
```


