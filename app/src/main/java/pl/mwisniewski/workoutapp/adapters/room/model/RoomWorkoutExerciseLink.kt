package pl.mwisniewski.workoutapp.adapters.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = WORKOUT_EXERCISE_LINK_TABLE_NAME)
data class RoomWorkoutExerciseLink(
    // Insert methods treat 0 as not-set while inserting the item.
    @PrimaryKey(autoGenerate = true) val id: Int,
    val exerciseName: String,
    val workoutName: String,
    val sets: Int,
    val minRepeats: Int,
    val maxRepeats: Int,
)

const val WORKOUT_EXERCISE_LINK_TABLE_NAME = "workout_exercise_link"
