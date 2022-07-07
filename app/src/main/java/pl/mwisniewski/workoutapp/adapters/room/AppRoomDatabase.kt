package pl.mwisniewski.workoutapp.adapters.room

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.mwisniewski.workoutapp.adapters.room.dao.ExerciseDao
import pl.mwisniewski.workoutapp.adapters.room.dao.WorkoutDao
import pl.mwisniewski.workoutapp.adapters.room.dao.WorkoutExerciseLinkDao

@Database(
    entities = [RoomExercise::class, RoomWorkout::class, RoomWorkoutExerciseLink::class],
    version = 1
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    abstract fun workoutDao(): WorkoutDao

    abstract fun workoutExerciseLinkDao(): WorkoutExerciseLinkDao
}