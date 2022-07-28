package pl.mwisniewski.workoutapp.adapters.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Singleton
    @Provides
    fun provideAppRoomDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app, AppRoomDatabase::class.java, "AppRoomDatabase"
        ).build()

    @Singleton
    @Provides
    fun provideExerciseDao(db: AppRoomDatabase) = db.exerciseDao()

    @Singleton
    @Provides
    fun provideWorkoutDao(db: AppRoomDatabase) = db.workoutDao()

    @Singleton
    @Provides
    fun provideWorkoutExerciseLinkDao(db: AppRoomDatabase) = db.workoutExerciseLinkDao()
}