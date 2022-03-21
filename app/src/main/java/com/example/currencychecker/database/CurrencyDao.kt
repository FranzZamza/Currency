package com.example.currencychecker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
@Query("SELECT * FROM currency_db")
fun getAllCurrency():LiveData<List<CurrencyModel>>

@Insert(onConflict = OnConflictStrategy.REPLACE)
fun insertAllCurrency(currencyList: List<CurrencyModel>)

}