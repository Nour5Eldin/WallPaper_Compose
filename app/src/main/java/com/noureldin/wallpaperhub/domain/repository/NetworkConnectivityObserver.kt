package com.noureldin.wallpaperhub.domain.repository

import com.noureldin.wallpaperhub.domain.model.NetworkStatus
import kotlinx.coroutines.flow.StateFlow

interface NetworkConnectivityObserver {
    val networkStatus: StateFlow<NetworkStatus>
}