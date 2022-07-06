package pl.mwisniewski.workoutapp.domain.model

data class Exercise(
    val name: String,
    val description: String,
    val category: Category,
)
