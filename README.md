# flutter_in_native
simple flutter in native android & iOS app

<img src="https://miro.medium.com/max/2202/1*qPMZb9AePHS5C_GlkS8rPg.png" alt=" " width="100%" />

First! Please visit official document here: https://flutter.dev/docs/development/add-to-app 

## Android/iOS Module
The app is simple. It Adds/Multiplies two numbers. The numbers are entered in the Android/iOS app which will be sent to Flutter module for Addition or Multiplication, receive the results and updates the UI. We will call this app AFE (Add Flutter to Existing) app.

## Flutter Module
Flutter module will receive two numbers from Android/iOS module. It will then give the option to perform Addition or Multiplication. Based on the operation, it will deliver results back to Android/iOS app.


## Add Flutter to existing Android/iOS app Series is divided into three parts.
### Part 1. Add Flutter in existing android app
### Part 2. Add Flutter to an existing iOS app
### Part 3. Pass Data between Flutter to Android/iOS app

# Part 1. Add Flutter in existing android app

### Create an Android Studio Project AFE-Android

<img src="https://miro.medium.com/max/1798/1*Co5MGm3fOmgqRtwTEZccjQ.png" alt=" " width="80%"/>

Made a few changes in UI and this is how the Android app looks like.

<img src="https://miro.medium.com/max/688/1*osZ0KsBWzNAxtdCx0fTe8g.png" alt=" "  width="50%"/>

```kotlin
package net.ngocdung.afe_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_input_numbers.*

class InputNumbersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_numbers)

        send.setOnClickListener {

            val pair = isInputValid()
            if (pair != null) {
                sendDataToFlutterModule(pair.first, pair.second)
            }
        }

    }

    private fun sendDataToFlutterModule(first: Int, second: Int) {
        //TODO will be implemented later
    }

    private fun isInputValid(): Pair<Int, Int>? {
        val number1 = et_number_1.text.toString()
        val number2 = et_number_2.text.toString()

        when {
            number1.isBlank() -> showToast("Please enter first number")
            number2.isBlank() -> showToast("Please enter second number")
            else -> return Pair(number1.toInt(), number2.toInt())
        }

        return null
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
```
Once we integrate Flutter Module, we will use **sendDataToFlutterModule** method to open flutter module and pass the data to it. 
As of now, we are done with **AFE-Android** part.

### Create a Flutter Module module_flutter 

Android Studio -> File -> New -> New Flutter Project -> Flutter Module. Click **Next**.

<img src="https://miro.medium.com/max/1796/1*gvDQn5LECt4ktcWjcalGFw.png" alt="Create a Flutter Module"  width="80%"/>

Configure Flutter Module. Enter Project Name as **module_flutter**. 
Provide Flutter SDK Path and Project location. Click **Next**.
Set the package name. Click **Finish**.

Made changes in ***main.dart*** to show two numbers received from Android/iOS module and an option to Send results back. This is how **module_flutter** module looks like:

<img src="https://miro.medium.com/max/690/1*zd_qs0jrq0eVnmGUz-B8Hw.png" alt="Create a Flutter Module" height="600px"/><img src="https://miro.medium.com/max/716/1*I_2cQOqCt4BOrO17xcQp2A.png" alt="Create a Flutter Module" height="600px"/>

