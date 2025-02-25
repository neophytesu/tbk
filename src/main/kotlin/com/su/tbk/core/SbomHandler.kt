package com.su.tbk.core

import java.io.File
import java.time.LocalDate

fun sbomResolve(file: File, type: String,newFileName:String): File {
    val params = ArrayList<String>()
    val outFile = File(newFileName)
    outFile.createNewFile()
    params.add("trivy")
    params.add("fs")
    params.add("--format")
    params.add(type)
    params.add("--output")
    params.add(outFile.absolutePath)
    params.add(file.absolutePath)
    val processBuilder = ProcessBuilder(params)
    processBuilder.redirectErrorStream(true)
    val process = processBuilder.start()
    process.waitFor()
    return outFile
}