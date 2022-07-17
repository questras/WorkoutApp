package pl.mwisniewski.workoutapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mwisniewski.workoutapp.domain.model.Exercise
import pl.mwisniewski.workoutapp.domain.port.ExerciseRepository
import java.io.IOException

class ExerciseViewModel(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchExercises() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {  // tODO: https://developer.android.com/kotlin/coroutines
            try {
                val exerciseItems = exerciseRepository.getAllExercises().map { it.toUiState() }
                _uiState.update {
                    it.copy(exerciseItems = exerciseItems)
                }
            } catch (ioe: IOException) {
                val messages = getMessagesFromThrowable(ioe)
                _uiState.update {
                    it.copy(userMessages = messages)
                }
            }
        }
    }

    fun userMessagesShown() {
        _uiState.update { it.copy(userMessages = listOf()) }
    }

    private fun Exercise.toUiState(): ExerciseItemUiState =
        ExerciseItemUiState(name, description, category.toString(),
            onDelete = { exerciseRepository.deleteExercise(this) })

    private fun getMessagesFromThrowable(exception: Throwable): List<Message> =
        listOf() // TODO: https://developer.android.com/topic/architecture/ui-layer#show-errors
}
