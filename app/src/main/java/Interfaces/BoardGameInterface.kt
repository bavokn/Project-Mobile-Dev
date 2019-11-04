package Interfaces

import BoardGames
import org.json.JSONArray

interface BoardGameInterface {

    fun onCallback(items: JSONArray): BoardGames
}