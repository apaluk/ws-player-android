package com.apaluk.wsplayer.data.webshare.remote.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "response")
open class ResponseDto {
    @field:Element(name = "status", required = true)
    lateinit var status: String
}

enum class StatusDto {
    OK, Fatal, Unknown
}
