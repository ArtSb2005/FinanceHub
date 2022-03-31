package com.vvb.financehub

import android.R.attr.data
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var apiClient: DefaultApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    override fun onStart() {
        super.onStart()

        findViewById<CardView>(R.id.ticker).setVisibility(View.INVISIBLE)

        findViewById<Button>(R.id.next).setOnClickListener {
            SearchTicker()
        }
    }

    private fun SearchTicker() {
        
        ApiClient.apiKey["token"] = "c8odjm2ad3iddfsasfg0" // Replace with your API key
        apiClient = DefaultApi()

        val track = apiClient.companyProfile2(symbol = findViewById<EditText>(R.id.search).text.toString(), isin = null, cusip = null)
        val tickers = track.toString().split(",").map{ it }

        val ticker = findViewById<TextView>(R.id.symbol)
        ticker.text = tickers.toString().split(",","=").map{ it }[9]
        findViewById<TextView>(R.id.name).text = tickers.toString().split(",","=").map{ it }[7]

        val image_url = tickers.toString().split(",","=").map{ it }[17] + '=' + ticker.text
        Picasso.get().load(image_url).into(findViewById<ImageView>(R.id.imageView));

        val price = apiClient.quote(findViewById<EditText>(R.id.search).text.toString())
        findViewById<TextView>(R.id.money).text = price.toString().split(",","=").map{ it }[7]

        if (ticker.text == "null"){
            Toast.makeText(this, "Неправильный тикер",
                Toast.LENGTH_LONG).show();
            findViewById<CardView>(R.id.ticker).setVisibility(View.INVISIBLE)
        } else {
            findViewById<CardView>(R.id.ticker).setVisibility(View.VISIBLE)
        }
    }

}