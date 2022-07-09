package pl.mwisniewski.workoutapp.adapters.room

import pl.mwisniewski.workoutapp.adapters.room.model.RoomExercise
import pl.mwisniewski.workoutapp.adapters.room.model.RoomWorkout
import pl.mwisniewski.workoutapp.adapters.room.model.RoomWorkoutExerciseLink
import pl.mwisniewski.workoutapp.domain.model.Exercise
import pl.mwisniewski.workoutapp.domain.model.ExerciseSet
import pl.mwisniewski.workoutapp.domain.model.Workout
import pl.mwisniewski.workoutapp.domain.port.WorkoutRepository

class RoomWorkoutRepository(roomDatabase: AppRoomDatabase) : WorkoutRepository {
    private val workoutDao = roomDatabase.workoutDao()
    private val linkDao = roomDatabase.workoutExerciseLinkDao()
    private val exerciseDao = roomDatabase.exerciseDao()

    override fun addWorkout(workout: Workout): Workout {
        val roomWorkout = workout.toRoom()
        val links = workout.toLinks()

        workoutDao.insert(roomWorkout)
        linkDao.insertAll(links)

        return workout
    }

    override fun deleteWorkout(workout: Workout) {
        val roomWorkout = workout.toRoom()

        linkDao.deleteByWorkout(roomWorkout.name)
        workoutDao.delete(roomWorkout)
    }

    override fun getAllWorkouts(): List<Workout> {
        val roomWorkouts = workoutDao.getAll()
        val linksByWorkout = linkDao.getAll().groupBy(RoomWorkoutExerciseLink::workoutName)
        val exercisesByName = exerciseDao.getAll()
            .map(RoomExercise::toDomain)
            .groupBy(Exercise::name)
            .mapValues { entry -> entry.value.first() }

        return roomWorkouts.map { it.toDomain(linksByWorkout[it.name]!!, exercisesByName) }
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

private fun RoomWorkout.toDomain(links: List<RoomWorkoutExerciseLink>,
                                 exercisesByName: Map<String, Exercise>): Workout {
    val exerciseSets = links.map { link ->
        ExerciseSet.of(
            exercisesByName[link.exerciseName]!!,
            link.sets, link.minRepeats, link.maxRepeats
        )
    }
    return Workout(this.name, this.description, this.breakTime, exerciseSets)
}

private const val INSERT_LINK_ID = 0