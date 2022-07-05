package pl.mwisniewski.workoutapp.domain.port

import pl.mwisniewski.workoutapp.domain.model.Workout

interface WorkoutRepository {
    fun addWorkout(workout: Workout): Workout

    fun deleteWorkout(workout: Workout): Unit

    fun getAllWorkouts(): List<Workout>
}