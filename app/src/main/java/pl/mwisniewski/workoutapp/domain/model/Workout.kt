package pl.mwisniewski.workoutapp.domain.model

typealias Seconds = Long

data class Workout(
    val name: String,
    val description: String,
    val breakTime: Seconds,
    val exercises: List<ExerciseSet>
)

data class ExerciseSet private constructor(
    val exercise: Exercise,
    val sets: Int,
    val minRepeats: Int,
    val maxRepeats: Int
) {
    companion object {
        fun of(exercise: Exercise, sets: Int, minRepeats: Int, maxRepeats: Int): ExerciseSet {
            require(sets > 0) { "Number of sets must be > -" }
            require(minRepeats > 0) { "Number of minRepeats must be > 0" }
            require(maxRepeats > 0) { "Number of maxRepeats must be > 0" }
            require(minRepeats <= maxRepeats) { "minRepeats must be <= maxRepeats" }

            return ExerciseSet(exercise, sets, minRepeats, maxRepeats)
        }
    }

}