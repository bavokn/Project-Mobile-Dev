package Interfaces

import org.json.JSONArray

interface BoardGameInterface {

    fun onCallback(items: JSONArray)
}