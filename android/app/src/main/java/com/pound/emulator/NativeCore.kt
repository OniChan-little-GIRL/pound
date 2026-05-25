package com.pound.emulator

object NativeCore {
    init {
        System.loadLibrary("pound_android")
    }

    external fun initCore()
    external fun startWorker()
}
