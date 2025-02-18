package com.example.cos30017assignment3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    //creates an instance of the repository/database in order to be used
    private val gameViewModel: GameViewModel by viewModels {
        GameViewModelFactory((application as GamesApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)

        val gamesRecycler = findViewById<RecyclerView>(R.id.gameList)
        val adapter = GameAdapter(gameViewModel)
        gamesRecycler.adapter = adapter
        gamesRecycler.layoutManager = LinearLayoutManager(this)

        gameViewModel.allGames.observe(this, Observer { games ->
            games?.let {adapter.submitList((it))}
        })

        val gsonGameConstructor = AddGamesGson(this, gameViewModel)
        gsonGameConstructor.addGame("Persona 5 Royal", 4.0f, true)
        gsonGameConstructor.addGame("AI: The Somnium Files", 3.0f, false)

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult -> if (result.resultCode == RESULT_OK) {
                val data = result.data
                val name = data?.getStringExtra("gameName")
                val rating = data?.getIntExtra("userRating", 0)
                val complete = data?.getBooleanExtra("completed", false)
                if (name != null && rating != null && complete != null) {
                    gsonGameConstructor.addGame(name, rating.toFloat(), complete)
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Adding game cancelled.", Toast.LENGTH_SHORT).show()
            }
        }

        fab.setOnClickListener {
            val mIntent = Intent(this, AddGameActivity::class.java)
            startForResult.launch(mIntent)
        }

    }

}