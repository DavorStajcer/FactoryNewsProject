package hr.davor_news.android.common.repository

import hr.bagy94.android.base.repository.BaseRepository
import hr.davor_news.android.common.error.AppErrorHandler

abstract class AppRepository(appErrorHandler: AppErrorHandler) : BaseRepository(appErrorHandler){

}