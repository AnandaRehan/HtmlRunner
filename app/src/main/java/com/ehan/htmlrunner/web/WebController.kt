package com.ehan.htmlrunner.web

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.webkit.WebViewAssetLoader
import com.ehan.htmlrunner.data.SafTree
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Memegang WebView + state-nya. Tiap proyek punya origin sendiri (subdomain palsu) sehingga
 * localStorage / IndexedDB / cookie antar proyek tidak saling bercampur.
 */
class WebController(
    private val context: Context,
    val tree: SafTree,
    projectId: String,
) {
    var loading by mutableStateOf(false)
        private set

    var canGoBack by mutableStateOf(false)
        private set

    var lastLoaded by mutableStateOf<String?>(null)
        private set

    /** Sidik jari folder saat terakhir dimuat; dipakai pemantau supaya tidak ada perubahan yang terlewat. */
    var knownSignature: Long? = null

    private val domain = "p${projectId.replace("-", "").take(8)}.appassets.androidplatform.net"
    private val startUrl = "https://$domain/"
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")
    private var pendingScrollY = 0
    private var started = false

    private val assetLoader = WebViewAssetLoader.Builder()
        .setDomain(domain)
        .addPathHandler("/", SafPathHandler(tree))
        .build()

    private val scope = MainScope()

    val webView: WebView = buildWebView()

    /**
     * Pembungkus WebView. Tarik halaman ke bawah dari paling atas = refresh manual.
     * Dinyalakan hanya saat refresh otomatis mati (diatur dari UI lewat isEnabled).
     */
    val container: SwipeRefreshLayout = SwipeRefreshLayout(context).apply {
        addView(
            webView,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT),
        )
        setOnChildScrollUpCallback { _, _ -> webView.scrollY > 0 }
        setOnRefreshListener { scope.launch { refresh() } }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun buildWebView(): WebView = WebView(context).apply {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = false
            allowContentAccess = false
            mediaPlaybackRequiresUserGesture = false
            cacheMode = WebSettings.LOAD_NO_CACHE
            useWideViewPort = true
            loadWithOverviewMode = true
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }
        if (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            WebView.setWebContentsDebuggingEnabled(true) // chrome://inspect di build debug
        }

        webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest,
            ): WebResourceResponse? = assetLoader.shouldInterceptRequest(request.url)

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                if (uri.host == domain) return false
                return when (uri.scheme) {
                    "http", "https", "mailto", "tel", "sms", "geo" -> openExternal(uri)
                    else -> true
                }
            }

            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                loading = true
            }

            override fun onPageFinished(view: WebView, url: String?) {
                loading = false
                container.isRefreshing = false
                canGoBack = view.canGoBack()
                lastLoaded = LocalTime.now().format(timeFormat)
                if (pendingScrollY > 0) {
                    val y = pendingScrollY
                    pendingScrollY = 0
                    view.postDelayed({ view.scrollTo(0, y) }, 80)
                }
            }

            override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
                canGoBack = view.canGoBack()
            }
        }
        webChromeClient = WebChromeClient()
    }

    private fun openExternal(uri: Uri): Boolean {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (e: ActivityNotFoundException) {
            // tidak ada aplikasi yang bisa membuka
        }
        return true
    }

    suspend fun start() {
        if (started) return
        started = true
        knownSignature = withContext(Dispatchers.IO) { tree.signature() }
        webView.loadUrl(startUrl)
    }

    /** Muat ulang halaman yang sedang dibuka (posisi scroll dipertahankan). */
    suspend fun refresh() {
        knownSignature = withContext(Dispatchers.IO) {
            tree.invalidate()
            tree.signature()
        }
        pendingScrollY = webView.scrollY
        if (webView.url == null) webView.loadUrl(startUrl) else webView.reload()
    }

    /** Kembali ke halaman utama proyek (index.html di root). */
    suspend fun goHome() {
        knownSignature = withContext(Dispatchers.IO) {
            tree.invalidate()
            tree.signature()
        }
        pendingScrollY = 0
        webView.loadUrl(startUrl)
    }

    fun destroy() {
        scope.cancel()
        webView.stopLoading()
        (container.parent as? ViewGroup)?.removeView(container)
        container.removeAllViews()
        webView.destroy()
    }
}
