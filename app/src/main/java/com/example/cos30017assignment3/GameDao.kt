package com.example.cos30017assignment3

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//Provides the queries available to the user.
@Dao
interface GameDao {

    @Query("SELECT * FROM user_games ORDER BY name ASC")
    fun getGamesByName(): Flow<List<Game>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game)

    @Query("DELETE FROM user_games")
    suspend fun deleteAll()

    @Update
    fun updateGame(vararg games: Game)

    @Query("SELECT * FROM user_games WHERE complete = 1")
    fun getCompleteGames(): Flow<List<Game>>

    @Delete
    suspend fun deleteGame(game: Game)

}