package com.example.currencychecker.database

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "currency_db")
data class CurrencyModel(
    val id:String,
    val numCode:String,
    val charCode:String,
    val nominal:String,
    val name:String,
    val value: String,
    val previous:String
)