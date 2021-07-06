# ledenadministratieCCNL
JAVA-FX application for membership administration of Citroën Club Nederland.

# Purpose of this application.
As a 'regular' Java-developer you normally don't come across Java-FX. 
But now that I'm retired I have time to look at this intriguing technology.
There are a lot of examples on the internet but mostly not beyond the HelloWorld level.
I wanted to explore Java-FX in a full size application, so with dozens of screens and interacting with a real business layer.

Exploring a technology is easier when there is a well understood use-case. 
For me this membership administration is such a use-case. I also had some code working on MS-Excel sheets.
The idea was to enhance this starting point to a real desktop application.

Aside of developing the application, I also wanted to look at an efficient deployment of the application, thus looking at JPMS.

# Design
## Spring Boot
I've designed quite some applications
Over the years Spring Boot has been the natural starting point for structuring an application. After checking some websites it turned out that combining Spring Boot and Java-FX is quite easy. My Class `SpringJavaFXBase` contains the extendable base structure to accomplish the integration of Spring Boot and Java-FX.

```
@SpringBootApplication
public class MySpringBootApplication {

  public static void main(String[] args) {
    Application.launch(MyJavaFXApplication.class, args);
  }
}
```

```
public class MyJavaFXApplication extends SpringJavaFXBase {

  public JavaFXApplication() {
    super(MySpringBootApplication.class);
  }

}
```

Spring Boot has one big disadvantage: the startup takes some time. Starting the application in my IDE took about 9 seconds.
I've chosen for lazy `lazy-initialization`. This helps gaining a few seconds.

## Database
I've made it easy on myself and have just chosen an embedded H2-database. Embedding the database also has the disadvantage that startup takes time. I've chosen `BootstrapMode.LAZY` to reduce the time to initialize SpringBoot JPA. Starting the application in my IDE was reduced to about 5 seconds.

The most challenging issue was a way to backup and restore the database while the application kept running. Backup is done via `SCRIPT SIMPLE NOPASSWORDS DROP TO '%s' COMPRESSION ZIP`. The restore is executing all the native sql statements of the backup one by one.

## Startup 
I initially have built this application as a single jar via the 'repackage' goal of the spring-boot-maven-plugin. (I changed that afterwards for a more efficient variant.)

As said before the startup takes time, so the user needs to know that something is happening when starting the application. I have taken the easiest solution with a 'SplashScreen-Image' in the manifest of the application jar. 

## FXML
I've chosen for FXML because I had no experience of building screens. In such a situation the SceneBuilder is handy to roughly model the screens. I also had the idea that FXML somehow will give me a ModelViewController like solution. I now think that the View (FXML) is too closely coupled with the Java-FX controller to have a true MVC-paradigm. 

