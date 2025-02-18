package com.example.cos30017assignment3

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    val allGames: LiveData<List<Game>> = repository.allGames.asLiveData()

    fun insert(game: Game) = viewModelScope.launch {
        repository.insert(game)
    }

    fun changeComplete(game: Game) = viewModelScope.launch {
        game.complete = !game.complete
        repository.update(game)
    }

    fun delete(game: Game) = viewModelScope.launch {
        repository.delete(game)
    }

}

class GameViewModelFactory(private val repository: GameRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(GameViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel Class")
    }
}