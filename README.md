[![Build Status](https://travis-ci.org/Giulianini/Jestures.svg?branch=master)](https://travis-ci.org/Giulianini/Jestures)
[![PyPI - License](https://img.shields.io/github/license/Giulianini/Jestures.svg)](https://github.com/Giulianini/Jestures/blob/master/LICENSE.txt)
[![Bintray](https://img.shields.io/bintray/v/giulianini/maven/Jestures.svg)](https://bintray.com/giulianini/maven/Jestures/_latestVersion)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.giulianini.jestures/Jestures.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A"com.github.giulianini.track4j"%20AND%20a%3A"track4j")
![GitHub issues](https://img.shields.io/github/issues/Giulianini/Jestures.svg)
![GitHub last commit](https://img.shields.io/github/last-commit/Giulianini/Jestures.svg)
![Maintenance](https://img.shields.io/maintenance/yes/2018.svg)
![GitHub repo size in bytes](https://img.shields.io/github/repo-size/Giulianini/Jestures.svg)
---

# Track4J

## A simple framework for gesture recognition in Java.

<h1 align="center">
    <img src="/pic/Track4J.png">
</h1>


## Download
* Released builds are available from - [Bintray](https://bintray.com/giulianini/maven/Track4J/_latestVersion)
* Released builds are available from - [Maven Central](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A"com.github.giulianini.track4j"%20AND%20a%3A"track4j")
* Snapshot builds are available from - [Artifactory](http://oss.jfrog.org/oss-snapshot-local/com/github/giulianini/track4j/track4j/0.1.6-SNAPSHOT/)

## Javadocs 
If you need to access the documentation for any stable version, [javadoc.io](https://www.javadoc.io/doc/com.github.giulianini.track4j/track4j/) is probably the right place to search in.

## Demo
Run the demo with:

    gradlew run

## Build
To build JFoenix, execute the following command:

    gradlew build

**NOTE** : Track4j uses JavaFx so it may not work on older version of Java.

### Gradle

#### How to Include In Gradle Project
```gradle
repositories {
    mavenCentral()
    jCenter()
}
```
Reference the repository from this location using:
```gradle
dependencies {
    compile 'com.github.giulianini:track4j:1.0.0'
}
```

### Maven

#### How to Include In Maven Project
```xml
<dependency>
  <groupId>com.github.giulianini</groupId>
  <artifactId>track4j</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

### Ivy

#### How to Include In Ivy Project
```xml
<dependency org='com.github.giulianini' name='track4j' rev='1.0.0'>
  <artifact name='track4j' ext='pom' />
</dependency>
```

## How can I use Track4J?

### Set Up
1. 	Install the [__Kinect SDK__ ](https://www.microsoft.com/en-us/download/confirmation.aspx?id=40278)
1. 	Download natives - [Natives dll for Kinect](https://drive.google.com/open?id=1Dpvs71O2dN6AxnTrMUGLAIDJkp0y8YXD)
1. 	Put __ufdw_j4k_**bit.dll_ Natives into __HOME/.Track4J/native__. Track4J will find them.
1. 
	* **Build**  
	You can download the source code of the library and build it as mentioned previously. Building Track4J will generate Track4J.jar under the Track4J/build/libs folder. To use Track4J, import Track4J.jar into your project and start tracking your body :).
	* **Import the dependency**  
	Include the dependency for your build system.
 
### Code

#### Start the Tracker
```java
	final Sensor sensor = new Kinect(Joint.RIGHT_HAND, KinectSensors.SKELETON_ONLY, KinectVersion.KINECT1);
        final Tracking tracker = Tracker.getInstance();
        tracker.attacheSensor(sensor);
	tracker.startSensor();
        tracker.setOnJointTracked(new JointListener(){
	....});
```
#### Start via UI

```java
	final Sensor sensor = new Kinect(Joint.RIGHT_HAND, KinectSensors.SKELETON_ONLY, KinectVersion.KINECT1);
        final Tracking tracker = Tracker.getInstance();
        tracker.attacheSensor(sensor);
	final View view = new TrackerView(tracker);
        tracker.attacheUI(view);
        tracker.setOnJointTracked(new JointListener(){
	....});
```
#### Define your own UI

```java
 public class Gui extends AbstractView {
        public Gui(Tracking tracker) {
            super(tracker);
            // TODO Auto-generated constructor stub
        }
        @Override
        public void notifyOnFrameChange(int frame, Vector2D derivative, Vector2D path) {
            // TODO Auto-generated method stub   
        }
        @Override
        public void notifyOnFeatureVectorEvent() {
            // TODO Auto-generated method stub   
        }
    }
```


## Supported sensors

* **Windows:** 
	- _Kinect v1_
	- _Kinect v2_

### Future support

* **Windows:** 
	- _Lipmotion_
	
* **Linux:**
	- _Kinect v1_
	- _Kinect v2_
	


## Notes for Developers

### Importing the project
The project has been developed using Eclipse, and can be easily imported in such IDE.

#### Recommended configuration
* Install the required eclipse plugins:
  * In Eclipse, click "Help" -> "Eclipse Marketplace..."
  * In the search field enter "findbugs", then press Enter
  * One of the retrieved entries should be "FindBugs Eclipse Plugin", click Install
  * Click "< Install More"
  * In the search field enter "checkstyle", then press Enter
  * One of the retrieved entries should be "Checkstyle Plug-in" with a written icon whose text is "eclipse-cs", click Install
  * Click "< Install More"
  * Wait for Eclipse to resolve all the features
  * Click "Confirm >"
  * Follow the instructions, accept the license, wait for Eclipse to download and install the product, accept the installation and restart the IDE
  * When restarted, click "Help" -> "Install New Software..."
  * Click "Add..."
  * In "Location" field, enter `https://dl.bintray.com/pmd/pmd-eclipse-plugin/updates/`
  * The "Name" field is not mandatory (suggested: "PMD")
  * Click OK.
  * If not already selected, in "Work with:" dropdown menu choose the just added update site
  * Select "PMD for Eclipse 4" and click next
  * Follow the instructions, accept the license, wait for Eclipse to download and install the product, accept the installation and restart the IDE.
* Set the line delimiter to LF (only for Windows users)
  * In Eclipse, click window -> preferences
  * In the search form enter "encoding", then press Enter
  * Go to General -> Workspace
  * In the section "New text file line delimiter" check "Other" and choose Unix
  * Apply
* Use space instead of tabs
  * In Eclipse, click window -> preferences
  * Go to General -> Editors -> Text Editors
  * Check "insert spaces for tabs" option.
  * Apply.
  * Go to Java -> Code style -> Formatter
  * Click Edit button
  * In Indentation tab, under "General Settings", set "tab policy" to "Spaces only"
  * Apply (you should probably rename the formatter settings).

## Screenshots
![Track4J](/pic/track4j.gif)
![Track4J](/pic/derivative.gif)

## License
[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
	
	
