package com.apaluk.wsplayer.core.util

import com.apaluk.wsplayer.data.webshare.remote.dto.ResponseDto
import com.apaluk.wsplayer.data.webshare.remote.dto.StatusDto
import com.apaluk.wsplayer.data.webshare.remote.mapper.toStatusDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.simpleframework.xml.core.Persister
import retrofit2.Response
import timber.log.Timber

fun <D, R: ResponseDto> webShareRepositoryFlow(
    apiOperation: suspend () -> Response<ResponseBody>,
    dtoType: Class<out R>,
    resultMapping: suspend (R) -> D
): Flow<Resource<D>>  = flow {
    try {
        emit(Resource.Loading())
        val response = apiOperation.invoke()
        if (!response.isSuccessful) {
            emitError("Response: code=${response.code()} error=${response.errorBody()?.string()} body=${response.body()?.string()}")
            return@flow
        }
        val body = response.body()?.string() ?: run {
            emitError("Response body is null.")
            return@flow
        }
        val webshareResponse = Persister().read(dtoType, body)
        val webshareResponseStatus = webshareResponse.status.toStatusDto()
        if(webshareResponseStatus != StatusDto.OK) {
            emitError("WebShare response=$webshareResponse body=$body")
            return@flow
        }
        val result = resultMapping(webshareResponse)
        emit(Resource.Success(data = result))
    } catch (e: Exception) {
        e.throwIfCancellation()
        emit(Resource.Error("Error: ${e.message}"))
        Timber.w(e)
    }
}

suspend fun <T> FlowCollector<Resource<T>>.emitError(message: String) {
    Timber.w(message)
    emit(Resource.Error(message = message))
}