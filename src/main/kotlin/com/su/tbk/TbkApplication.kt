package com.su.tbk

import org.mybatis.spring.annotation.MapperScan
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment

@SpringBootApplication
@MapperScan("com.su.tbk.mapper")
class TbkApplication

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger(TbkApplication::class.java)
    val context =  runApplication<TbkApplication>(*args)
    val env = context.getBean(Environment::class.java)
    log.info("\n----------------------------------------------------------\n\t" +
            "Application: '{}' is running Success! \n\t" +
            "Local URL: \thttp://localhost:{}\n\t" +
            "Document:\thttp://localhost:{}/doc.html\n" +
            "----------------------------------------------------------",
        env.getProperty("spring.application.name"),
        env.getProperty("server.port"),
        env.getProperty("server.port"))
}
