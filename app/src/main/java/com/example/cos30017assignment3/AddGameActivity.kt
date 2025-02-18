package com.example.cos30017assignment3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONException

class AddGameActivity : AppCompatActivity(), AddGameCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val enterGameName = findViewById<TextView>(R.id.enterGameName)
        val addButton = findViewById<Button>(R.id.addButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        addButton.setOnClickListener {
            val gameName = enterGameName.text
            checkIfGameExists(gameName.toString())
        }

        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

    }

    override fun findGameResult(passed: Boolean, gameName: String?) {
        if (passed){
            val entryRating = findViewById<SeekBar>(R.id.seekBar)
            val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
            val selectedRadio = radioGroup.checkedRadioButtonId

            val mIntent = Intent()
            mIntent.putExtra("gameName", gameName)
            mIntent.putExtra("userRating", entryRating.progress)
            mIntent.putExtra("completed", (selectedRadio == R.id.completedButton))
            setResult(RESULT_OK, mIntent)
            finish()
        } else {
            Toast.makeText(this, "Game not found. Please enter the name exactly.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfGameExists(name: String){
        val mBody = "fields name, id, summary, first_release_date, cover; where name = \"$name\";"
        val url = "https://api.igdb.com/v4/games"
        val mHeaders = HashMap<String, String>()

        mHeaders["Client-ID"] = "h1dkc2uktuwjq9i5r47x6j36wddq4e"
        mHeaders["Authorization"] = "Bearer 5ts6o7b7onbqbkwcv7kjfily40ofdu"

        val checkGame = object: JsonArrayRequest(Request.Method.POST, url, null,
            Response.Listener { response ->
                try {
                    val gameName: String = response.getString(0)
                    findGameResult(true, name)
                } catch (e: JSONException){
                    findGameResult(false, null)
                }
            },
            Response.ErrorListener {
                findGameResult(false, null)
            }){
            override fun getHeaders(): Map<String, String> {
                return mHeaders
            }
            override fun getBody(): ByteArray {
                return mBody.toByteArray()
            }
        }
        GsonQueue.getInstance(this).addToRequestQueue(checkGame)
    }
}


interface AddGameCallbacks{
    fun findGameResult(passed: Boolean, gameName: String?){

    }
}