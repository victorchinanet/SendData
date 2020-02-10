package com.chinanetco.senddata

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.chinanetco.senddata.model.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list


class MainActivity : AppCompatActivity() {
    var view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        view = findViewById(android.R.id.content)

        val victor = User("Victor", "1")
        val rain = User("Rain", "2")
        val list = listOf(victor, rain)

        val json = Json(JsonConfiguration.Stable)
        val jsonData = json.stringify(User.serializer().list, list)

        tv_data.text = jsonData

        fab.setOnClickListener { view ->

            sendDataToOtherApp(jsonData)

        }
    }

    private fun sendDataToOtherApp(jsonData: String) { // pass the uri (scheme & screen path) of a screen defined from app XXX that you want to open (e.g HomeActivity)
        val uri: Uri = Uri.parse("http://watsons.co/list?data=${Uri.encode(jsonData)}")

        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        //Verify if app XXX has this screen path
        val packageManager = packageManager
        val activities =
            packageManager.queryIntentActivities(mapIntent, 0)
        val isIntentSafe = activities.size > 0
        //Start HomeActivity of app XXX because it's existed
        if (isIntentSafe) {
            startActivity(mapIntent)
            showSnackBar(jsonData)
        }
    }

    private fun showSnackBar(sentData:String){
        view?.let {
            val mySnackbar = Snackbar.make(it, sentData, Snackbar.LENGTH_SHORT)
            mySnackbar.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
