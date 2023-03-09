package com.example.common.utils

import com.example.common.log.LaterLog

object StringUtils {
    private val pattern = "(.*)(https?://[\\w-]+(\\.[\\w-]+)+([\\w-.,@?^=%&:/~+#]*[\\w@?^=%&/~+#])?)(.*)".toRegex()

    private fun extractText(input: String): List<String>{
        val matchResult = pattern.find(input)
        matchResult?.groupValues?.forEach {
            LaterLog.e("group: $it", "extractText")
        }
        val (title, url, _, _, description) = matchResult?.groupValues ?: listOf("", "", "", "", "")
        println("title: $title")
        println("url: $url")
        println("description: $description")
        return listOf(title, url, description)
    }

    fun extractUrlFromText(input: String): String {
        val extractText = extractText(input)
        return extractText[1]
    }

    fun extractTitleFromText(input: String): String {
        val extractText = extractText(input)
        return extractText[0]
    }

    fun extractDescriptionFromText(input: String): String {
        val extractText = extractText(input)
        return extractText[2]
    }
}