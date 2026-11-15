@file:OptIn(ExperimentalMaterial3Api::class)

package com.ehan.htmlrunner.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ehan.htmlrunner.MainViewModel
import com.ehan.htmlrunner.data.Project
import com.ehan.htmlrunner.data.prettyPath
import com.ehan.htmlrunner.ui.theme.Icons

@Composable
fun HomeScreen(vm: MainViewModel, onOpen: (String) -> Unit) {
    val projects by vm.projects.collectAsStateWithLifecycle()
    val autoRefresh by vm.autoRefresh.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var renaming by remember { mutableStateOf<Project?>(null) }
    var deleting by remember { mutableStateOf<Project?>(null) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) vm.addFolder(uri)
    }

    LaunchedEffect(Unit) { vm.events.collect { snackbar.showSnackbar(it) } }

    Scaffold(
        topBar = { TopAppBar(title = { Text("HTML Runner") }) },
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { picker.launch(null) },
                icon = { Icon(imageVector = Icons.Add, contentDescription = null) },
                text = { Text("Tambah proyek") },
            )
        },
    ) { padding ->
        val list = projects
        if (list == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item(key = "auto") {
                    AutoRefreshCard(checked = autoRefresh, onChange = vm::setAutoRefresh)
                }
                if (list.isEmpty()) {
                    item(key = "empty") { EmptyState() }
                } else {
                    items(list, key = { it.id }) { project ->
                        ProjectCard(
                            project = project,
                            onOpen = { onOpen(project.id) },
                            onRename = { renaming = project },
                            onDelete = { deleting = project },
                        )
                    }
                }
            }
        }
    }

    renaming?.let { p ->
        RenameDialog(
            project = p,
            onDismiss = { renaming = null },
            onConfirm = { vm.rename(p.id, it); renaming = null },
        )
    }
    deleting?.let { p ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Hapus dari daftar?") },
            text = { Text("“${p.displayName}” hanya dihapus dari aplikasi ini. Folder aslinya di penyimpanan tidak dihapus.") },
            confirmButton = {
                TextButton(onClick = { vm.remove(p.id); deleting = null }) { Text("Hapus") }
            },
            dismissButton = { TextButton(onClick = { deleting = null }) { Text("Batal") } },
        )
    }
}

@Composable
private fun AutoRefreshCard(checked: Boolean, onChange: (Boolean) -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Refresh otomatis", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Muat ulang halaman otomatis saat file di folder asli berubah.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.width(12.dp))
            Switch(checked = checked, onCheckedChange = onChange)
        }
    }
}

@Composable
private fun EmptyState() {
    Text(
        "Belum ada proyek.\nKetuk “Tambah proyek”, lalu pilih folder yang berisi index.html.",
        modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun ProjectCard(
    project: Project,
    onOpen: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit,
) {
    var menu by remember { mutableStateOf(false) }
    val path = remember(project.folderUri) { prettyPath(project.folderUri) }

    Card(onClick = onOpen, modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(imageVector = Icons.PlayArrow, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    project.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    path,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Box {
                IconButton(onClick = { menu = true }) {
                    Icon(imageVector = Icons.MoreVert, contentDescription = "Menu")
                }
                DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                    DropdownMenuItem(
                        text = { Text("Ubah nama") },
                        leadingIcon = { Icon(imageVector = Icons.Edit, contentDescription = null) },
                        onClick = { menu = false; onRename() },
                    )
                    DropdownMenuItem(
                        text = { Text("Hapus dari daftar") },
                        leadingIcon = { Icon(imageVector = Icons.Delete, contentDescription = null) },
                        onClick = { menu = false; onDelete() },
                    )
                }
            }
        }
    }
}

@Composable
private fun RenameDialog(project: Project, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var text by remember(project.id) { mutableStateOf(project.displayName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ubah nama proyek") },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    label = { Text("Nama di aplikasi") },
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Hanya mengubah tampilan di aplikasi. Nama folder aslinya tidak berubah.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        confirmButton = { TextButton(onClick = { onConfirm(text) }) { Text("Simpan") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } },
    )
}
