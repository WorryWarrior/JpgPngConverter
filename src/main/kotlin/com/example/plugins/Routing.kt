package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO

fun Application.configureRouting() {

    routing {
        get("/") {
            withContext(Dispatchers.IO) {
                call.respondHtml {
                    body {
                        form(
                            encType = FormEncType.multipartFormData,
                            action = "/convert",
                            method = FormMethod.post
                        ) {
                            input(type = InputType.file) {
                                id = "convert-image"
                                name = "convert-image"
                                onChange = "form.submit();"
                            }
                        }

                        form(
                            encType = FormEncType.multipartFormData,
                            action = "/convert",
                            method = FormMethod.post
                        ) {
                            input(type = InputType.file) {
                                id = "convert-image"
                                name = "convert-image"
                                onChange = "form.submit();"
                            }
                        }
                    }
                }
            }
        }

        post("/convert") {
            withContext(Dispatchers.IO) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                val multipart = call.receiveMultipart()
                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val fileBytes = part.streamProvider().readBytes()
                        byteArrayOutputStream.writeBytes(fileBytes)
                    }
                    part.dispose()
                }

                val suppliedFile = File.createTempFile("Test", "Test")
                val byteArray = byteArrayOutputStream.toByteArray()

                val fileOutputStream = FileOutputStream(suppliedFile)
                fileOutputStream.write(byteArray)
                fileOutputStream.flush()
                fileOutputStream.close()

                if (isPng(byteArray)) {
                    val responseFile = File.createTempFile("Test_JPG", "Test_JPG")
                    ImageIO.write(ImageIO.read(suppliedFile), "JPEG", responseFile)

                    if (ImageIO.read(responseFile) != null) {
                        call.response.header(
                            HttpHeaders.ContentDisposition,
                            ContentDisposition.Attachment.withParameter(
                                ContentDisposition.Parameters.FileName,
                                "converted_jpg.jpg"
                            ).toString()
                        )

                        call.respondFile(responseFile)
                    }
                }
                else if (isJpg(byteArray)) {
                    val responseFile = File.createTempFile("Test_PNG", "Test_PNG")
                    ImageIO.write(ImageIO.read(suppliedFile), "PNG", responseFile)

                    if (ImageIO.read(responseFile) != null) {
                        call.response.header(
                            HttpHeaders.ContentDisposition,
                            ContentDisposition.Attachment.withParameter(
                                ContentDisposition.Parameters.FileName,
                                "converted_png.png"
                            ).toString()
                        )
                        call.respondFile(responseFile)
                    }
                }
                else {
                    call.respondText("Invalid file")
                }
            }
        }
    }
}

private fun isPng(byteArray: ByteArray): Boolean {
    val pngMagicBytes = listOf(0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A).map { it.toByte() }

    for (i in pngMagicBytes.indices) {
        if (byteArray[i] != pngMagicBytes[i])
            return false
    }

    return true
}

private fun isJpg(byteArray: ByteArray): Boolean {
    val jpgMagicBytes = listOf(0xFF, 0xD8, 0xFF, 0xE0).map { it.toByte() }

    for (i in jpgMagicBytes.indices) {
        if (byteArray[i] != jpgMagicBytes[i])
            return false
    }

    return true
}