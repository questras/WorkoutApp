package pl.mwisniewski.workoutapp.adapters.room

import pl.mwisniewski.workoutapp.adapters.room.dao.WorkoutExerciseLinkDao
import pl.mwisniewski.workoutapp.adapters.room.model.RoomExercise
import pl.mwisniewski.workoutapp.adapters.room.model.RoomWorkout
import pl.mwisniewski.workoutapp.adapters.room.model.RoomWorkoutExerciseLink
import pl.mwisniewski.workoutapp.domain.model.Category
import pl.mwisniewski.workoutapp.domain.model.Exercise
import pl.mwisniewski.workoutapp.domain.model.Workout
import pl.mwisniewski.workoutapp.domain.port.ExerciseRepository
import pl.mwisniewski.workoutapp.domain.port.WorkoutRepository

class RoomWorkoutRepository(roomDatabase: AppRoomDatabase) : WorkoutRepository {
    private val dao = roomDatabase.workoutDao()
    private val linkDao = roomDatabase.workoutExerciseLinkDao()

    override fun addWorkout(workout: Workout): Workout {
        TODO("Not yet implemented")
    }

    override fun deleteWorkout(workout: Workout) {
        TODO("Not yet implemented")
    }

    override fun getAllWorkouts(): List<Workout> {
        TODO("Not yet implemented")
    }
}

private fun Workout.toRoom(): RoomWorkout =
    RoomWorkout(name, description, breakTime)

private fun Workout.toLinks(): List<RoomWorkoutExerciseLink> =
    exercises.map { set ->
        RoomWorkoutExerciseLink(
            INSERT_LINK_ID,
            set.exercise.name,
            this.name,
            set.sets,
            set.minRepeats,
            set.maxRepeats
        )
    }

private fun toDomain(roomWorkout: RoomWorkout,
                     roomWorkoutExerciseLinks: List<RoomWorkoutExerciseLink>): Workout =
    Workout(
        roomWorkout.name, roomWorkout.description, roomWorkout.breakTime,
        TODO()
    )

private const val INSERT_LINK_ID = 0