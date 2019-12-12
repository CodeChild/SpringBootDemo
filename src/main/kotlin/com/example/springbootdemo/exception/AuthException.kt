package com.example.springbootdemo.exception

import java.lang.Exception

class AuthException(resultCode:Int) : Exception(resultCode.toString()) {
}