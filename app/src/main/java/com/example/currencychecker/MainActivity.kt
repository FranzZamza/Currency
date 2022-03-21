package com.example.currencychecker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.example.currencychecker.ui.theme.CurrencyCheckerTheme
import com.example.currencychecker.ui.theme.Purple200
import com.example.currencychecker.ui.theme.lightGrey

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyCheckerTheme {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ListOfCurrency(currencyList = viewModel.currencyListResponse, viewModel)
                    viewModel.getCurrencyList()
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(end = 24.dp, bottom = 24.dp), Alignment.BottomEnd) {
                        RefreshButton(viewModel = viewModel)
                }
            }
        }
    }
}
@Composable
fun RefreshButton(viewModel: MainViewModel) {
    Button(
        onClick = {
            viewModel.getCurrencyList()
        },
        Modifier.size(60.dp, 60.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Purple200),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_refresh),
            contentDescription = "Refresh",
            tint = Color.White
        )

    }
}
@Composable
fun ListOfCurrency(currencyList: List<Currency>, viewModel: MainViewModel) {
    LazyColumn {
        itemsIndexed(items = currencyList) { index, item ->
            ItemCurrency(currency = item, index, viewModel)
        }
    }
}
@Composable
fun ItemCurrency(currency: Currency, index: Int, viewModel: MainViewModel) {
    val isExpanded by viewModel.isExpanded.observeAsState(false)
    val indexFromModel by viewModel.index.observeAsState(initial = -1)
    val rubValue by viewModel.valueOfRub.observeAsState("")
    val maxChar=16
    val iconArrow=if (isExpanded){
        painterResource(id = R.drawable.ic_arrow_up)
    }else{
        painterResource(id = R.drawable.ic_arrow_down)
    }
    val heightValue = if (isExpanded && index == indexFromModel) 220.dp else 110.dp
    Card(
        Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth()
            .height(heightValue)
            .clickable {
                if (indexFromModel != -1 && isExpanded) {
                    viewModel.setIndex(index)
                }
                if (indexFromModel == index && isExpanded) {
                    viewModel.setIndex(-1)
                    viewModel.changeValueExpanded()
                }
                if (indexFromModel == -1 && !isExpanded) {
                    viewModel.changeValueExpanded()
                    viewModel.setIndex(index)
                }
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
    ) {
        Surface() {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = currency.name,
                    Modifier.padding(top = 4.dp),
                    color = lightGrey
                )
            }
            Row(
                verticalAlignment = if (isExpanded && indexFromModel == index) Alignment.Top else Alignment.CenterVertically,
                modifier = if (isExpanded && indexFromModel == index) Modifier.padding(top = 36.dp) else Modifier.padding(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "${currency.value} RUB",
                    color = Color.DarkGray
                )
                Text(
                    text = "1 ${currency.charCode}",
                    color = Color.DarkGray
                )
            }
            if (isExpanded && index == indexFromModel) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .padding(bottom = 46.dp, start = 15.dp)
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = rubValue,
                        onValueChange = {
                                it ->
                            if (maxChar>it.length)
                            viewModel.setValueOfRub(it)
                        },
                        Modifier.weight(4f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(
                                text = "RUB",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        singleLine = true
                    )
                    Column(Modifier.weight(1f)) {
                    }
                    TextField(
                        value = viewModel.getConvertValue(currency.value),
                        onValueChange = {},
                        modifier = Modifier
                            .weight(4f)
                            .padding(end = 16.dp),
                        readOnly = true,
                        singleLine = true,
                        label = {
                            Text(
                                text = currency.charCode,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }
            }
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                Icon(painter = iconArrow,
                    contentDescription ="Arrow",
                    Modifier.padding(bottom = 2.dp),
                    tint = Color.DarkGray)
            }
        }
    }
}