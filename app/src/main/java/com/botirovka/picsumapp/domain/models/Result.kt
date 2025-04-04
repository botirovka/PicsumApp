package com.botirovka.picsumapp.domain.models

sealed interface ApiResult {
    data object None: ApiResult
    data object Success: ApiResult
    data object NoInternetConnection: ApiResult
    data class GeneralError(val message: String = ""): ApiResult
}