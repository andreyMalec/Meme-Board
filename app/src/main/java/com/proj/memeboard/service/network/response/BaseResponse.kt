package com.proj.memeboard.service.network.response

interface BaseResponse<T> {
    fun convert(): T
}