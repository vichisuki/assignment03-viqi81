package com.example.cos30017assignment3

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GamesApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { GameRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { GameRepository(database.gameDao()) }


}