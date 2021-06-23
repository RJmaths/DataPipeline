package com.example.analyticspipeline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

lateinit var analyticsDao: AnalyticsDao

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: TextView = findViewById(R.id.textView)
        textView.movementMethod = ScrollingMovementMethod()

        val db = Room.databaseBuilder(
            applicationContext, AnalyticsDatabase::class.java, "database-name"
        ).build()

        analyticsDao = db.analyticsDao()

        CoroutineScope(IO).launch {
            updateList()
        }
    }

    fun onButton1Click(view: View) {
        buttonInsert("Button 1 clicked!")
    }

    fun onButton2Click(view: View) {
        buttonInsert("Button 2 clicked!")
    }

    private fun buttonInsert(buttonDesc: String) {

        CoroutineScope(IO).launch {
            analyticsDao.insert(Analytic("Button click", buttonDesc))
            updateList()
        }
    }

    fun deleteLatest(view: View) {
        CoroutineScope(IO).launch {
            val analytic: Analytic? = analyticsDao.getLatestAnalytic()
            if (analytic == null) {
                val snack = Snackbar.make(view, "Database is empty", Snackbar.LENGTH_LONG)
                snack.show()
            } else {
                analyticsDao.delete(analytic)
                updateList()
            }
        }
    }

    private suspend fun updateList(){
        val analytics: List<Analytic> = analyticsDao.getAll()

        withContext(Main) {
            findViewById<TextView>(R.id.textView).text = ""

            for (analytic in analytics) {
                findViewById<TextView>(R.id.textView).append(
                    "Id-${analytic.id}\nType-${analytic.analyticType}\n" +
                            "Description-${analytic.analyticDescription}\nTime-${analytic.analyticRecordTime}\n\n"
                )
            }
        }
    }
}