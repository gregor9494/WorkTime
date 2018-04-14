package ggaworowski.worktime.di

import com.mosbyextra.ggaworowski.mosbyextralibrary.common.IPComunicator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Grzegorz Gaworowski company Graphicbox on 2017-10-11.
 */

@Module
class CommunicatorModule {

    @Provides
    @Singleton
    fun provideCommunicator(): IPComunicator {
        return IPComunicator()
    }

}