```dart
import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'AFE Flutter',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'AFE Flutter Module'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String dropdownValue = 'Add';
  int result = 0;

  _addNumbers(int n1, int n2) {
    return n1 + n2;
  }

  _multiplyNumbers(int n1, int n2) {
    return n1 * n2;
  }

  _setResults(int n1, int n2) {
    setState(() {
      if (dropdownValue == 'Add') {
        result = _addNumbers(n1, n2);
      } else {
        result = _multiplyNumbers(n1, n2);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(widget.title),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Container(
                  padding: EdgeInsets.all(10),
                  child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: <Widget>[
                        Text('First Number: ',
                            style:
                                TextStyle(color: Colors.black, fontSize: 16)),
                        Text('10',
                            style: TextStyle(color: Colors.blue, fontSize: 16)),
                      ])),
              Container(
                  padding: EdgeInsets.all(10),
                  child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: <Widget>[
                        Text('Second Number: ',
                            style:
                                TextStyle(color: Colors.black, fontSize: 16)),
                        Text('20',
                            style: TextStyle(color: Colors.blue, fontSize: 16)),
                      ])),
              Container(
                  margin: EdgeInsets.all(10),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      DropdownButton<String>(
                        value: dropdownValue,
                        style: TextStyle(color: Colors.blue, fontSize: 16),
                        items: <String>['Add', 'Multiply']
                            .map<DropdownMenuItem<String>>((String value) {
                          return DropdownMenuItem<String>(
                            value: value,
                            child: Text(value),
                          );
                        }).toList(),
                        onChanged: (String newValue) {
                          setState(() {
                            dropdownValue = newValue;
                          });
                        },
                      )
                    ],
                  )),
              RaisedButton(
                onPressed: () {
                  _setResults(10, 20);
                  _sendResultsToAndroidiOS();
                },
                textColor: Colors.white,
                padding: const EdgeInsets.all(0.0),
                child: Container(
                    decoration: BoxDecoration(color: Colors.blue),
                    padding: const EdgeInsets.all(10.0),
                    child: const Text('Send Results to Android/iOS module',
                        style: TextStyle(fontSize: 16))),
              ),
              Container(
                  margin: EdgeInsets.all(10),
                  child: Text(
                    'Result: $result',
                    style: TextStyle(color: Colors.blue, fontSize: 16),
                  ))
            ],
          ),
        ));
  }

  void _sendResultsToAndroidiOS() {
    //TODO send results to Android/iOS module
  }
}
```

### Integrating module_flutter in AFE-Android

To import **module_flutter** module, right click on Project Name -> New -> Module. 
Scroll to the bottom and select **“Import Flutter Module”**. Click **Next**.

<img src="https://miro.medium.com/max/1798/1*XOBI59Vg_4eLZ3IBakH41w.png" alt="Import_flutter_module"  width="80%"/>

On the next screen, give **module_flutter** module’s path and Click **Finish**. After finishing this, you will find below changes in gradle files.

**settings.gradle** will have a path to AFE_Flutter directory.

```
include ':app'
setBinding(new Binding([gradle: this]))
evaluate(new File(
  settingsDir.parentFile,
  'module_flutter/.android/include_flutter.groovy'
))
```

app’s **build.gradle** has a dependency added for flutter module.

``` 
implementation project(':flutter')
```

And this is how project pane looks like

<img src="https://i.imgur.com/8JA1gxv.png" alt="AFE-Android project pane after importing module_flutter"  width="50%"/>


# If you select **use androidx. artifacts**, you will meet issues after compile build the project like this.

<img src="https://i.imgur.com/mI04Epm.png" alt="Import_flutter_module"  width="80%"/>

## change import packages and dependencies
> android/Flutter/src/main/java/io/flutter/facade/Flutter.java
```dart
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
//change to
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.annotation.NonNull;
```
> android/Flutter/src/main/java/io/flutter/facade/FlutterFragment.java

```dart
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
//change to
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
```

<img src="https://i.imgur.com/lQYyZBD.png" alt="Import_flutter_module"  width="80%"/>

> android/app/build.gradle

<img src="https://i.imgur.com/atc7z56.png" alt="Import_flutter_module"  width="80%"/>

```dart
testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
//change to
testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
```

```dart
implementation 'com.android.support:appcompat-v7:27.1.1'
implementation 'com.android.support.constraint:constraint-layout:1.1.2'
implementation 'com.android.support:design:27.1.1'
androidTestImplementation 'com.android.support.test:runner:1.0.2'
androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
//change to
implementation 'androidx.appcompat:appcompat:1.1.0'
implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
androidTestImplementation 'androidx.test:runner:1.2.0'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
```

> android/flutter/build.gradle

<img src="https://i.imgur.com/WVUndY6.png" alt="Import_flutter_module"  width="80%"/>

```dart
testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
//change to
testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
```

```dart
implementation 'com.android.support:support-v13:27.1.1'
implementation 'com.android.support:support-annotations:27.1.1
//change to
implementation 'androidx.legacy:legacy-support-v13:1.0.0'
implementation 'androidx.annotation:annotation:1.1.0'
```





