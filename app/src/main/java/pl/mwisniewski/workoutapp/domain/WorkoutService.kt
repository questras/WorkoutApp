package pl.mwisniewski.workoutapp.domain

import pl.mwisniewski.workoutapp.domain.model.ExerciseSet
import pl.mwisniewski.workoutapp.domain.model.Seconds
import pl.mwisniewski.workoutapp.domain.model.Workout
import pl.mwisniewski.workoutapp.domain.port.WorkoutRepository

class WorkoutService(private val workoutRepository: WorkoutRepository) {
    fun addWorkout(name: String, description: String,
                   breakTime: Seconds, exercises: List<ExerciseSet>): Workout {
        require(name.isNotBlank()) { "Name of the workout cannot be blank." }
        require(description.isNotBlank()) { "Description of the workout cannot be blank." }
        require(breakTime >= 0) { "Break time must be higher or equal to 0." }

        val workout = Workout(name, description, breakTime, exercises)

        return workoutRepository.addWorkout(workout)
    }

    fun deleteWorkout(workout: Workout) = workoutRepository.deleteWorkout(workout)

    fun getAllWorkouts(): List<Workout> = workoutRepository.getAllWorkouts()
}