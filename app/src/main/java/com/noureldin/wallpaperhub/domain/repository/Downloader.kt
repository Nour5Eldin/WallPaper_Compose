package com.noureldin.wallpaperhub.domain.repository

interface Downloader {
    fun downloadFile(url: String, fileName: String?)
}