package com.ehan.htmlrunner.web

import android.webkit.MimeTypeMap
import android.webkit.WebResourceResponse
import androidx.webkit.WebViewAssetLoader
import com.ehan.htmlrunner.data.SafTree
import java.io.ByteArrayInputStream

/**
 * Menyajikan isi folder proyek seperti web server statis: / -> index.html, /css/app.css, dst.
 * Dipasang di root origin, jadi URL root-relative ("/style.css") juga jalan.
 */
class SafPathHandler(private val tree: SafTree) : WebViewAssetLoader.PathHandler {

    override fun handle(path: String): WebResourceResponse? {
        val clean = path.trimStart('/')
        val root = tree.rootNode()

        val found = if (clean.isEmpty()) root else tree.find(clean)
        val target = when {
            found == null -> null
            !found.isDir -> found
            found.docId == root.docId -> tree.findIndex(found) ?: tree.firstHtmlInRoot()
            else -> tree.findIndex(found)
        }
        if (target == null) return notFound(clean)
        val stream = tree.open(target) ?: return notFound(clean)

        val mime = mimeFor(target.name)
        val encoding = if (isText(mime)) "utf-8" else null
        val headers = mapOf(
            "Cache-Control" to "no-store, no-cache, must-revalidate",
            "Access-Control-Allow-Origin" to "*",
        )
        return WebResourceResponse(mime, encoding, 200, "OK", headers, stream)
    }

    private fun notFound(path: String): WebResourceResponse {
        val msg = if (path.isEmpty()) {
            "Folder ini tidak punya index.html (atau file .html lain) di root."
        } else {
            "File tidak ditemukan: " + path.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        }
        val html = "<!doctype html><meta charset=utf-8>" +
            "<meta name=viewport content='width=device-width,initial-scale=1'>" +
            "<body style='font-family:sans-serif;padding:24px'><h3>404</h3><p>$msg</p></body>"
        return WebResourceResponse(
            "text/html",
            "utf-8",
            404,
            "Not Found",
            mapOf("Cache-Control" to "no-store"),
            ByteArrayInputStream(html.toByteArray()),
        )
    }

    private fun isText(mime: String) =
        mime.startsWith("text/") || mime == "application/json" || mime.endsWith("+json") ||
            mime == "image/svg+xml" || mime == "application/javascript"

    private fun mimeFor(name: String): String {
        val ext = name.substringAfterLast('.', "").lowercase()
        return OVERRIDES[ext]
            ?: MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
            ?: "application/octet-stream"
    }

    private companion object {
        val OVERRIDES = mapOf(
            "html" to "text/html", "htm" to "text/html",
            "js" to "text/javascript", "mjs" to "text/javascript",
            "css" to "text/css", "json" to "application/json", "map" to "application/json",
            "webmanifest" to "application/manifest+json", "svg" to "image/svg+xml",
            "wasm" to "application/wasm", "txt" to "text/plain",
            "woff" to "font/woff", "woff2" to "font/woff2", "ttf" to "font/ttf", "otf" to "font/otf",
            "ico" to "image/x-icon", "webp" to "image/webp", "avif" to "image/avif",
            "mp4" to "video/mp4", "webm" to "video/webm", "mp3" to "audio/mpeg",
        )
    }
}
