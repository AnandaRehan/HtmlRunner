package com.ehan.htmlrunner.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "html_runner")

class ProjectStore(context: Context) {

    private val store = context.applicationContext.dataStore
    private val projectsKey = stringPreferencesKey("projects_json")
    private val autoRefreshKey = booleanPreferencesKey("auto_refresh")

    val projects: Flow<List<Project>> = store.data.map { decode(it[projectsKey]) }

    /** Pengaturan global refresh otomatis (default: aktif). */
    val autoRefresh: Flow<Boolean> = store.data.map { it[autoRefreshKey] ?: true }

    suspend fun update(transform: (List<Project>) -> List<Project>) {
        store.edit { prefs ->
            prefs[projectsKey] = encode(transform(decode(prefs[projectsKey])))
        }
    }

    suspend fun setAutoRefresh(enabled: Boolean) {
        store.edit { it[autoRefreshKey] = enabled }
    }

    private fun decode(json: String?): List<Project> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            val arr = JSONArray(json)
            List(arr.length()) { i ->
                val o = arr.getJSONObject(i)
                val folder = o.getString("folder")
                Project(
                    id = o.getString("id"),
                    folderUri = o.getString("uri"),
                    folderName = folder,
                    displayName = o.optString("name", folder),
                )
            }
        } catch (e: JSONException) {
            emptyList()
        }
    }

    private fun encode(list: List<Project>): String {
        val arr = JSONArray()
        list.forEach {
            arr.put(
                JSONObject()
                    .put("id", it.id)
                    .put("uri", it.folderUri)
                    .put("folder", it.folderName)
                    .put("name", it.displayName),
            )
        }
        return arr.toString()
    }
}
