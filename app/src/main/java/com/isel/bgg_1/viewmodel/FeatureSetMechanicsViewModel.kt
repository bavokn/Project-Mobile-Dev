package com.isel.bgg_1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isel.bgg_1.boardgameatlas.BGAMechanicFeatureApi
import com.isel.bgg_1.boardgameatlas.BGAMechanicFeatureApiImpl
import com.isel.bgg_1.boardgameatlas.dto.FeatureSetsDto.ValueDTO
import com.isel.bgg_1.utils.AppError

class FeatureSetMechanicsViewModel(application: Application) : AndroidViewModel(application) {
    var games: LiveData<Array<ValueDTO>> = MutableLiveData(emptyArray())

    var error: LiveData<AppError> = MutableLiveData()

    fun searchGames(name: String, page: Int, type: String) {
        bgaFeatureSetsApi.searchValues(name,
            page,
            type,
            { (games as MutableLiveData).value = it.results.gameMatches.mechanics },
            { (error as MutableLiveData).value = it }
        )
    }

    private val bgaFeatureSetsApi: BGAMechanicFeatureApiImpl by lazy {
        BGAMechanicFeatureApiImpl(application)
    }
}