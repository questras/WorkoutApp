package pl.mwisniewski.workoutapp.ui

import pl.mwisniewski.workoutapp.domain.model.Category
import pl.mwisniewski.workoutapp.domain.model.Exercise


data class ExercisesUiState(
    val exerciseItems: List<ExerciseItemUiState> = listOf(),
    val userMessages: List<Message> = listOf()
)

data class Message(val message: String)

data class ExerciseItemUiState(
    val name: String,
    val description: String,
    val category: String,
    val onDelete: () -> Unit
)
