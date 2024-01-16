package com.spongycode.songquest.util

object CategoryConvertor {
    fun codeToDisplayText(code: String): String {
        return when (code) {
            Constants.BOLLYWOOD_CODE -> Constants.BOLLYWOOD_DISPLAY_TEXT
            Constants.HOLLYWOOD_CODE -> Constants.HOLLYWOOD_DISPLAY_TEXT
            Constants.DESI_HIP_HOP_CODE -> Constants.DESI_HIP_HOP_DISPLAY_TEXT
            Constants.HIP_HOP_CODE -> Constants.HIP_HOP_DISPLAY_TEXT
            else -> ""
        }
    }
}