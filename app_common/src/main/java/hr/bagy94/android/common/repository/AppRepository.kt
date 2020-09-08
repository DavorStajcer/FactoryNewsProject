package hr.bagy94.android.common.repository

import hr.bagy94.android.base.repository.BaseRepository
import hr.bagy94.android.common.error.AppErrorHandler

abstract class AppRepository(appErrorHandler: AppErrorHandler) : BaseRepository(appErrorHandler){

}