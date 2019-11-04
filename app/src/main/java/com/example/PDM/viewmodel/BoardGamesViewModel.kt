package isel.leic.i1920.pdm.li51n.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.PDM.dtos.GameDTO
import isel.leic.i1920.pdm.li51n.lastfm.LastFmWebApi
import isel.leic.i1920.pdm.li51n.utils.AppError
import org.geniuz.lastfm.LastfmWebApiImpl

class BoardGamesViewModel(application: Application) : AndroidViewModel(application) {
    var games : LiveData<Array<GameDTO>> = MutableLiveData(emptyArray())

    var error : LiveData<AppError> = MutableLiveData()


    fun searchGames(name: String, page: Int) : Unit {
        lastfm.searchGames(name,
            page,
            { (games as MutableLiveData).value = it.results.gameMatches.games },
            { (error as MutableLiveData).value  = it }
        )
    }

    val lastfm : LastFmWebApi by lazy {
        LastfmWebApiImpl(application)
    }
}