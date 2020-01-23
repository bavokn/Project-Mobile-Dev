package com.isel.bgg_1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isel.bgg_1.boardgameatlas.BGADesignerFeatureApiImpl
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.DesignerDTO
import com.isel.bgg_1.utils.AppError

class FeatureSetDesignersViewModel(application: Application) : AndroidViewModel(application) {
    var games: LiveData<Array<DesignerDTO>> = MutableLiveData(emptyArray())

    var error: LiveData<AppError> = MutableLiveData()

    fun searchGames(name: String, page: Int, type: String) {
        bgaFeatureSetsApi.searchGames(name,
            page,
            type,
            { (games as MutableLiveData).value = it.results.gameMatches.games },
            { (error as MutableLiveData).value = it }
        )
    }

    private val bgaFeatureSetsApi: BGADesignerFeatureApiImpl by lazy {
        BGADesignerFeatureApiImpl(application)
    }
}