package pl.mwisniewski.workoutapp.adapters.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.mwisniewski.workoutapp.domain.model.Category
import pl.mwisniewski.workoutapp.domain.model.Exercise

@Entity(tableName = EXERCISE_TABLE_NAME)
data class RoomExercise(
    @PrimaryKey val name: String,
    val category: String,
) {
    fun toDomain(): Exercise = Exercise(name, Category.fromString(category))
}

const val EXERCISE_TABLE_NAME = "exercises"