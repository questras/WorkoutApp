package pl.mwisniewski.workoutapp.adapters.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = EXERCISE_TABLE_NAME)
data class RoomExercise(
    @PrimaryKey val name: String,
    val description: String,
    val category: String,
)

const val EXERCISE_TABLE_NAME = "exercises"