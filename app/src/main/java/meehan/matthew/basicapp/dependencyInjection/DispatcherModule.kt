package meehan.matthew.basicapp.dependencyInjection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {
    @Provides
    @IoDispatcher
    fun provideIoDispatcher() = Dispatchers.IO
}