package hr.davor_news.factory.error

import android.util.Log
import hr.bagy94.android.base.error.APIError
import hr.bagy94.android.base.error.Error
import hr.davor_news.android.common.error.AppErrorHandler


class AppErrorHandlerImpl : AppErrorHandler {
    override fun handleAPIError(errorBody: String?, httpCode: Int): Error {
      return object : APIError<String>(errorObject = errorBody,httpCode = httpCode){
      }
    }
}