package com.godaddy.namesearch.logger

import android.text.TextUtils
import timber.log.Timber

object LogUtils {

    enum class LogType { D, E, W}

    enum class TagFilter constructor(
        val key: String) {
            API("API"),
            DAO("DAO"),
            LOT("LOT"),
            MIS("MIS"),
            NET("NET"),
            THM("THM"),
            EXC("EXC"),
            APP("APP")
    }

    private fun log(logType: LogType, filterTags: FilterTags, message: String, throwable: Throwable) {
        log(logType, null, filterTags, message, throwable)
    }

    private fun log(
        logType: LogType,
        TAG: String?,
        filterTags: FilterTags,
        message: String,
        throwable: Throwable? = null
    ) {
        val messageHeaderString = getMessageHeader(filterTags)
        when (logType) {
            LogType.D -> Timber.tag(TAG).d("%s %s", messageHeaderString, message)
            LogType.W -> Timber.tag(TAG).w("%s %s", messageHeaderString, message)
            LogType.E -> Timber.e(throwable, "%s %s", messageHeaderString, message)
        }
    }

    fun D(TAG: String, filterTags: FilterTags, message: String) {
        log(LogType.D, TAG, filterTags, message)
    }

    fun W(TAG: String, filterTags: FilterTags, message: String) {
        log(LogType.W, TAG, filterTags, message)
    }

    fun E(filterTags: FilterTags, exception: Exception) {
        E(filterTags, " EXCEPTION:", exception)
    }

    fun E(filterTags: FilterTags, message: String, throwable: Throwable) {
        log(LogType.E, filterTags, if (TextUtils.isEmpty(message)) " EXCEPTION:" else message, throwable)
    }

    class FilterTags internal constructor(argsParam: Array<out TagFilter>) {
        internal var args: Array<out TagFilter> = argsParam

        companion object {
            fun withTags(vararg args: TagFilter): FilterTags {
                return FilterTags(args)
            }
        }
    }

    private fun getMessageHeader(filterTags: FilterTags): String {
        val tagBuffer = StringBuilder()
        tagBuffer.append(addBrackets(TagFilter.APP.key))
        for (tagFilter in filterTags.args) {
            tagBuffer.append(addBrackets(tagFilter.name))
        }
        return tagBuffer.toString()
    }

    private fun addBrackets(tagFilter: String): String {
        return "[$tagFilter]"
    }

}
