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
import kotlinx.coroutines.launch
import pl.mwisniewski.workoutapp.domain.model.Exercise
import pl.mwisniewski.workoutapp.domain.model.ExerciseSet
import pl.mwisniewski.workoutapp.domain.model.Workout
import pl.mwisniewski.workoutapp.domain.port.ExerciseRepository
import pl.mwisniewski.workoutapp.domain.port.WorkoutRepository
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
        TODO()
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
                // TODO: error handling if time
                Unit
            }
        }
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