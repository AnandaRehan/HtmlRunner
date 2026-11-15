@file:OptIn(ExperimentalMaterial3Api::class)

package com.ehan.htmlrunner.ui

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.ehan.htmlrunner.R
import com.ehan.htmlrunner.MainViewModel
import com.ehan.htmlrunner.data.Project
import com.ehan.htmlrunner.data.SafTree
import com.ehan.htmlrunner.data.changes
import com.ehan.htmlrunner.web.WebController
import com.ehan.htmlrunner.icons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RunnerScreen(vm: MainViewModel, projectId: String, onBack: () -> Unit) {
    val projects by vm.projects.collectAsStateWithLifecycle()
    val autoRefresh by vm.autoRefresh.collectAsStateWithLifecycle()
    val list = projects
    val project = list?.firstOrNull { it.id == projectId }

    when {
        list == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        project == null -> LaunchedEffect(Unit) { onBack() }
        else -> RunnerContent(
            project = project,
            autoRefresh = autoRefresh,
            onBack = onBack,
            onToggleAuto = vm::setAutoRefresh,
            onRelink = { uri -> vm.relink(project.id, uri) },
        )
    }
}

@Composable
private fun RunnerContent(
    project: Project,
    autoRefresh: Boolean,
    onBack: () -> Unit,
    onToggleAuto: (Boolean) -> Unit,
    onRelink: (Uri) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val tree = remember(project.folderUri) { SafTree(context, Uri.parse(project.folderUri)) }
    val controller = remember(tree) { WebController(context, tree, project.id) }
    var accessible by remember(tree) { mutableStateOf<Boolean?>(null) }

    val relinkPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) onRelink(uri)
    }

    DisposableEffect(controller) { onDispose { controller.destroy() } }

    // 1) cek folder masih bisa dibaca, 2) baru muat halaman
    LaunchedEffect(tree) { accessible = withContext(Dispatchers.IO) { tree.isAccessible() } }
    LaunchedEffect(controller, accessible) { if (accessible == true) controller.start() }

    // Refresh otomatis: hanya jalan saat layar aktif dan saklar menyala.
    LaunchedEffect(controller, accessible, autoRefresh) {
        if (accessible == true && autoRefresh) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                controller.tree.changes(initial = controller.knownSignature).collect {
                    controller.refresh()
                }
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) { controller.webView.onPause() }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { controller.webView.onResume() }
    BackHandler(enabled = controller.canGoBack) { controller.webView.goBack() }

    val statusLine = buildString {
        append(if (autoRefresh) "Auto refresh: aktif" else "Auto refresh: mati")
        controller.lastLoaded?.let { append(" • dimuat $it") }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            project.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            statusLine,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = icons.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { scope.launch { controller.goHome() } },
                        enabled = accessible == true,
                    ) { Icon(imageVector = icons.Home, contentDescription = "Ke halaman utama proyek") }
                    IconButton(
                        onClick = { scope.launch { controller.refresh() } },
                        enabled = accessible == true,
                    ) { Icon(imageVector = icons.Refresh, contentDescription = "Refresh") }
                    Text("Auto", style = MaterialTheme.typography.labelLarge)
                    Switch(
                        checked = autoRefresh,
                        onCheckedChange = onToggleAuto,
                        modifier = Modifier.padding(start = 6.dp, end = 12.dp),
                    )
                },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (accessible) {
                null -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                false -> Column(
                    modifier = Modifier.align(Alignment.Center).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Folder tidak bisa dibuka lagi", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Mungkin folder dipindah, dihapus, atau izin aksesnya dicabut.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { relinkPicker.launch(null) }) { Text("Pilih ulang folder") }
                }
                true -> {
                    AndroidView(factory = { controller.webView }, modifier = Modifier.fillMaxSize())
                    if (controller.loading) {
                        LinearProgressIndicator(Modifier.fillMaxWidth().align(Alignment.TopCenter))
                    }
                }
            }
        }
    }
}
