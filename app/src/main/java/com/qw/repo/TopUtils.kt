package com.qw.repo

import com.test.soultools.tool.log.TLog

fun String.method1(): String {
    return ""
}

fun log(message: Any?) {
    TLog.d("qw", message)
}