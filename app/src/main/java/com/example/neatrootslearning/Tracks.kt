package com.example.neatrootslearning

data class Tracks(
    val items: List<Item>,
    val pagingInfo: PagingInfo,
    val totalCount: Int
)