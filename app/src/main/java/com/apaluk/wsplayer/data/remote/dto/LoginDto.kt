package com.apaluk.wsplayer.data.remote.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "response")
class SaltDto {
    @field:Element(name = "status", required = true)
    lateinit var status: String

    @field:Element(name = "salt", required = false)
    lateinit var salt: String
}

enum class StatusDto {
    OK, Fatal, Unknown
}

