package com.example.analyticspipeline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    lateinit var analyticsDao: AnalyticsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext, AnalyticsDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()

        analyticsDao = db.analyticsDao()

        updateList()
    }

    fun onButton1Click(view: View) {
        buttonInsert("Button 1 clicked!")
    }

    fun onButton2Click(view: View) {
        buttonInsert("Button 2 clicked!")
    }

    private fun buttonInsert(buttonDesc: String) {
        analyticsDao.insert(Analytic("Button click", buttonDesc))
        updateList()
    }

    private fun updateList(){
        val analytics: List<Analytic> = analyticsDao.getAll()

        findViewById<TextView>(R.id.textView).text = ""

        for (analytic in analytics){
            findViewById<TextView>(R.id.textView).append("Id-${analytic.id} Type-${analytic.analyticType}" +
                    " Description-${analytic.analyticDescription} Time-${analytic.analyticRecordTime}\n")
        }
    }
}