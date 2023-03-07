package com.apaluk.wsplayer.domain.use_case.webshare

import com.apaluk.wsplayer.core.login.LoginManager
import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.data.webshare.WebShareRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFileLinkUseCase @Inject constructor(
    private val loginManager: LoginManager,
    private val webShareRepository: WebShareRepository
) {

    suspend operator fun invoke(fileIdent: String): Flow<Resource<String>> =
        webShareRepository.getFileLink(loginManager.getWebShareToken(), fileIdent)

}