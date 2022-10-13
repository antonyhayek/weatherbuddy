package com.antonyhayek.weatherbuddy.data.networking

import java.io.IOException

class ApiException(val title: String?, message: String?, val code: Int? = -1) :
    IOException(message)