package com.example.currencychecker

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var _valueOfRub: MutableLiveData<String> = MutableLiveData()
    var valueOfRub: LiveData<String> = _valueOfRub

    private var _isExpanded: MutableLiveData<Boolean> = MutableLiveData(false)
    var isExpanded: LiveData<Boolean> = _isExpanded

    private var _index: MutableLiveData<Int> = MutableLiveData(-1)
    var index: LiveData<Int> = _index

    var currencyListResponse: List<Currency> by mutableStateOf(listOf())
    var errorMessage: String by mutableStateOf("")


    fun setValueOfRub(value: String) {
        _valueOfRub.postValue(value)
    }

    fun getConvertValue(currencyValue: String): String {
        return if (!_valueOfRub.value.isNullOrEmpty()) (Math.round(_valueOfRub.value!!.toDouble() / currencyValue.toDouble() * 1000.0) / 1000.0).toString()
        else ""
    }


    fun setIndex(value: Int) {
        _index.postValue(value)
    }

    fun changeValueExpanded() {
        viewModelScope.launch {
            _isExpanded.postValue(!_isExpanded.value!!)
        }
    }


    fun getCurrencyList() {
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            try {
                val currencyList = mutableListOf<Currency>()
                for (item in apiService.getDaily().valute) {
                    currencyList.add(item.value)
                }
                currencyListResponse = currencyList

            } catch (e: Exception) {
                errorMessage = e.message.toString()

            }
        }
    }
}


