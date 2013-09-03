openshift-android
=====================

Android application to manage an OpenShift account

## Getting Started

Initialize the Maven repository by executing the following command:

	mvn initialize

##Basic Commands

Run the following commands from within the openshift-android project

Start Android Emulator
     
    mvn android:emulator-start
    
Deploy
   
    mvn android:deploy
    
Run Project on Emulator

    mvn android:run
    
Undeploy

    mvn android:undeploy
 
__Note__: Always make sure to mvn clean install after code modification.

OpenShift Rest API 1.6