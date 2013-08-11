openshift-android
=====================

Android application to manage an OpenShift account

## Configuration

Modify the configuration file located in _openshift-android/assets/openshiftconfiguration.properties_ with the openshift username and password

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