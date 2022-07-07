package pl.mwisniewski.workoutapp.adapters.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = WORKOUT_TABLE_NAME)
data class RoomWorkout(
    @PrimaryKey val name: String,
    val description: String,
    val breakTime: Int,
)

const val WORKOUT_TABLE_NAME = "workouts"
