[![Build Status](https://travis-ci.org/Giulianini/Jestures.svg?branch=master)](https://travis-ci.org/Giulianini/Jestures)
[![PyPI - License](https://img.shields.io/github/license/Giulianini/Jestures.svg)](https://github.com/Giulianini/Jestures/blob/master/LICENSE.txt)
 [ ![Download](https://api.bintray.com/packages/giulianini/maven/Jestures/images/download.svg?version=0.0.2) ](https://bintray.com/giulianini/maven/Jestures/0.0.2/link)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.giulianini.jestures/jestures.svg)](https://search.maven.org/search?q=g:com.github.giulianini.jestures)
[![Javadocs](https://www.javadoc.io/badge/com.github.giulianini.jestures/jestures.svg)](https://www.javadoc.io/doc/com.github.giulianini.jestures/jestures)
![GitHub issues](https://img.shields.io/github/issues/Giulianini/Jestures.svg)
![GitHub last commit](https://img.shields.io/github/last-commit/Giulianini/Jestures.svg)
![Maintenance](https://img.shields.io/maintenance/yes/2018.svg)
![GitHub repo size in bytes](https://img.shields.io/github/repo-size/Giulianini/Jestures.svg)
![GitHub contributors](https://img.shields.io/github/contributors/Giulianini/Jestures.svg)

---

# Jestures

## A simple framework for gesture recognition in Java.

<h1 align="center">
    <img src="/pic/Jestures.png">
</h1>


## Download
* Released builds are available from - [Bintray](https://bintray.com/giulianini/maven/Jestures/_latestVersion)
* Released builds are available from - [Maven Central](https://search.maven.org/search?q=g:com.github.giulianini.jestures)
* Snapshot builds are available from - [Artifactory](http://oss.jfrog.org/oss-snapshot-local/com/github/giulianini/jestures/)

## Javadocs 
If you need to access the documentation for any stable version, [javadoc.io](https://www.javadoc.io/doc/com.github.giulianini.track4j/track4j/) is probably the right place to search in.

## Build
To build Jestures, execute the following command:

    gradlew build

**NOTE** : Jestures uses JavaFx so it may not work on older version of Java.

## Demo
Run the demo with:
    
    cd Jestures_Recorder
    gradlew run

## Gesture Recorder Tool
Run the demo with:

    cd Jestures_Demo
    gradlew run

## Adding Jestures to your build
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
    compile 'com.github.giulianini.jestures:jestures:1.0.0'
}
```

### Maven

#### How to Include In Maven Project
```xml
<dependency>
  <groupId>com.github.giulianini.jestures</groupId>
  <artifactId>jestures</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

### Ivy

#### How to Include In Ivy Project
```xml
<dependency org='com.github.giulianini.jestures' name='jestures' rev='1.0.0'>
  <artifact name='jestures' ext='pom' ></artifact>
</dependency>
```

## How can I use Jestures?

### Set Up The Framework
1. 	Install the [__Kinect SDK__ ](https://www.microsoft.com/en-us/download/confirmation.aspx?id=40278)
1. 	Download natives - [Natives dll for Kinect](https://drive.google.com/open?id=1Dpvs71O2dN6AxnTrMUGLAIDJkp0y8YXD)
1. 	Put __ufdw_j4k_**bit.dll_ Natives into __HOME/.Jestures/native__. Jestures will find them.
1. 
	* **Build**  
	You can download the source code of the library and build it as mentioned previously. Building Jestures will generate Jestures.jar under the Track4J/build/libs folder. To use Jestures, import Jestures.jar into your project and start recognizing gestures :).
	* **Import the dependency**  
	Include the dependency for your build system.
 
 ### Start The Recorder Tool
 1.	Follow the step above and the recorder tool must work with your kinect version.
 
### Code

#### Tracker only
```java
	final Sensor sensor = new Kinect(Joint.RIGHT_HAND, KinectSensors.SKELETON_ONLY, KinectVersion.KINECT1);
        final Tracking tracker = Tracker.getInstance();
        tracker.attacheSensor(sensor);
	tracker.startSensor();
        tracker.setOnJointTracked(new JointListener(){
	....});
```

#### Start the Recognizer
```java
	final Sensor sensor = new Kinect(Joint.RIGHT_HAND, KinectSensors.SKELETON_ONLY, KinectVersion.KINECT1);
        final Recognition recognizer = Recognizer.getInstance();
        recognizer.attacheSensor(sensor);
	recognizer.startSensor();
        recognizer.setOnGestureRecognized(System.out::println);
```

#### Start via UI

```java
	final Sensor sensor = new Kinect(Joint.RIGHT_HAND, KinectSensors.SKELETON_ONLY, KinectVersion.KINECT1);
        final Recognition recognizer = Recognizer.getInstance();
        recognizer.attacheSensor(sensor);
        final RecognitionView view = new RecognitionScreenView(recognizer);
        recognizer.attacheUI(view);
        recognizer.setOnGestureRecognized(System.out::println);
```
#### Define your own UI

```java
 public class Gui extends AbstractView {
        public Gui(Recognition recognizer) {
            super(recognizer);
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
	..... A lot of methods to implements
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
![Track4J](/pic/recorder.gif)
![Track4J](/pic/dataset.gif)
![Track4J](/pic/recognizer_canvas.gif)
![Track4J](/pic/recognizer_derivative.gif)

## License
[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
	
	
