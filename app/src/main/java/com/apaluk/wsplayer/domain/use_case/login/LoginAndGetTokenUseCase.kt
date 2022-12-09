package com.apaluk.wsplayer.domain.use_case.login

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.core.util.md5Crypt
import com.apaluk.wsplayer.core.util.sha1
import com.apaluk.wsplayer.data.webshare.WebShareRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class LoginAndGetTokenUseCase @Inject constructor(
    private val webShareRepository: WebShareRepository
) {

    operator fun invoke(username: String, password: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val saltResource = webShareRepository.getSalt(username).last()
        if(saltResource is Resource.Error) {
            emit(saltResource)
            return@flow
        }
        if(saltResource.data == null) {
            emit(Resource.Error("Data is null"))
            return@flow
        }
        val passwordDigest = password.md5Crypt(salt = saltResource.data).sha1()
        val loginResource = webShareRepository.login(username, passwordDigest).last()
        emit(loginResource)
    }

}