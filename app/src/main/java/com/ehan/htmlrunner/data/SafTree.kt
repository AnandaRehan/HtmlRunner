package com.ehan.htmlrunner.data

import android.content.Context
import android.net.Uri
import android.os.SystemClock
import android.provider.DocumentsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.InputStream
import java.util.Objects
import java.util.concurrent.ConcurrentHashMap

/**
 * Akses baca ke folder proyek lewat Storage Access Framework (tree URI).
 * Dipakai oleh WebView (menyajikan file) dan oleh pemantau perubahan.
 */
class SafTree(context: Context, private val treeUri: Uri) {

    private val resolver = context.applicationContext.contentResolver
    private val rootDocId: String = DocumentsContract.getTreeDocumentId(treeUri)

    data class Node(
        val docId: String,
        val name: String,
        val mime: String,
        val modified: Long,
        val size: Long,
    ) {
        val isDir: Boolean get() = mime == DocumentsContract.Document.MIME_TYPE_DIR
    }

    private class CachedDir(val loadedAt: Long, val byName: Map<String, Node>)

    private val dirCache = ConcurrentHashMap<String, CachedDir>()

    fun invalidate() = dirCache.clear()

    fun rootNode() = Node(rootDocId, "", DocumentsContract.Document.MIME_TYPE_DIR, 0L, 0L)

    /** false kalau folder dipindah/dihapus atau izin aksesnya dicabut. */
    fun isAccessible(): Boolean = try {
        val uri = DocumentsContract.buildDocumentUriUsingTree(treeUri, rootDocId)
        resolver.query(
            uri,
            arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID),
            null,
            null,
            null,
        )?.use { it.moveToFirst() } ?: false
    } catch (e: Exception) {
        false
    }

    /** Daftar isi satu folder, tanpa cache. */
    fun list(dirDocId: String): List<Node> {
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, dirDocId)
        val result = ArrayList<Node>()
        try {
            resolver.query(childrenUri, PROJECTION, null, null, null)?.use { c ->
                while (c.moveToNext()) {
                    result += Node(
                        docId = c.getString(0),
                        name = c.getString(1).orEmpty(),
                        mime = c.getString(2).orEmpty(),
                        modified = if (c.isNull(3)) 0L else c.getLong(3),
                        size = if (c.isNull(4)) 0L else c.getLong(4),
                    )
                }
            }
        } catch (e: Exception) {
            // folder tidak terbaca: anggap kosong
        }
        return result
    }

    private fun children(dirDocId: String): Map<String, Node> {
        val now = SystemClock.elapsedRealtime()
        dirCache[dirDocId]?.let { if (now - it.loadedAt < CACHE_TTL_MS) return it.byName }
        val map = LinkedHashMap<String, Node>()
        for (n in list(dirDocId)) map[n.name] = n
        dirCache[dirDocId] = CachedDir(now, map)
        return map
    }

    /** Cari file/folder dari path relatif, mis. "css/app.css". */
    fun find(relativePath: String): Node? {
        var current = rootNode()
        for (seg in relativePath.split('/')) {
            if (seg.isEmpty() || seg == ".") continue
            if (seg == "..") return null
            if (!current.isDir) return null
            val kids = children(current.docId)
            current = kids[seg]
                ?: kids.entries.firstOrNull { it.key.equals(seg, ignoreCase = true) }?.value
                ?: return null
        }
        return current
    }

    fun findIndex(dir: Node): Node? {
        val kids = children(dir.docId)
        for (candidate in INDEX_NAMES) {
            kids.entries
                .firstOrNull { !it.value.isDir && it.key.equals(candidate, ignoreCase = true) }
                ?.let { return it.value }
        }
        return null
    }

    /** Cadangan kalau root tidak punya index.html: pakai file .html pertama (urut abjad). */
    fun firstHtmlInRoot(): Node? = children(rootDocId).values
        .filter { !it.isDir && (it.name.endsWith(".html", true) || it.name.endsWith(".htm", true)) }
        .minByOrNull { it.name.lowercase() }

    fun open(node: Node): InputStream? = try {
        resolver.openInputStream(DocumentsContract.buildDocumentUriUsingTree(treeUri, node.docId))
    } catch (e: Exception) {
        null
    }

    /**
     * "Sidik jari" seluruh isi folder (nama, ukuran, waktu ubah). Kalau berubah, berarti ada file
     * yang ditambah/diubah/dihapus. Penjumlahan bersifat komutatif jadi urutan listing tidak berpengaruh.
     */
    fun signature(): Long {
        var acc = 0L
        fun walk(dirDocId: String, depth: Int) {
            for (n in list(dirDocId)) {
                if (n.isDir) {
                    if (depth < MAX_DEPTH && n.name !in IGNORED_DIRS) {
                        acc += n.docId.hashCode().toLong() * 31L
                        walk(n.docId, depth + 1)
                    }
                } else {
                    acc += Objects.hash(n.docId, n.modified, n.size).toLong()
                }
            }
        }
        walk(rootDocId, 0)
        return acc
    }

    private companion object {
        const val CACHE_TTL_MS = 1500L
        const val MAX_DEPTH = 12
        val IGNORED_DIRS = setOf(".git", "node_modules", ".idea", ".gradle")
        val INDEX_NAMES = listOf("index.html", "index.htm")
        val PROJECTION = arrayOf(
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_MIME_TYPE,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            DocumentsContract.Document.COLUMN_SIZE,
        )
    }
}

/**
 * Memancarkan Unit setiap kali isi folder berubah. Polling ringan (SAF tidak punya inotify) dan
 * menunggu sampai penyimpanan file "tenang" dulu supaya tidak reload di tengah proses simpan editor.
 *
 * @param initial sidik jari terakhir yang sudah dimuat; kalau diisi, perubahan sejak itu langsung terdeteksi.
 */
fun SafTree.changes(initial: Long? = null, intervalMs: Long = 1000L): Flow<Unit> = flow {
    var last = initial ?: signature()
    var wait = if (initial == null) intervalMs else 0L
    while (true) {
        delay(wait)
        val t0 = SystemClock.elapsedRealtime()
        var current = signature()
        val scanMs = SystemClock.elapsedRealtime() - t0
        if (current != last) {
            while (true) {
                delay(250)
                val again = signature()
                if (again == current) break
                current = again
            }
            last = current
            emit(Unit)
        }
        // folder besar -> polling otomatis diperlambat
        wait = maxOf(intervalMs, scanMs * 4)
    }
}.flowOn(Dispatchers.IO)

/** Tampilkan tree URI sebagai path yang enak dibaca, mis. /storage/emulated/0/Download/web. */
fun prettyPath(folderUri: String): String = try {
    val docId = DocumentsContract.getTreeDocumentId(Uri.parse(folderUri))
    val volume = docId.substringBefore(':')
    val path = docId.substringAfter(':', "")
    when (volume) {
        "primary" -> "/storage/emulated/0/$path".trimEnd('/')
        "raw" -> path
        else -> docId
    }
} catch (e: Exception) {
    folderUri
}
