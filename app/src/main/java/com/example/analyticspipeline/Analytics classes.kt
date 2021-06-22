package com.example.analyticspipeline

import androidx.room.*
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Analytic(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "analyticType") var analyticType: String,
    @ColumnInfo(name = "analyticDescription") var analyticDescription: String,
    @ColumnInfo(name = "analyticRecordTime") var analyticRecordTime: String,
    @ColumnInfo(name = "priority") var priority: Int
){
    constructor(analyticType: String, analyticDesc: String):this(0, analyticType,
        analyticDesc, getTime(),createPriority())
}

//fun createPriority(): Int {

//}

fun getTime(): String {
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    return sdf.format(Date())
}

fun createPriority(): Int {
    return (analyticsDao.getLowestPriority()?:Int.MIN_VALUE) - 1
}

@Dao
interface AnalyticsDao {
    @Query("SELECT * FROM analytic")
    fun getAll(): List<Analytic>

    @Query("SELECT * FROM analytic ORDER BY id DESC LIMIT 1")
    fun getLatestAnalytic(): Analytic?

    @Query("SELECT MIN(priority) FROM analytic")
    fun getLowestPriority(): Int?

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    fun insert(analytic: Analytic)

    @Delete
    fun delete(analytic: Analytic)
}


@Database(entities = [Analytic::class], version = 1)
abstract class AnalyticsDatabase : RoomDatabase() {
    abstract fun analyticsDao(): AnalyticsDao
}