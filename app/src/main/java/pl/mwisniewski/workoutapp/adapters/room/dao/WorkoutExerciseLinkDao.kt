package pl.mwisniewski.workoutapp.adapters.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.mwisniewski.workoutapp.adapters.room.model.RoomWorkoutExerciseLink
import pl.mwisniewski.workoutapp.adapters.room.model.WORKOUT_EXERCISE_LINK_TABLE_NAME

@Dao
interface WorkoutExerciseLinkDao {
    @Query("SELECT * FROM $WORKOUT_EXERCISE_LINK_TABLE_NAME")
    fun getAll(): List<RoomWorkoutExerciseLink>

    @Insert
    fun insert(link: RoomWorkoutExerciseLink)

    @Delete
    fun delete(link: RoomWorkoutExerciseLink)
}