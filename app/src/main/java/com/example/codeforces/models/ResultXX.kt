package com.example.codeforces.models

data class ResultXX(
    val author: Author,
    val contestId: Int,
    val creationTimeSeconds: Int,
    val id: Int,
    val memoryConsumedBytes: Int,
    val passedTestCount: Int,
    val problem: Problem,
    val programmingLanguage: String,
    val relativeTimeSeconds: Int,
    val testset: String,
    val timeConsumedMillis: Int,
    val verdict: String
)