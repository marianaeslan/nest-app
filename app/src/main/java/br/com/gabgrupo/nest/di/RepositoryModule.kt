package br.com.gabgrupo.nest.di

import br.com.gabgrupo.nest.data.repository.AuthRepository
import br.com.gabgrupo.nest.data.repository.AuthRepositoryImpl
import br.com.gabgrupo.nest.data.repository.DashboardRepository
import br.com.gabgrupo.nest.data.repository.DashboardRepositoryImpl
import br.com.gabgrupo.nest.data.repository.GuidelineRepository
import br.com.gabgrupo.nest.data.repository.GuidelineRepositoryImpl
import br.com.gabgrupo.nest.data.repository.IdeaRepository
import br.com.gabgrupo.nest.data.repository.IdeaRepositoryImpl
import br.com.gabgrupo.nest.data.repository.ProjectRepository
import br.com.gabgrupo.nest.data.repository.ProjectRepositoryImpl
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
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindGuidelineRepository(impl: GuidelineRepositoryImpl): GuidelineRepository

    @Binds
    @Singleton
    abstract fun bindIdeaRepository(impl: IdeaRepositoryImpl): IdeaRepository

    @Binds
    @Singleton
    abstract fun bindProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}