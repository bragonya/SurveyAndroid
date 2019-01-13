package com.apps.brayan.surveyapp.coreApp.fallback

import android.support.annotation.StringRes

import com.apps.brayan.surveyapp.R

enum class FallbackCase constructor(@param:StringRes @field:StringRes
                                            var message: Int) {
    EMPTY_ERROR(R.string.no_data_fallback_error_message),
    INTERNET_ERROR(R.string.no_internet_fallback_error_message),
    GENERIC_ERROR(R.string.generic_fallback_error_message)
}
