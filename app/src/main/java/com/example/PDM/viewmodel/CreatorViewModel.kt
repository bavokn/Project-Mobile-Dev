package isel.leic.i1920.pdm.li51n.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.PDM.dtos.GameDTO
import com.example.PDM.lastfm.LastFmWebApi
import com.example.PDM.lastfm.LastfmWebApiImpl
import isel.leic.i1920.pdm.li51n.utils.AppError


class CreatorViewModel(application: Application) : AndroidViewModel(application) {
    var creators : LiveData<Array<GameDTO>> = MutableLiveData(emptyArray())

    var error : LiveData<AppError> = MutableLiveData()

    fun searchGames(name: String, page: Int, type: String) {
        lastfm.searchGames(name,
            page,
            type,
            { (creators as MutableLiveData).value = it.results.gameMatches.games },
            { (error as MutableLiveData).value  = it }
        )
    }

    val lastfm : LastFmWebApi by lazy {
        LastfmWebApiImpl(application)
    }
}