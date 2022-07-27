package pl.mwisniewski.workoutapp.ui


data class ExercisesUiState(
    val exerciseItems: List<ExerciseItemUiState> = listOf(),
    val userMessages: List<Message> = listOf()
)

data class Message(val message: String)

data class ExerciseItemUiState(
    val name: String,
    val category: String,
    val onDelete: () -> Unit
)
