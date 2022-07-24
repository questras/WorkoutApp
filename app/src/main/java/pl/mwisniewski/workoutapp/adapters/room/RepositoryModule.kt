package pl.mwisniewski.workoutapp.adapters.room

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.mwisniewski.workoutapp.domain.port.ExerciseRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindExerciseRepository(
        exerciseRepository: RoomExerciseRepository
    ): ExerciseRepository
}
