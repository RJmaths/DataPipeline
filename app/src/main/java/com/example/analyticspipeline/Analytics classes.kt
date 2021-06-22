package com.example.analyticspipeline

import androidx.room.*
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Analytic(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "analyticType") var analyticType: String,
    @ColumnInfo(name = "analyticDescription") var analyticDescription: String,
    @ColumnInfo(name = "analyticRecordTime") var analyticRecordTime: String//,
    //@ColumnInfo(name = "priority") var priority: Int
){
    constructor(analyticType: String, analyticDesc: String):this(0, analyticType,
        analyticDesc, getTime())//,createPriority())
}

//fun createPriority(): Int {

//}

fun getTime(): String {
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    return sdf.format(Date())
}

@Dao
interface AnalyticsDao {
    @Query("SELECT * FROM analytic")
    fun getAll(): List<Analytic>

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    fun insert(analytic: Analytic)

    @Delete
    fun delete(analytics: Analytic)
}


@Database(entities = [Analytic::class], version = 1)
abstract class AnalyticsDatabase : RoomDatabase() {
    abstract fun analyticsDao(): AnalyticsDao
}