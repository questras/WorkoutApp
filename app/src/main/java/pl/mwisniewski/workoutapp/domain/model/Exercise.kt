package pl.mwisniewski.workoutapp.domain.model

data class Exercise(
    private val name: String,
    private val description: String,
    private val category: Category,
)
