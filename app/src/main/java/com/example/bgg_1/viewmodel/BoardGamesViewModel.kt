package com.example.bgg_1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bgg_1.boardgameatlas.BoardGameAtlasApi
import com.example.bgg_1.boardgameatlas.BoardGameAtlasApiImpl
import com.example.bgg_1.boardgameatlas.dto.GameDTO
import com.example.bgg_1.utils.AppError

class BoardGamesViewModel(application: Application) : AndroidViewModel(application) {
    var games: LiveData<Array<GameDTO>> = MutableLiveData(emptyArray())

    var error: LiveData<AppError> = MutableLiveData()

    fun searchGames(name: String, page: Int, type: String) {
        boardGameAtlasApi.searchGames(name,
            page,
            type,
            { (games as MutableLiveData).value = it.results.gameMatches.games },
            { (error as MutableLiveData).value = it }
        )
    }

    private val boardGameAtlasApi: BoardGameAtlasApi by lazy {
        BoardGameAtlasApiImpl(application)
    }
}