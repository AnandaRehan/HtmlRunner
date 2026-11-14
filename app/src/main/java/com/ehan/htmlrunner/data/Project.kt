package com.ehan.htmlrunner.data

/**
 * @param folderUri   URI folder (tree URI SAF) yang disimpan permanen: ini "path" folder aslinya.
 * @param folderName  nama folder asli (tidak pernah diubah aplikasi).
 * @param displayName nama yang ditampilkan di aplikasi saja, boleh diubah user.
 */
data class Project(
    val id: String,
    val folderUri: String,
    val folderName: String,
    val displayName: String,
)
