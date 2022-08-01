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
import pl.mwisniewski.workoutapp.domain.model.Exercise
import pl.mwisniewski.workoutapp.domain.model.ExerciseSet
import pl.mwisniewski.workoutapp.domain.model.Workout
import pl.mwisniewski.workoutapp.domain.port.ExerciseRepository
import pl.mwisniewski.workoutapp.domain.port.WorkoutRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutsUiState())
    val uiState: StateFlow<WorkoutsUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchWorkouts() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val workoutItems = workoutRepository.getAllWorkouts().map { it.toUiState() }
                _uiState.update {
                    it.copy(workoutItems = workoutItems)
                }
            } catch (ioe: IOException) {
                val message = UserMessage("Cannot read exercises")
                _uiState.update { it.copy(userMessage = message) }
            }
        }
    }

    private fun Workout.toUiState(): WorkoutItemUiState =
        WorkoutItemUiState(
            name, breakTime, exercises.map { it.toUiState() }, onDelete = { deleteWorkout(this) }
        )

    private fun ExerciseSet.toUiState(): ExerciseSetItemUiState =
        ExerciseSetItemUiState(exercise.name, sets, repeats)

    private fun deleteWorkout(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.deleteWorkout(workout)
            fetchWorkouts()
        }
    }

    fun addWorkout(addWorkoutRequest: AddWorkoutRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val exerciseByName: Map<String, Exercise> = exerciseRepository
                    .getAllExercises()
                    .associateBy(Exercise::name)

                workoutRepository.addWorkout(
                    addWorkoutRequest.toDomain(exerciseByName)
                )
            } catch (e: SQLiteConstraintException) {
                val message = UserMessage("Workout with such name already exists!")
                _uiState.update { it.copy(userMessage = message) }
            }
        }
    }

    fun userMessagesShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}

data class AddWorkoutRequest(
    val name: String,
    val breakTimeSeconds: Int,
    val exercises: List<AddWorkoutExerciseSetRequest>
) {
    fun toDomain(nameToExercise: Map<String, Exercise>): Workout =
        Workout(
            name,
            breakTimeSeconds,
            exercises.map { it.toDomain(nameToExercise[it.exerciseName]!!) }
        )
}

data class AddWorkoutExerciseSetRequest(
    val exerciseName: String,
    val sets: Int,
    val repeats: Int,
) {
    fun toDomain(domainExercise: Exercise): ExerciseSet =
        ExerciseSet.of(domainExercise, sets, repeats)
}