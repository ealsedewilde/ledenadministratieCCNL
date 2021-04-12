# ledenadministratieCCNL
JAVA-FX application for membership administration of Citroën Club Nederland.

# Purpose of this application.
As a 'regular' Java-developer you normally come across Java-FX. 
But now that I'm retired I have time to look at this intriguing technology.
I wanted to explore a full blown application, not just the getting started.

Exploring a technology is easier when there is a well understood use-case. 
For me this membership administration is such a use-case. I also had some code working on MS-Excel sheets.
The idea was to enhance this starting point to a real desktop application.

# Design
## Spring Boot
Over the years Spring Boot has been the natural starting point for structuring to an application. After checking some websites it turned out that combining Spring Boot and Java-FX is quite easy. Class `SpringJavaFXBase` contains the extendable base structure to accomplish the integration of Spring Boot and Java-FX.

```
@SpringBootApplication
public class MySpringBootApplication {

  public static void main(String[] args) {
    Application.launch(JavaFXApplication.class, args);
  }
}
```

```
public class JavaFXApplication extends SpringJavaFXBase {

  public JavaFXApplication() {
    super(MySpringBootApplication.class);
  }

}
```

Spring Boot has one big disadvantage: the startup takes some time. 
I've chosen for lazy `lazy-initialization` is helps a bit.

## Database
I've made it easy on myself and have just chosen an H2-database. This also has the disadvantage that startup takes time even with `BootstrapMode.LAZY`.

## Startup 
I build this application as a single jar via the 'repackage' goal of the spring-boot-maven-plugin.

As said before the startup takes time, so the user needs to know that something is happening when starting the application. I have taken the easiest solution with a 'SplashScreen-Image' in the manifest of the application jar. 

## FXML
I've chosen for FXML because I had no experience of building screens. In such a situation the SceneBuilder is handy to roughly model the screens. I also had the idea that FXML somehow will give me a ModelViewController like solution. I now think that the View (FXML) is too closely coupled with the Java-FX controller to have a true MVC-paradigm.

