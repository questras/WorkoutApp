package pl.mwisniewski.workoutapp.domain.port

import pl.mwisniewski.workoutapp.domain.model.Exercise

interface ExerciseRepository {
    suspend fun addExercise(exercise: Exercise): Exercise

    fun deleteExercise(exercise: Exercise): Unit

    fun getAllExercises(): List<Exercise>
}