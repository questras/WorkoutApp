package pl.mwisniewski.workoutapp.ui


data class ExercisesUiState(
    val exerciseItems: List<ExerciseItemUiState> = listOf(),
    val userMessage: UserMessage? = null
)

data class ExerciseItemUiState(
    val name: String,
    val category: String,
    val onDelete: () -> Unit
)
