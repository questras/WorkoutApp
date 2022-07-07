package pl.mwisniewski.workoutapp.adapters.room

import pl.mwisniewski.workoutapp.adapters.room.model.RoomExercise
import pl.mwisniewski.workoutapp.domain.model.Category
import pl.mwisniewski.workoutapp.domain.model.Exercise
import pl.mwisniewski.workoutapp.domain.port.ExerciseRepository

class RoomExerciseRepository(roomDatabase: AppRoomDatabase) : ExerciseRepository {
    private val dao = roomDatabase.exerciseDao()

    override fun addExercise(exercise: Exercise): Exercise =
        dao.insert(exercise.toRoom()).let { exercise }

    override fun deleteExercise(exercise: Exercise) =
        dao.delete(exercise.toRoom())

    override fun getAllExercises(): List<Exercise> =
        dao.getAll().map(RoomExercise::toDomain)
}

private fun Exercise.toRoom(): RoomExercise =
    RoomExercise(name, description, category.toString())

private fun RoomExercise.toDomain(): Exercise =
    Exercise(name, description, Category.valueOf(category))