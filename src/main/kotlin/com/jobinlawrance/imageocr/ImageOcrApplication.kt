package com.jobinlawrance.imageocr

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import net.sourceforge.tess4j.Tesseract
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.net.URL
import java.nio.file.Paths

@RestController
@SpringBootApplication
class ImageOcrApplication {

    @GetMapping("/hello")
    fun home(): String? {
        return "Hello World!"
    }

    @GetMapping("/ocr/printedText")
    suspend fun ocr(@RequestParam imageUrl: String): String {
        val client = HttpClient(CIO)
        val cwd = Paths.get("").toAbsolutePath()
        val imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
        val file = File("$cwd/$imageName")
        val tesserract = Tesseract()
        return withContext(Dispatchers.Default) {
            client.downloadFile(file, imageUrl)
            tesserract.setDatapath("$cwd/tesdata")
            try {
                tesserract.doOCR(file) ?: ""
            } catch (e: Exception) {
                e.localizedMessage
            } finally {
                delay(1000)
                file.delete()
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ImageOcrApplication>(*args)
}

suspend fun HttpClient.downloadFile(file: File, url: String): Boolean {

    val response = request<HttpStatement> {
        url(url)
        method = HttpMethod.Get
    }.execute()

    return if (response.status.isSuccess()) {
        withContext(Dispatchers.IO) {
            response.content.copyAndClose(file.writeChannel())
        }
        true
    } else false

}

