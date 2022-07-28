package pl.mwisniewski.workoutapp.domain.model

typealias Seconds = Int

data class Workout(
    val name: String,
    val breakTime: Seconds,
    val exercises: List<ExerciseSet>
)

data class ExerciseSet private constructor(
    val exercise: Exercise,
    val sets: Int,
    val repeats: Int,
) {
    companion object {
        fun of(exercise: Exercise, sets: Int, repeats: Int): ExerciseSet {
            require(sets > 0) { "Number of sets must be > -" }
            require(repeats > 0) { "Number of repeats must be > 0" }

            return ExerciseSet(exercise, sets, repeats)
        }
    }

}