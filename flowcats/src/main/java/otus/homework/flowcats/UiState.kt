package otus.homework.flowcats

sealed class UiState {
    data class Success(val fact: Fact? = null) : UiState()
    data class Error(val message: String) : UiState()
}