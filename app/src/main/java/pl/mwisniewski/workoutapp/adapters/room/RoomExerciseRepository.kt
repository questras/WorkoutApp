package pl.mwisniewski.workoutapp.adapters.room

import pl.mwisniewski.workoutapp.adapters.room.dao.ExerciseDao
import pl.mwisniewski.workoutapp.adapters.room.model.RoomExercise
import pl.mwisniewski.workoutapp.domain.model.Exercise
import pl.mwisniewski.workoutapp.domain.port.ExerciseRepository
import javax.inject.Inject

// TODO: https://developer.android.com/guide/background
// TODO: https://developer.android.com/training/data-storage/room/async-queries
class RoomExerciseRepository @Inject constructor(
    private val dao: ExerciseDao
) : ExerciseRepository {
    override suspend fun addExercise(exercise: Exercise): Exercise =
        dao.insert(exercise.toRoom()).let { exercise }

    override suspend fun deleteExercise(exercise: Exercise) =
        dao.delete(exercise.toRoom())

    override suspend fun getAllExercises(): List<Exercise> =
        dao.getAll().map(RoomExercise::toDomain)
}

private fun Exercise.toRoom(): RoomExercise =
    RoomExercise(name, category.toString())
