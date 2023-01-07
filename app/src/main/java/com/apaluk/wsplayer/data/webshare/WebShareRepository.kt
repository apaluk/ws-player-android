package com.apaluk.wsplayer.data.webshare

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.core.util.webShareRepositoryFlow
import com.apaluk.wsplayer.data.webshare.remote.WebShareApi
import com.apaluk.wsplayer.data.webshare.remote.dto.FileLinkDto
import com.apaluk.wsplayer.data.webshare.remote.dto.LoginDto
import com.apaluk.wsplayer.data.webshare.remote.dto.SaltDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebShareRepository @Inject constructor(
    private val webShareApi: WebShareApi
) {

    fun getSalt(username: String): Flow<Resource<String>> = webShareRepositoryFlow(
        apiOperation = { webShareApi.salt(username) },
        dtoType = SaltDto::class.java,
        resultMapping = { it.salt }
    )

    fun login(username: String, passwordDigest: String) = webShareRepositoryFlow(
        apiOperation = { webShareApi.login(username, passwordDigest) },
        dtoType = LoginDto::class.java,
        resultMapping = { it.token }
    )

    fun getFileLink(webShareToken: String, fileIdent: String) = webShareRepositoryFlow(
        apiOperation = { webShareApi.getFileLink(webShareToken, fileIdent)},
        dtoType = FileLinkDto::class.java,
        resultMapping = { it.link }
    )
}