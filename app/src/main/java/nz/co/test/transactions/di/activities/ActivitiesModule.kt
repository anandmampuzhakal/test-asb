package nz.co.test.transactions.di.activities

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import nz.co.test.transactions.activities.MainActivity

@Module
@InstallIn(SingletonComponent::class)
class ActivitiesModule {

    @Provides
    @IntoMap
    @ActivityClassKey(MainActivity::class)
    fun providesMainActivity(): Activity = MainActivity()
}