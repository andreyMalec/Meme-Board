package com.proj.memeboard.model.response

interface BaseResponse<T> {
    fun convert(): T
}