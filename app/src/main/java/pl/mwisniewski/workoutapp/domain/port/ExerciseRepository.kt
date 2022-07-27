package pl.mwisniewski.workoutapp.domain.port

import pl.mwisniewski.workoutapp.domain.model.Exercise

interface ExerciseRepository {
    suspend fun addExercise(exercise: Exercise): Exercise

    suspend fun deleteExercise(exercise: Exercise): Unit

    suspend fun getAllExercises(): List<Exercise>
}