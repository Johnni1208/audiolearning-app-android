package com.audiolearning.app.extensions

/**
 * Adds the item to the ArrayList, if the ArrayList does not contain the item.
 *
 * @param item the item to be added
 *
 * @return true - Item was added
 *
 * false - Item was not added, so it is already in the ArrayList
 */
fun <T> ArrayList<T>.addIfNotContained(item: T): Boolean {
    if (this.contains(item)) return false

    this.add(item)
    return true
}

/**
 * Removes the item from the ArrayList, if the ArrayList does contain the item.
 *
 * @param item the item to be removed
 *
 * @return true - Item was removed
 *
 * false - Item was not removed, so it is not in the ArrayList
 */
fun <T> ArrayList<T>.removeIfContained(item: T): Boolean {
    if (!this.contains(item)) return false

    this.remove(item)
    return true
}