package uk.co.wedgetech.hostpinger.utils

import java.net.URL

fun makeUrl(url: String): URL {
    return if (Regex("^https?://.*$").matches(url))
        URL(url)
    else URL("http://" + url)
}
