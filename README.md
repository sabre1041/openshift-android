openshift-android
=====================

## NOTICE

We are moving the app architecture to accomodate multiple mobile platforms instead of just Android.

Because of this, the development on this git repo has been discontinued.

The project has been moved: https://github.com/sabre1041/openshift-mobile.



## Description

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
