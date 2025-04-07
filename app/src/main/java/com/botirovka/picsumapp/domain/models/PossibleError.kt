package com.botirovka.picsumapp.domain.models

sealed interface PossibleError {
    data object NoInternetConectionError: PossibleError
    data object EmptyImageListError: PossibleError
    data class GeneralError(val message: String = "") : PossibleError
}
