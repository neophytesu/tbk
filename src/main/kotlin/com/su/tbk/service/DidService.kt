package com.su.tbk.service



interface DidService {
    fun createKey(): Pair<String,String>
    fun getKeyList(): List<Pair<String,String>>
    fun removeKey(privateKey: String) :Boolean
}