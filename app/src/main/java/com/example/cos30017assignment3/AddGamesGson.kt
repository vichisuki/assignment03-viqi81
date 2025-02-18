package com.example.cos30017assignment3

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.google.gson.Gson
import org.json.JSONException

private const val hereTag = "AddGamesGson.kt"

class AddGamesGson(context: Context, private val viewModel: GameViewModel) : CallbackLibs {

    private val mHeaders = HashMap<String, String>()
    private val baseUrl = "https://api.igdb.com/v4/"
    private val context: Context = context.applicationContext
    val gameAdded : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(null)
    }

    init {
        mHeaders["Client-ID"] = "h1dkc2uktuwjq9i5r47x6j36wddq4e"
        mHeaders["Authorization"] = "Bearer 5ts6o7b7onbqbkwcv7kjfily40ofdu"
    }

    fun addGame(name: String, rating: Float, completed: Boolean){
        val mBody = "fields name, id, summary, first_release_date, cover; where name = \"$name\";"
        val url = baseUrl + "games"
        Log.i(hereTag, "Attempted body: $mBody")

        val gameReq = object: JsonArrayRequest(Request.Method.POST, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = Gson().fromJson(response.getString(0), Game::class.java)
                    responseObj.rating = rating
                    responseObj.complete = completed
                    onRegularCallback(responseObj)
                } catch (e: JSONException) {
                    Log.e(hereTag, "Error: $e. Name was likely incorrect.")
                    gameAdded.postValue(false)
                }
            },
            Response.ErrorListener { e ->
                Log.e(hereTag, "Volley Error Details: $e")
                gameAdded.postValue(false)
            }) {
            override fun getHeaders(): Map<String, String> {
                return mHeaders
            }
            override fun getBody(): ByteArray {
                return mBody.toByteArray()
            }
        }
        GsonQueue.getInstance(context).addToRequestQueue(gameReq)
    }



    override fun onRegularCallback(responseObj: Game) {
        val url = baseUrl + "covers"
        val mBody = "fields image_id; where id = ${responseObj.cover};"


        val imgReq = object: JsonArrayRequest(Request.Method.POST, url, null,
            Response.Listener { response ->
                val imgObj = Gson().fromJson(response.getString(0), GameImg::class.java)
                responseObj.image_id = imgObj.image_id
                Log.i(hereTag, "Retrieved Image Id ${imgObj.image_id}")
                gameAdded.postValue(true)
                viewModel.insert(responseObj)
            },
            Response.ErrorListener { e ->
                Log.e(hereTag, "Volley Error Details: $e")
                gameAdded.postValue(false)
            }) {
            override fun getHeaders(): Map<String, String> {
                return mHeaders
            }
            override fun getBody(): ByteArray {
                return mBody.toByteArray()
            }
        }
        GsonQueue.getInstance(context).addToRequestQueue(imgReq)
    }

    override fun onErrorCallback(errorMsg: String){

    }

}

interface CallbackLibs{
    fun onRegularCallback(responseObj: Game)

    fun onErrorCallback(errorMsg: String)
}

data class GameImg(val id: Int, val image_id: String)