# flutter_in_native
simple tutorial import flutter to native android & iOS app.

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

**settings.gradle** will have a path to **module_flutter** directory.

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

Also, To Add Flutter module to existing Android project, Java 8 compatibility is required. Please add below lines under the android section of app’s **build.gradle** file

```dart
compileOptions {
    sourceCompatibility 1.8
    targetCompatibility 1.8
}
```

## Now, we can just write a code to open flutter module in our AFE-Android app. 
You can either create a View or a Fragment and add it to your layout.

```kotlin
//Import as a view
val flutterView = Flutter.createView(
    this@FlutterViewActivity,
    lifecycle, null
)

addContentView(
    flutterView,
    FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
)//Import as a fragmentval tx = supportFragmentManager.beginTransaction()
tx.replace(R.id.someContainer, Flutter.createFragment(null))
tx.commit()
```
First, we will create an activity (FlutterViewActivity)and open it from InputNumbersActivity. FlutterViewActivity will render the flutter view.

```kotlin
package net.ngocdung.afe_android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import io.flutter.facade.Flutter

class FlutterViewActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, FlutterViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val flutterView = Flutter.createView(
            this@FlutterViewActivity,
            lifecycle, null
        )

        addContentView(
            flutterView,
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        )
    }
}
```
From InputNumbersActivity, edit function **sendDataToFlutterModule**

```kotlin
private fun sendDataToFlutterModule(first: Int, second: Int) {
    FlutterViewActivity.startActivity(this)
}
```
**We have successfully added module_flutter to the Android app.**

# Part 2. Add Flutter to an existing iOS app
coming soon

# Part 3. Pass Data between Flutter to Android/iOS app
Flutter allows you to call platform-specific APIs whether available in Java or Kotlin code on Android, or in Objective-C or Swift code on iOS.

In Flutter documents, Flutter part is the client app and native part i.e. Android/iOS part is the host app. Flutter’s platform-specific API relies on a flexible message passing style. Client app (Flutter part of the app)sends the message to the host over the platform channel. The host app (Android/iOS part of the app) listens on the platform channel and receives the message.

Messages are passed between the client (UI) and host (platform) using platform channels as illustrated in this diagram:

<img src="https://miro.medium.com/max/580/1*IyZxSjFOQNVju3WUu758Bg.png" alt="Import_flutter_module"  width="100%"/>

**Setting up module_flutter to receive data from AFE_Android and AFE_iOS**

To pass the data between the client and the host app, we must create a platform channel on which we will send/receive data.

In module_flutter Module create a channel with the name **‘net.ngocdung.afe/data’**

```dart
class _MyHomePageState extends State<MyHomePage> {
.....
  static const platform = const MethodChannel(‘net.ngocdung.afe/data’);
......
}
```

Create a method **_receiveFromNative** to handle data received from the host. We will process the data and show it in the UI later on.

Initialize the platform in the **_MyHomePageState** constructor and set the **_receiveFromNative** method as Method Call Handler.

```dart
_MyHomePageState() {
  platform.setMethodCallHandler(_receiveFromHost);
}
```

Now **module_flutter** is listening to the channel ‘net.ngocdung.afe/data’. Any message sent to this channel will be received in **_receiveFromNative** method.

## Setting up AFE-Android to send and receive data from module_flutter

Since we rendered Flutter View inside **FlutterViewActivity**, We will set it up to send and receive data from **module_flutter**.

To Send entered numbers to **module_flutter**, add below code after **addContentView** method in **FlutterViewActivity.kt**

```kotlin
val first = intent?.extras?.getInt("first")
val second = intent?.extras?.getInt("second")

val json = JSONObject()
json.put("first", first)
json.put("second", second)

Handler().postDelayed({
    MethodChannel(flutterView, CHANNEL).invokeMethod("formNativeToFlutter", json.toString())
}, 500)
```

