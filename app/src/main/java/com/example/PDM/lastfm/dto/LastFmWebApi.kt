package isel.leic.i1920.pdm.li51n.lastfm

import com.example.PDM.dtos.GetGamesDto
import com.example.PDM.dtos.SearchDto
import isel.leic.i1920.pdm.li51n.utils.AppError

interface LastFmWebApi {
    fun searchGames(
        name: String,
        page: Int,
        onSuccess: (SearchDto) -> Unit,
        onError: (AppError) -> Unit
    )

//    fun getGames(mbid: String,
//                  page: Int,
//                  onSuccess: (GetGamesDto) -> Unit,
//                  onError: (AppError) -> Unit
//    )

}
