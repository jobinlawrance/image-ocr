package com.jobinlawrance.imageocr

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SpringBootApplication
class ImageOcrApplication {

	@GetMapping("/hello" )
	fun home(): String? {
		return "Hello World!"
	}

}

fun main(args: Array<String>) {
	runApplication<ImageOcrApplication>(*args)
}
