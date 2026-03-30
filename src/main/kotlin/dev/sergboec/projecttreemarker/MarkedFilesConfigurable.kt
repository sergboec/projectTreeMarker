package dev.sergboec.projecttreemarker

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.*

class MarkedFilesConfigurable(private val project: Project) : BoundConfigurable("Project Tree Marker") {

    private var lightColorHex: String = ""
    private var darkColorHex: String = ""

    override fun createPanel() = panel {
        val settings = project.service<MarkedFilesSettings>()
        lightColorHex = settings.state.lightColorHex
        darkColorHex = settings.state.darkColorHex

        group("Mark Highlight Colors") {
            row("Light theme color (hex):") {
                textField()
                    .columns(8)
                    .bindText(::lightColorHex)
                    .validationOnInput {
                        if (!isValidHex(it.text)) error("Invalid hex color (e.g. FFE4AD)") else null
                    }
            }
            row("Dark theme color (hex):") {
                textField()
                    .columns(8)
                    .bindText(::darkColorHex)
                    .validationOnInput {
                        if (!isValidHex(it.text)) error("Invalid hex color (e.g. 52423D)") else null
                    }
            }
        }
    }

    override fun apply() {
        super.apply()
        val settings = project.service<MarkedFilesSettings>()
        settings.state.lightColorHex = lightColorHex
        settings.state.darkColorHex = darkColorHex
    }

    override fun isModified(): Boolean {
        val settings = project.service<MarkedFilesSettings>()
        return lightColorHex != settings.state.lightColorHex || darkColorHex != settings.state.darkColorHex
    }

    override fun reset() {
        val settings = project.service<MarkedFilesSettings>()
        lightColorHex = settings.state.lightColorHex
        darkColorHex = settings.state.darkColorHex
        super.reset()
    }

    private fun isValidHex(hex: String): Boolean {
        val cleaned = hex.removePrefix("#")
        return cleaned.length == 6 && cleaned.all { it in '0'..'9' || it in 'a'..'f' || it in 'A'..'F' }
    }
}
