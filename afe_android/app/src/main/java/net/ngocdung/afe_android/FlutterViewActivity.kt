package net.ngocdung.afe_android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import io.flutter.facade.Flutter
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class FlutterViewActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL = "net.ngocdung.afeandroid/data"

        fun startActivity(context: InputNumbersActivity, first: Int, second: Int) {
            val intent = Intent(context, FlutterViewActivity::class.java)
            intent.putExtra("first", first)
            intent.putExtra("second", second)
            context.startActivityForResult(intent, 100)
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
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

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

        val first = intent?.extras?.getInt("first")
        val second = intent?.extras?.getInt("second")

        val json = JSONObject()
        json.put("first", first)
        json.put("second", second)

        Handler().postDelayed({
            MethodChannel(flutterView, CHANNEL).invokeMethod("formNativeToFlutter", json.toString())
        }, 500)

    }
}
