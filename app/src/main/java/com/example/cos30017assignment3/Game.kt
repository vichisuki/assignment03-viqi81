package com.example.cos30017assignment3

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_games")
data class Game(

    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "summary") val summary: String?,
    @ColumnInfo(name = "rating") var rating: Float = 0f,
    @ColumnInfo(name = "release") val first_release_date: Long,
    @ColumnInfo(name = "complete") var complete: Boolean = false,
    @ColumnInfo(name = "image_id") var image_id: String,
    @ColumnInfo(name = "cover") val cover: Int,
    @ColumnInfo(name = "rating_count") val rating_count: Int = 0

)
