package com.ezdream.color.di

import android.content.Context
import com.ezdream.color.BuildConfig.API_KEY
import com.ezdream.color.BuildConfig.API_URL
import com.ezdream.color.util.ApiKey
import com.ezdream.color.util.ApiUrl
import com.ezdream.color.util.DeviceId
import com.ezdream.color.util.DeviceModel
import com.ezdream.color.util.DeviceUtil
import com.ezdream.color.util.GsonProvider
import com.ezdream.color.util.SoftwareVersion
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @DeviceId
    @Provides
    fun provideDeviceId(@ApplicationContext context: Context) = DeviceUtil.getDeviceId(context)

    @DeviceModel
    @Provides
    fun provideDeviceModel() = DeviceUtil.getDeviceModel()

    @SoftwareVersion
    @Provides
    fun provideSoftwareVersion(@ApplicationContext context: Context) =
        DeviceUtil.getSoftwareVersion(context)

    @ApiUrl
    @Provides
    fun provideApiUrl() = API_URL

    @ApiKey
    @Provides
    fun provideApiKey() = API_KEY

    @Singleton
    @Provides
    fun provideGson() = GsonProvider.gson



}