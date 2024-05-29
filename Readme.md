# How to run the program?

## Method 1
* Copy and paste App.kt file to Kotlin playground (https://play.kotlinlang.org/)
* Set arguments as below to replicate data in problem statement
* Run the program and output will be logged in console

### Arguments: 
100 5 PKG1 50 30 OFR001 PKG2 75 125 OFR0008 PKG3 175 100 OFR003 PKG4 110 60 OFR002 PKG5 155 95 NA 2 70 200

## Method 2
* Create a jar file
* Run the jar file using terminal, passing the values as paramters

### Commands:
kotlinc App.kt -include-runtime -d kiki.jar 

java -jar kiki.jar 100 5 PKG1 50 30 OFR001 PKG2 75 125 OFR0008 PKG3 175 100 OFR003 PKG4 110 60 OFR002 PKG5 155 95 NA 2 70 200

### Note: Requires Java and Kotlin to be installed (Refer: https://kotlinlang.org/docs/command-line.html#create-and-run-an-application
)