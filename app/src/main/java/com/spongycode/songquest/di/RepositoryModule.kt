package com.spongycode.songquest.di

import com.spongycode.songquest.data.repository.AuthRepositoryImpl
import com.spongycode.songquest.data.repository.GameplayRepositoryImpl
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.domain.repository.GameplayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindGameplayRepository(
        gameplayRepositoryImpl: GameplayRepositoryImpl
    ): GameplayRepository
}
