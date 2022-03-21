package com.example.currencychecker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Currency(
    @SerializedName("ID")
    val id:String,
    @SerializedName("NumCode")
    val numCode:String,
    @SerializedName("CharCode")
    val charCode:String,
    @SerializedName("Nominal")
    val nominal:String,
    @SerializedName("Name")
    val name:String,
    @SerializedName("Value")
    val value: String,
    @SerializedName("Previous")
    val previous:String
)
