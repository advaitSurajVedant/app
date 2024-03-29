package com.advaitvedant.sync.manager

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.advaitvedant.data.utils.SyncManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject

internal const val SyncWorkName = "SyncWorkName"
class WorkManagerSyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
) : SyncManager {

    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(SyncWorkName)
            .map(MutableList<WorkInfo>::anyRunning)
            .asFlow()
            .conflate()
}

private val List<WorkInfo>.anyRunning get() = any { it.state == WorkInfo.State.RUNNING }