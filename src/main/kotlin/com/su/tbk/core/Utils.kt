package com.su.tbk.core

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun jsonFile2JsonString(jsonFile: File,): String {
    val gson=Gson()

}
class LocalDateTimeAdapter: TypeAdapter<LocalDateTime>(){
    private val formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    override fun write(out: JsonWriter?, value: LocalDateTime?) {
        out?.value(value?.format(formatter))
    }

    override fun read(`in`: JsonReader?): LocalDateTime {
        return LocalDateTime.parse(`in`!!.nextString(), formatter)
    }
}