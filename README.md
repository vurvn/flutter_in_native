# flutter_in_native
simple flutter in native android & iOS app

<img src="https://miro.medium.com/max/2202/1*qPMZb9AePHS5C_GlkS8rPg.png" alt=" " />

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
**Create an Android Studio Project AFE-Android**

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

**Create a Flutter Module module_flutter**

Android Studio -> File -> New -> New Flutter Project -> Flutter Module. Click Next.

<img src="https://miro.medium.com/max/1796/1*gvDQn5LECt4ktcWjcalGFw.png" alt=" "  width="50%"/>
