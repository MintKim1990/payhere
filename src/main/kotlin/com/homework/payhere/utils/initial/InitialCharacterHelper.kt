package com.homework.payhere.utils.initial

fun extractInitialCharacters(name: String): List<String> {

    val BASE_CODE = 44032
    val CHOSUNG = 588
    val CHOSUNG_LIST = charArrayOf(
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
        'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
        'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
        'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    )


    val splitArray = name.split(" ")
    return splitArray.map { split ->
        split.fold(StringBuilder()) { acc, char ->
            acc.append(
                when (char) {
                    in '가'..'힣' -> {
                        val charCode = char.code - BASE_CODE
                        val charIndex = charCode / CHOSUNG
                        CHOSUNG_LIST[charIndex].toString()
                    }
                    else -> char.toString()
                }
            )
        }.toString()
    }
}