I need a mechanism to control the flow between the dozens of fxml files. I have defined the `PageName` enum that points to the fxml file that it refers to. I noticed that loading fxml is a quite heavy task, so once loaded I cache the fxml; in the `FXMLNodeMap`. Caching means that the initialization of controllers only happens once. When reusing a screen it has to be reset explicitly. (I'm used to it, but I'm not sure if it is the best option. Later i came across the FxWeaver framework. I've looked at it, but found it not flexible enough to suit my needs, so I sticked to the mechanism I had.

## Controlling the UI.
Being able to identify an fxml pages via the PageName enum is a start, but not the whole story of controlling the UI.
My design is that my base screen (main.fxml) is a Vbox with the included menu.fxml and a BorderPane. In the TOP section of the BorderPane I show a generic header. The CENTER initially contains a placeholder fxml. Based on menu choices various dialogs of pages are loaded one by one in the CENTER of the BorderPane. The BOTTOM section is used for displaying messages. 

It is the `PageController` that handles the loading of pages and showing the correct page in the CENTER of the BorderPane of the main.fxml. The PageController also controls the display of messages in the BOTTOM section.

It is the `MenuController` that handles menu choices. When applicable it sends information to the PageController on what page to show (and when applicable on what page to preload.)
The MenuController then has to activate the applicable controller. This can be every controller in the application.
I use eventing to decouple the MenuController from all other controllers. I use Spring's ApplicationContext.publishEvent(ApplicationEvent event) for this. 

Eventing works as long as the controller Spring component is loaded. Since I have chosen lazy loading of Spring components this is a point of attention. When a controller is attached to a fxml then loading the fxml will instantiate the Spring Controller attached to it.
But there are also menu choices that just execute a command. Explicit Eager instantiation is needed otherwise it can't respond on an event. Autowiring can't be used in eager loaded controllers otherwise eager loading would cascade to other Spring components. So I use `applicationContext.getBean(Bean.class)` instead.

The application uses forms that span multiple pages. (Registering all properties for a new member is spread across four pages.) All the pages of a form are linked to the same controller. This has the advantage that the form is controlled as a whole, but the disadvantage is that the initialization of the controller is per page.

## MVC data mapping.
When using forms data has to be mapped between the model (JPA entities) and the form (Java-FX controls in the controller). I've built a generic extensible framework that can do this mapping based on name equality of properties in both the model and the form; see the `DataMapper` class. I rely on Java bean introspection to determine the mapping. The DataMapper framework works quite well as long as the controller is structured carefully. (For example the controller will have a reference to the model. If the bean introspection picks the model reference as part of the form, then it will try to map the model to the model and the data mapping will fail. Carefully defining getters and setter in combination with the use of the `@Mapping` annotation should be used.)

## Java-FX concurrency
My business layer is behind a number of Spring services. Some of these services provide batch functionality. Such a batch function only take a few seconds at most, so no need for progress bars. However at the begin of a batch process I want to display a 'batch started message' and at the end a message of either 'successful' or 'error'. When I try to run the batch process in the FX-application thread then the start message is never shown. When I use Java-FX concurrency to asynchronously run the batch process in another thread then the start message is displayed. I use the Spring `TaskExecutor` to provide the threads for the asynchronous `javafx.concurrent.Task`. 

## A PDF-viewer
My application needs a way to show multi page PDF's. To my surprise there is no JavaFX framework for that. It is suggested to use pdf.js of Firefox in the webclient. That worked in my IDE, but from within a jar the whole JVM crashes without any notion why. Also pdf.js is extremely slow. I then came across IcePDF. It consists of a core component that renders PDF-pages and a Swing viewer for it. I use a `org.icepdf.core.pobjects.Document` of the IcePDF core component to render a `java.awt.image.BufferedImage` per PDF-page.
With `SwingFXUtils.toFXImage(bufferedImage, null)` I convert it to a `javafx.scene.image.Image`. That image is than used in a simple PDFViewer that I wrote. This approach delivers fine looking PDF's with an excellent performance.

## Printing
JavaFx has a way of printing Nodes. I've tried it for the PDF's an the result was very, very poor. I then switched to AWT printing and that works excellent, but requires the application to run with headless=false. By default SpringBoot runs with headless=true.  I had to change that in the `SpringApplicationBuilder` when building the ApplicationContext.

## Business Layer
For me the business layer is not very special. It uses techniques that a backend Java developer is familiar with. But still it has points that were fun building:
- Generating a Direct Debit file for the bank for the yearly membership fee; (partly reusing older code)
- Playing with Excel in a structured way; (partly reusing older code)
- Matching payments in a bank statement with a specific member. With the more restrictive AVG privacy regulations it is harder than before because the address of the person that made the payment is no longer supplied. 
- Generating PDF-documents based on templates. 

## Unit testing Java-FX
Java-fx requires the launch of the toolset and a special FX-application thread to run its functionality. That can easily conflict with the way the maven-surefire-plugin uses forking for its tests. 
Initially I had serious performance problems running my Java-FX unit tests. Then I set the forkCount of the maven-sure-fire plugin to 1 and I load the Java-FX toolset only once for all my tests. My test run asynchronously in a `Platform.runLater(runnable)`.
I use `Awaitility` so the thread of the maven-surefire-plugin will wait for the Java-FX runnable to finish. Well what can I say? It works and has a good performance. I have looked at TestFX , but I haven't used it because I think it doesn't add much to my unit tests.

# What about Java modularity?
The classpath of application consists of about 100 jars. Some are modules, some have a `Automatic-Module-Name` in their manifest and some are just pre JPMS jars.
I've tried putting a `module-info.java` in every Maven module of the project. The result was a lot of errors. For example HikariCP version 3.4.5 refers to the non existing module `hibernate.core`; (I know this is fixed in HikariCP version 4.0.3.)
Also there are java packages that exist in multiple jars. Specially the messy jar structure of `org.apache.xmlgraphics:fop` causes challenges. (I have been using Apache Fop for 20 years and it's structure has always been a mess.)

In the end I managed to get my modules error free and I got the application running fully functionalin my IDE. However I was unable to run the `@SpringBootTest` annotated unit tests. This was because the SpringBoot component scan was incomplete and I was unable to fix it.

My modular application was still relying on several non modular jars. This means that I still was unable to run JLINK without tricks. So I reverted Java modularity, but preserving the cleaned classpath.

# Deployment
My goal is to run my application with a minimal JRE. Via  trail and error I was able to determine the jmods needed in this minimal JRE.

I want to package my application in a single jar which includes all dependencies.
There are several strategies to construct such a jar:
- Via the maven-assembly-plugin (jar-with-dependencies)
- Via the spring-boot-maven-plugin (repackage)
- Via the maven-shade-plugin

I've tried all three. 
The maven-assembly-plugin fails to build an executable jar, because (at least) the `spring.factories` resource is incorrect.
The spring-boot-maven-plugin builds an executable jar, but with considerable overhead which makes the jar relatively large and slow compared to the jar generated by the maven-shade-plugin. Furthermore the BOOT-INF structure makes it harder to load resource. Especially org.apache.xmlgraphics:fop had troubles with it.
So the maven-shade-plugin is my choice for building the executable jar. Starting the application takes 3 seconds. (Quite good compared to the initial 9 seconds.)

So now I have a minimal JRE and an executable application jar. How to ship this? (Windows is the target platform for my Java-FX application.)
I've installed WIX-tools on my PC and tried JPACKAGE. It generates an exe which is able to install my application in the Program Files.
However I want to be able to configure the location of the embedded database before the first start of the application. I'm unable to get that done via JPACKAGE. (I'm sure it is feasible, but I could not find out how.)

The alternative is to produce a zip-file. That is what I've done via the maven-assembly-plugin. 

I want to start my application as an EXE. I used Launch4J to create such a single instance EXE without wrapping the jar and with a relative path to the minimal JRE.

This assembled ZIP will contain:
- the application jar 
- the minimal JRE
- The EXE created via Launch4J
- `db.properties` file with the configurable database location




