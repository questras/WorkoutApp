package pl.mwisniewski.workoutapp.ui

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mwisniewski.workoutapp.domain.model.Category
import pl.mwisniewski.workoutapp.domain.model.Exercise
import pl.mwisniewski.workoutapp.domain.port.ExerciseRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor( // TODO: https://developer.android.com/topic/libraries/architecture/coroutines
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchExercises() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(Dispatchers.IO) {
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

    fun addExercise(addExerciseRequest: AddExerciseRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                exerciseRepository.addExercise(addExerciseRequest.toDomain())
            } catch (e: SQLiteConstraintException) {
                // TODO: error handling if time
                Unit
            }
        }
    }

    private fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch(Dispatchers.IO) {
            exerciseRepository.deleteExercise(exercise)
            fetchExercises()
            // TODO:
            //            workoutService
            //                .getAllWorkouts()
            //                .firstOrNull { workout ->
            //                    workout.exercises.any { exerciseSet -> exerciseSet.exercise.name == exercise.name }
            //                }
            //                ?.let { workout -> throw CannotDeleteExerciseException(exercise, workout) }
            //                ?: exerciseRepository.deleteExercise(exercise)
        }
    }


    fun userMessagesShown() { // TODO: maybe can be used to show errors like duplicate names.
        _uiState.update { it.copy(userMessages = listOf()) }
    }

    private fun Exercise.toUiState(): ExerciseItemUiState =
        ExerciseItemUiState(name, category.toString(), onDelete = { deleteExercise(this) })

    private fun getMessagesFromThrowable(exception: Throwable): List<UserMessage> =
        listOf() // TODO: https://developer.android.com/topic/architecture/ui-layer#show-errors
}

data class AddExerciseRequest(
    val name: String,
    val category: String,
) {
    fun toDomain(): Exercise =
        Exercise(name, Category.fromString(category))
}
