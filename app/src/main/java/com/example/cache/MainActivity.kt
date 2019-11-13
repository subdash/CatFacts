package com.example.cache

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.cache.service.RetrievalMethod
import com.example.cache.service.ServiceInterface
import com.inmotionsoftware.promisekt.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

//
// Created by Dashiell Sublett, 10/2019
//

class MainActivity : AppCompatActivity() {

    //
    // Methods
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.pawButton.setOnClickListener(this.PawButtonListener())
        this.clearButton.setOnClickListener(this.ClearButtonListener())
    }

    override fun onBackPressed() {
        this.startActivity(Intent(this, SplashActivity::class.java)
            .apply { action = Intent.ACTION_VIEW })
    }

    private fun showLoadingSpinner() = runOnUiThread {
        this.loadingSpinner.visibility = View.VISIBLE
    }

    private fun hideLoadingSpinner() = runOnUiThread {
        this.loadingSpinner.visibility = View.GONE
    }

    private fun showToastAtTop(context: Context, text: String) = Toast
        .makeText(context, text, Toast.LENGTH_SHORT)
        .run {
            setGravity(Gravity.TOP, 0, 48)
            show()
        }

    //
    // Classes
    //

    inner class PawButtonListener : View.OnClickListener {
        private fun getRandomCatFact(factArray: JSONArray): String =
            (factArray
                .get(Random.nextInt(0, factArray.length())) as JSONObject)
                .getString("text")

        override fun onClick(v: View?) {
            this@MainActivity.showLoadingSpinner()
            ServiceInterface(this@MainActivity)
                .getData()
                .done { data ->
                    runOnUiThread {
                        this@MainActivity.dataTv.text =
                            getRandomCatFact((JSONObject(data.second).get("all") as JSONArray))
                        when (data.first) {
                            RetrievalMethod.LOCAL -> {
                                this@MainActivity.showToastAtTop(
                                    this@MainActivity, "Cat fact retrieved locally."
                                )
                            }
                            RetrievalMethod.REMOTE -> {
                                this@MainActivity.showToastAtTop(
                                    this@MainActivity, "Cat fact retrieved remotely."
                                )
                            }
                        }
                    }
                }
                .ensure { this@MainActivity.hideLoadingSpinner() }
                .catch {
                    runOnUiThread {
                        this@MainActivity.showToastAtTop(
                            this@MainActivity, "Failed to retrieve data."
                        )
                    }
                }
        }
    }

    inner class ClearButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            this@MainActivity.let {
                it.dataTv.text = ""
                val sharedPreferences = it.getSharedPreferences("Data", Context.MODE_PRIVATE)
                val sharedPreferencesKey = "Data_Key"

                with(sharedPreferences.edit()) {
                    putString(sharedPreferencesKey, "DEFAULT")
                    apply()
                }

                it.showToastAtTop(it, "Cached data cleared.")
            }
        }
    }
}
