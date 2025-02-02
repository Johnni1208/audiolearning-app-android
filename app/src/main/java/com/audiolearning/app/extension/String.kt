package com.audiolearning.app.extension

import java.util.regex.Pattern

/**
 * Checks whether the string has characters which are not allowed for files.
 *
 * Reserved characters: \|?*<":>/'
 *
 * @return True - If name does not contain any reserved characters
 *
 * False - If name does return any reserved characters
 */
fun String.isAllowedFileName(): Boolean {
    val reservedCharacters = Pattern.compile("[\\\\|?*<\":>/'.]")
    return !reservedCharacters.matcher(this).find()
}
