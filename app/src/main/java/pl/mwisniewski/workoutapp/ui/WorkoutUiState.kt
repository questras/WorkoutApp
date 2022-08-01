package pl.mwisniewski.workoutapp.ui

data class WorkoutsUiState(
    val workoutItems: List<WorkoutItemUiState> = listOf(),
    val userMessage: UserMessage? = null,
)

data class WorkoutItemUiState(
    val name: String,
    val breakTime: Int,
    val exerciseSets: List<ExerciseSetItemUiState>,
    val onDelete: () -> Unit
)

data class ExerciseSetItemUiState(
    val exerciseName: String,
    val sets: Int,
    val repeats: Int,
)
