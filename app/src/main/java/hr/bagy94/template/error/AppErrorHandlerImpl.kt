package hr.bagy94.template.error

import hr.bagy94.android.base.error.Error
import hr.bagy94.android.common.error.AppErrorHandler

class AppErrorHandlerImpl : AppErrorHandler {
    override fun handleAPIError(errorBody: String?, httpCode: Int): Error {
        TODO("Not yet implemented AppErrorHandler::handleApiError")
    }
}