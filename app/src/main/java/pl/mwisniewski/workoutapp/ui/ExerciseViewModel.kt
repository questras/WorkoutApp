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
import pl.mwisniewski.workoutapp.domain.port.WorkoutRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val workoutRepository: WorkoutRepository,
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
                val message = UserMessage("Cannot read exercises")
                _uiState.update { it.copy(userMessage = message) }
            }
        }
    }

    fun addExercise(addExerciseRequest: AddExerciseRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                exerciseRepository.addExercise(addExerciseRequest.toDomain())
                val message = UserMessage("Exercise created!")
                _uiState.update { it.copy(userMessage = message) }
            } catch (e: SQLiteConstraintException) {
                val message = UserMessage("Exercise with such name already exists!")
                _uiState.update { it.copy(userMessage = message) }
            }
        }
    }

    private fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository
                .getAllWorkouts()
                .firstOrNull { workout ->
                    workout.exercises.any { exerciseSet -> exerciseSet.exercise.name == exercise.name }
                }
                ?.let {
                    val message = UserMessage("Cannot delete exercise used by workout.")
                    _uiState.update { it.copy(userMessage = message) }
                }
                ?: exerciseRepository.deleteExercise(exercise)
            fetchExercises()
        }
    }

    private fun Exercise.toUiState(): ExerciseItemUiState =
        ExerciseItemUiState(name, category.toString(), onDelete = { deleteExercise(this) })

    fun userMessagesShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}

data class AddExerciseRequest(
    val name: String,
    val category: String,
) {
    fun toDomain(): Exercise =
        Exercise(name, Category.fromString(category))
}
