package com.spongycode.songquest.di

import com.spongycode.songquest.data.repository.AuthRepositoryImpl
import com.spongycode.songquest.domain.repository.AuthRepository
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
    abstract fun bindMyRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
