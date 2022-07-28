package pl.mwisniewski.workoutapp.ui


data class ExercisesUiState(
    val exerciseItems: List<ExerciseItemUiState> = listOf(),
    val userMessages: List<UserMessage> = listOf()
)

data class ExerciseItemUiState(
    val name: String,
    val category: String,
    val onDelete: () -> Unit
)
