package com.example.myactivityroomdb

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "person_table")
data class Person(
    @PrimaryKey(autoGenerate = true) val pId :Int,
    @ColumnInfo(name = "person_name") val name : String,
    @ColumnInfo(name = "person_age")val age : Int,
    @ColumnInfo(name = "person_city")val city: String,
    ) : Parcelable
