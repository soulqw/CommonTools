package com.test.soultools.tool.log

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.PrintWriter
import java.io.StringWriter

object TLog {
    private const val MAX_LENGTH = 4000
    private const val PARAM = "Param"
    private const val NULL = "null"
    private const val V = 0x1
    private const val D = 0x2
    private const val I = 0x3
    private const val W = 0x4
    private const val E = 0x5
    private const val A = 0x6

    val LINE_SEPARATOR = System.getProperty("line.separator").toString()

    const val JSON_INDENT = 4
    /**
     * like log d
     */
    @JvmStatic
    fun d(tag: String, vararg objects: Any?) {
        printLogImp(D, tag, *objects as Array<out Any>)
    }

    /**
     * like log i
     */
    @JvmStatic
    fun i(tag: String, vararg objects: Any?) {
        printLogImp(I, tag, *objects as Array<out Any>)
    }

    /**
     * like log w
     */
    @JvmStatic
    fun w(tag: String, vararg objects: Any?) {
        printLogImp(W, tag, *objects as Array<out Any>)
    }

    /**
     * like log e
     */
    @JvmStatic
    fun e(tag: String, vararg objects: Any?) {
        printLogImp(E, tag, *objects as Array<out Any>)
    }

    @JvmStatic
    fun printTrace() {
        printStackTrace()
    }

    private fun printStackTrace() {
        val tr = Throwable()
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        val message = sw.toString()
        val traceString = message.split("\n\t").toTypedArray()
        val sb = StringBuilder()
        sb.append("\n")
        val packageName = TLog.javaClass.`package`?.name ?: ""
        for (trace in traceString) {
            if (trace.contains("Throwable")) {
                continue
            }
            if (trace.contains(packageName)) {
                continue
            }
            sb.append(trace).append("\n")
        }
        val contents: Array<String> = wrapperLogContent(
            4,
            null,
            sb.toString()
        )
        val tag = contents[0]
        val msg = contents[1]
        val headString = contents[2]
        printDefault(D, tag, headString + msg)
    }

    private fun printLogImp(type: Int, tagStr: String, vararg objects: Any) {
        val contents = wrapperLogContent(5, tagStr, *objects)
        val tag = contents[0]
        val msg = contents[1]
        val headString = contents[2]
        printDefault(type, tag, headString + msg)
    }

    private fun wrapperLogContent(
        lineIndex: Int = 5,
        tagStr: String?,
        vararg objects: Any
    ): Array<String> {
        val stackTrace = Thread.currentThread().stackTrace
        val targetElement = stackTrace[lineIndex]
        val fileName = targetElement.fileName
        val methodName = targetElement.methodName
        var lineNumber = targetElement.lineNumber
        if (lineNumber < 0) {
            lineNumber = 0
        }
        val tag = tagStr ?: fileName
        val msg = getObjectsString(*objects)
        val headString = "($fileName:$lineNumber)->$methodName "
        return arrayOf(tag, msg, headString)
    }

    private fun getObjectsString(vararg objects: Any): String {
        return when {
            objects.size > 1 -> {
                val stringBuilder = StringBuilder()
                stringBuilder.append("\n")
                for (i in objects.indices) {
                    val `object` = objects[i]
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ")
                        .append(`object`.toString()).append("\n")
                }
                stringBuilder.toString()
            }
            objects.size == 1 -> {
                val element = objects[0]
                element.toString()
            }
            else -> {
                NULL
            }
        }
    }

    private fun printDefault(type: Int, tag: String, msg: String) {
        var index = 0
        val length = msg.length
        val countOfSub = length / MAX_LENGTH
        if (countOfSub > 0) {
            for (i in 0 until countOfSub) {
                val sub = msg.substring(index, index + MAX_LENGTH)
                printSub(type, tag, sub)
                index += MAX_LENGTH
            }
            printSub(type, tag, msg.substring(index, length))
        } else {
            printSub(type, tag, msg)
        }
    }

    private fun printSub(type: Int, tag: String, sub: String) {
        when (type) {
            V -> Log.v(tag, sub)
            D -> Log.d(tag, sub)
            I -> Log.i(tag, sub)
            W -> Log.w(tag, sub)
            E -> Log.e(tag, sub)
            A -> Log.wtf(tag, sub)
        }
    }

    fun printJson(tag: String?, msg: String, headString: String) {
        var message: String = try {
            if (msg.startsWith("{")) {
                val jsonObject = JSONObject(msg)
                jsonObject.toString(JSON_INDENT)
            } else if (msg.startsWith("[")) {
                val jsonArray = JSONArray(msg)
                jsonArray.toString(JSON_INDENT)
            } else {
                msg
            }
        } catch (e: JSONException) {
            msg
        }
        message = headString + LINE_SEPARATOR + message
        val lines: Array<String> = message.split(LINE_SEPARATOR).toTypedArray()
        for (line in lines) {
            Log.d(tag, "║ $line")
        }
    }

}