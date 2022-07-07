package pl.mwisniewski.workoutapp.adapters.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.mwisniewski.workoutapp.adapters.room.model.EXERCISE_TABLE_NAME
import pl.mwisniewski.workoutapp.adapters.room.model.RoomExercise

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM $EXERCISE_TABLE_NAME")
    fun getAll(): List<RoomExercise>

    @Insert
    fun insert(exercise: RoomExercise)

    @Delete
    fun delete(exercise: RoomExercise)
}