We are getting first and second numbers from previous activity in extras and creating the JSON object to send to **module_flutter**. **MethodChannel** is being used for that. **"formNativeToFlutter"** is the method name we are invoking and another is arguments in which JSON string is being passed.

```
Note: I have added the delay of 500ms because it takes a little time to render FluterView and initialize the channel. I am using emulator, you may not face this issue in the real device. Alternatively, from module_flutter you can notify host app to send data to module_flutter.
```

Now, to receive data from **module_flutter**, we need to set up **MethodCallHandler** as we did in **module_flutter**.

```dart
MethodChannel(flutterView, CHANNEL).setMethodCallHandler { call, result ->
    // manage method calls here
    if (call.method == "fromFlutterToNative") {
        val resultStr = call.arguments.toString()
        //TODO parse result string and send results back to previous activity
    } else {
        result.notImplemented()
    }
}
```
Here, **"fromFlutterToNative"** method name is being used to receive data from **module_flutter**.

## Passing data between AFE-Android and module_flutter

Receiving the data in **module_flutter** and showing it in UI.

```dart
Future<void> _receiveFromNative(MethodCall call) async {
  int f = 0;
  int s = 0;

  try {
    print(call.method);

    if (call.method == "formNativeToFlutter") {
      final String data = call.arguments;
      print(call.arguments);
      final jData = jsonDecode(data);

      f = jData['first'];
      s = jData['second'];
    }
  } on PlatformException catch (e) {
    //platform may not able to send proper data.
  }

  setState(() {
    _first = f;
    _second = s;
  });
}
```

Also, remove hardcoded numbers in **module_flutter** and set ***_first*** and ***_second*** as a text in both the text view.

Performing selected operation and sending results back to the host app.

```dart
void _sendResultsToAndroidiOS() {
  if (dropdownValue == 'Add') {
    _result = _addNumbers(_first, _second);
  } else {
    _result = _multiplyNumbers(_first, _second);
  }

  Map<String, dynamic> resultMap = Map();
  resultMap['operation'] = dropdownValue;
  resultMap['result'] = _result;

  setState(() {
    resultStr = resultMap.toString();
  });

  platform.invokeMethod("fromFlutterToNative", resultMap));
}
```

Now, we need to process the data received from the client app in **FlutterViewActivity** and send it back to **InputNumbersActivity** to show it in the UI.

```dart
MethodChannel(flutterView, CHANNEL).setMethodCallHandler { call, result ->
    // manage method calls here
    if (call.method == "fromFlutterToNative") {
        val resultStr = call.arguments.toString()
        val resultJson = JSONObject(resultStr)
        val res = resultJson.getInt("result")
        val operation = resultJson.getString("operation")

        val intent = Intent()
        intent.putExtra("result", res)
        intent.putExtra("operation", operation)
        setResult(Activity.RESULT_OK, intent)
        finish()
    } else {
        result.notImplemented()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
```

and in **InputNumbersActivity.kt’s onActivityResult** method,

```dart
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == 100) {
        if (resultCode == Activity.RESULT_OK) {
            val result = data?.extras?.getInt("result")
            val operation = data?.extras?.getString("operation")

            tvResult.text = "${when (operation) {
                "Add" -> "Addition"
                "Multiply" -> "Multiplication"
                else -> "NA"
            }} of the entered numbers is $result"
        } else {
            tvResult.text = "Could not perform the operation"
        }
    }
}
```

**We are done!! AFE-Android should show the result of the operation being performed on input numbers.
You can find the full source code on this Github Repositories.**

## Passing data between AFE_iOS and module_flutter
coming soon


## This article based on
[Add Flutter to existing Android/iOS app | Written by Nirav Tukadiya](https://medium.com/flutter-community/add-flutter-to-existing-android-ios-app-ae8c4fb1582e)

[Tutorial import Flutter module to Android](https://juejin.im/post/5d47d8dc6fb9a06b0517d3d5)
