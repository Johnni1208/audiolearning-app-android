package com.audiolearning.app.util

/**
 * Throw this when Fragment, Activity or any other Arguments are missing.
 *
 * @param missingArgumentKey Key of the argument, example: ARG_TITLE
 */
class MissingArgumentException(missingArgumentKey: String) :
    Exception("No argument provided for: $missingArgumentKey")