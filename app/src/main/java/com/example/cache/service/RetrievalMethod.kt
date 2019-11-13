package com.example.cache.service

enum class RetrievalMethod {
    LOCAL, REMOTE;

    override fun toString(): String = super
        .toString()
        .toLowerCase()
        .capitalize()
}