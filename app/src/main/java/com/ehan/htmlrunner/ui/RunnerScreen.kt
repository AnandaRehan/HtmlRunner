package com.ehan.htmlrunner.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.ehan.htmlrunner.MainViewModel
import com.ehan.htmlrunner.data.Project
import com.ehan.htmlrunner.data.SafTree
import com.ehan.htmlrunner.data.changes
import com.ehan.htmlrunner.web.WebController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RunnerScreen(vm: MainViewModel, projectId: String, onBack: () -> Unit) {
    val projects by vm.projects.collectAsStateWithLifecycle()
    val autoRefresh by vm.autoRefresh.collectAsStateWithLifecycle()
    val fullscreen by vm.fullscreen.collectAsStateWithLifecycle()
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
            fullscreen = fullscreen,
            onRelink = { uri -> vm.relink(project.id, uri) },
        )
    }
}

@Composable
private fun RunnerContent(
    project: Project,
    autoRefresh: Boolean,
    fullscreen: Boolean,
    onRelink: (Uri) -> Unit,
) {
    val context = LocalContext.current
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

    // Layar penuh bisa dihidupkan/dimatikan dari layar utama. Saat aktif, geser dari tepi
    // layar untuk memunculkan bar sistem sebentar.
    val window = remember(context) { context.findActivity()?.window }
    fun applyFullscreen(on: Boolean) {
        val w = window ?: return
        val insets = WindowCompat.getInsetsController(w, w.decorView)
        if (on) {
            insets.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            insets.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            insets.show(WindowInsetsCompat.Type.systemBars())
        }
    }
    DisposableEffect(fullscreen) {
        applyFullscreen(fullscreen)
        onDispose { applyFullscreen(false) }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) { controller.webView.onPause() }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        controller.webView.onResume()
        applyFullscreen(fullscreen)
    }

    // Tombol/gesture kembali bawaan HP: mundur di riwayat halaman dulu, baru keluar ke layar utama.
    BackHandler(enabled = controller.canGoBack) { controller.webView.goBack() }

    Surface(Modifier.fillMaxSize()) {
        // Layar penuh mati -> isi dibatasi area aman (di luar bar status, kamera depan, bar navigasi).
        val safe = if (fullscreen) Modifier else Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
        Box(Modifier.fillMaxSize().then(safe)) {
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
                    AndroidView(
                        factory = { controller.container },
                        modifier = Modifier.fillMaxSize(),
                        // tarik-untuk-refresh hanya aktif saat refresh otomatis mati
                        update = { it.isEnabled = !autoRefresh },
                    )
                    if (controller.loading) {
                        LinearProgressIndicator(Modifier.fillMaxWidth().align(Alignment.BottomCenter))
                    }
                }
            }
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
