package pl.mwisniewski.workoutapp.domain

import pl.mwisniewski.workoutapp.domain.model.Category
import pl.mwisniewski.workoutapp.domain.model.Exercise
import pl.mwisniewski.workoutapp.domain.port.ExerciseRepository

class ExerciseService(private val exerciseRepository: ExerciseRepository) {
    fun addExercise(name: String, description: String, category: String): Exercise {
        require(name.isNotBlank()) { "Name of the exercise cannot be blank." }
        require(description.isNotBlank()) { "Description of the exercise cannot be blank." }
        val domainCategory = Category.valueOf(category)

        val exercise = Exercise(name, description, domainCategory)
        return exerciseRepository.addExercise(exercise)
    }

    fun deleteExercise(exercise: Exercise): Unit = exerciseRepository.deleteExercise(exercise)

    fun getAllExercises(): List<Exercise> = exerciseRepository.getAllExercises()
}