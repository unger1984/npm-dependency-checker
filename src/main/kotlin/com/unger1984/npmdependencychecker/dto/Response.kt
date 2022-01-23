package com.unger1984.npmdependencychecker.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class DistTags(
    @get:JsonProperty("latest") var latest: String
)

data class Response(
    @get:JsonProperty("dist-tags") val distTags: DistTags
)

