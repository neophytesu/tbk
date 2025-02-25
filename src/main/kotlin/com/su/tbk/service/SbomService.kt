package com.su.tbk.service

import java.io.File

interface SbomService {
    fun generateSbom(file: File,type: String): File

}