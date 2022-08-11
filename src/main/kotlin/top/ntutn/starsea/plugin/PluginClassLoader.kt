package top.ntutn.starsea.plugin

import java.io.File
import java.net.URLClassLoader

class PluginClassLoader(): URLClassLoader(arrayOf()) {
    fun addPlugin(file: File) {
        addURL(file.toURI().toURL())
    }
}