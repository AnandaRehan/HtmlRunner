package com.ehan.htmlrunner

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ehan.htmlrunner.data.Project
import com.ehan.htmlrunner.data.ProjectStore
import com.ehan.htmlrunner.data.SafTree
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val store = ProjectStore(app)

    /** null = masih memuat dari penyimpanan. */
    val projects: StateFlow<List<Project>?> =
        store.projects.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val autoRefresh: StateFlow<Boolean> =
        store.autoRefresh.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 8)
    val events: SharedFlow<String> = _events

    fun addFolder(uri: Uri) {
        viewModelScope.launch {
            val app = getApplication<Application>()
            if (projects.value.orEmpty().any { it.folderUri == uri.toString() }) {
                _events.tryEmit("Folder ini sudah ada di daftar")
                return@launch
            }
            if (!takeRead(uri)) {
                _events.tryEmit("Izin folder tidak bisa disimpan")
                return@launch
            }
            val name = DocumentFile.fromTreeUri(app, uri)?.name ?: "Proyek"
            store.update { it + Project(UUID.randomUUID().toString(), uri.toString(), name, name) }

            val hasHtml = withContext(Dispatchers.IO) {
                val tree = SafTree(app, uri)
                tree.findIndex(tree.rootNode()) != null || tree.firstHtmlInRoot() != null
            }
            if (!hasHtml) _events.tryEmit("Ditambahkan, tapi tidak ada file .html di root folder")
        }
    }

    /** Hanya mengubah nama tampilan di aplikasi, folder aslinya tidak disentuh. */
    fun rename(id: String, newName: String) {
        viewModelScope.launch {
            store.update { list ->
                list.map { p ->
                    if (p.id == id) p.copy(displayName = newName.trim().ifEmpty { p.folderName }) else p
                }
            }
        }
    }

    /** Hapus dari daftar aplikasi saja; folder asli tidak dihapus. */
    fun remove(id: String) {
        viewModelScope.launch {
            val target = projects.value?.firstOrNull { it.id == id } ?: return@launch
            store.update { list -> list.filterNot { it.id == id } }
            releaseRead(Uri.parse(target.folderUri))
        }
    }

    /** Pilih ulang folder kalau folder lama dipindah/izinnya hilang. */
    fun relink(id: String, uri: Uri) {
        viewModelScope.launch {
            val app = getApplication<Application>()
            val old = projects.value?.firstOrNull { it.id == id } ?: return@launch
            if (!takeRead(uri)) {
                _events.tryEmit("Izin folder tidak bisa disimpan")
                return@launch
            }
            val name = DocumentFile.fromTreeUri(app, uri)?.name ?: old.folderName
            store.update { list ->
                list.map { p -> if (p.id == id) p.copy(folderUri = uri.toString(), folderName = name) else p }
            }
            if (old.folderUri != uri.toString()) releaseRead(Uri.parse(old.folderUri))
        }
    }

    fun setAutoRefresh(enabled: Boolean) {
        viewModelScope.launch { store.setAutoRefresh(enabled) }
    }

    private fun takeRead(uri: Uri): Boolean = try {
        getApplication<Application>().contentResolver
            .takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        true
    } catch (e: SecurityException) {
        false
    }

    private fun releaseRead(uri: Uri) {
        try {
            getApplication<Application>().contentResolver
                .releasePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } catch (e: SecurityException) {
            // sudah tidak dipegang
        }
    }
}
