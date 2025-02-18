package com.example.cos30017assignment3

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameDao: GameDao){

    val allGames: Flow<List<Game>> = gameDao.getGamesByName()

    @WorkerThread
    suspend fun insert(game: Game){
        gameDao.insert(game)
    }

    @WorkerThread
    suspend fun update(game: Game){
        gameDao.updateGame(game)
    }

    @WorkerThread
    suspend fun delete(game: Game){
        gameDao.deleteGame(game)
    }

}