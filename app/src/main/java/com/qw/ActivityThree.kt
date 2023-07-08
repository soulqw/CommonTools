package com.qw

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
import com.qw.repo.Api
import com.qw.repo.log
import com.qw.tools.R
import com.test.soultools.tool.log.TLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ActivityThree : AppCompatActivity() {

    var service: Api? = null

    var xieChengJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thrid)
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(Api::class.java)
//        testXieChen()
//        textXieCheng2()
//        testXieCheng3()


        findViewById<View>(R.id.btn1).setOnClickListener {
//            testXieCheng4()
            testXieCheng5()
//            GlobalScope.launch(Dispatchers.Main) {
//                val data = withContext(Dispatchers.IO) {
//                    val header = getData()
//                    val finalData = processData(header)
//                    finalData
//                }
//                findViewById<TextView>(R.id.tv_content).text = data
//            }
        }
    }

    fun testXieChen() {
        CoroutineScope(Dispatchers.Main).launch {
            TLog.d("qw", "${Thread.currentThread().name}")
            //切走再切回来
            val data = withContext(Dispatchers.IO) {
                TLog.d("qw", "${Thread.currentThread().name}")
                getNetWorkData()
            }
            TLog.d("qw", data)
            TLog.d("qw", "${Thread.currentThread().name}")
        }

    }

    private suspend fun getNetWorkData(): String {
        delay(1000)
        TLog.d("qw", "${Thread.currentThread().name}")
        return "data"
    }

    fun textXieCheng2() {
        GlobalScope.launch(Dispatchers.Main) {
            TLog.d("qw", "${Thread.currentThread().name}")
            ioCode1()
            uiCode2()
        }
    }

    fun testXieCheng3() {
        val superVisorJob = SupervisorJob()
        val scopr = CoroutineScope(Dispatchers.Main + superVisorJob)
        val handler = CoroutineExceptionHandler { _, exception ->
            TLog.d("qw", exception)
        }
        xieChengJob = scopr.launch(handler) {
            try {

                TLog.d("qw", "${Thread.currentThread().name}")
//                val soulqw = service!!.listRepos("soulqw")
//                val data1 = soulqw[1].name
//                TLog.d("qw", data1)
//                val jakeWharton = service!!.listRepos("JakeWharton")
//                val data2 = jakeWharton[2].name
//                TLog.d("qw", data2)
                yield()
                val data1 = async {
                    TLog.d("qw", "request qw")
                    val dataFinal = service!!.listRepos("soulqw")
                    dataFinal[1].name
                }
                val data2 = async {
                    TLog.d("qw", "request JakeWharton")
                    val dataFinal = service!!.listRepos("JakeWharton")
//                    throw IndexOutOfBoundsException()
                    dataFinal[1].name
                }
                val result = "qinwei+ ${data1.await()} and ${data2.await()} "
                TLog.d("qw", result)
                findViewById<TextView>(R.id.tv_content).text = result


            } catch (e: Throwable) {
                TLog.d("qw", e)
            }
        }
    }

    fun testXieCheng4() {
        val handler = CoroutineExceptionHandler { _, exception ->
            log(exception)
        }
        GlobalScope.launch(handler) {
            val job = launch { // 第一个子协程
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    withContext(NonCancellable) {
                        log("Children are cancelled, but exception is not handled until all children terminate")
                        delay(100)
                        log("The first child finished its non cancellable block")
                    }
                }
            }
            launch { // 第二个子协程
                job.join()
                delay(10)
                log("Second child throws an exception")
                throw ArithmeticException()
            }
        }
    }

    fun testXieCheng5() {
        GlobalScope.launch(Dispatchers.Main) {
            log("work start ")
//            val result = withContext(Dispatchers.IO) {
//                childTask1()
//                log("work1 finish ")
//            }
//            withContext(Dispatchers.Default) {
//                childTask2()
//                log("work2 finish ")
//            }

            val job1 = async (Dispatchers.IO) {
                childTask1()
                log("work1 finish ")
            }
            val job2 = async(Dispatchers.IO) {
//                job1.join()
                childTask2()
                log("work2 finish ")
            }
            log("all work is finish await ${job1.await()} and ${job2.await()}")
            log("all work is finish job $job1 and $job2")
        }
    }

    private suspend fun childTask1() {
        for (i in 0..5) {
            delay(100)
            log("current 1 work in $i")
        }
    }

    private suspend fun childTask2() {
        for (i in 0..10) {
            delay(100)
            log("current 2 work in $i")
        }
    }


    private suspend fun ioCode1() {
        withContext(Dispatchers.IO) {
            TLog.d("qw", "${Thread.currentThread().name}")
        }
    }

    private fun uiCode1() {
        TLog.d("qw", "${Thread.currentThread().name}")
    }


    private fun ioCode2() {
        TLog.d("qw", "${Thread.currentThread().name}")
    }

    private fun uiCode2() {
        TLog.d("qw", "${Thread.currentThread().name}")
    }

    private fun getData(): String {
        Thread.sleep(1000)
        TLog.d("qw", "${Thread.currentThread().name}")
        return "header "
    }

    private fun processData(data: String): String {
        Thread.sleep(1000)
        TLog.d("qw", "${Thread.currentThread().name}")
        return data + "with final data"
    }

    override fun onDestroy() {
        super.onDestroy()
        xieChengJob?.cancel()
    }
}