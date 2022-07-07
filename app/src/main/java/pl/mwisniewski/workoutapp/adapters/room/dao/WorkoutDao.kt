package pl.mwisniewski.workoutapp.adapters.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.mwisniewski.workoutapp.adapters.room.model.RoomWorkout
import pl.mwisniewski.workoutapp.adapters.room.model.WORKOUT_TABLE_NAME

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM $WORKOUT_TABLE_NAME")
    fun getAll(): List<RoomWorkout>

    @Insert
    fun insert(workout: RoomWorkout)

    @Delete
    fun delete(workout: RoomWorkout)
}