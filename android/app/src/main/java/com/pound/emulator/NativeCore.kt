package com.pound.emulator

object NativeCore {
    init {
        System.loadLibrary("pound_android")
    }

    external fun initCore(profileJson: String): Boolean
    external fun startWorker(gamePath: String): Boolean
}
