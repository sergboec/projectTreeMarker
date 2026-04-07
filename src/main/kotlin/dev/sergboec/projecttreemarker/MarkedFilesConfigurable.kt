package dev.sergboec.projecttreemarker

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.*

class MarkedFilesConfigurable(private val project: Project) :
    BoundConfigurable(MyMessageBundle.message("configurable.display.name")) {

    private var lightColorHex: String = ""
    private var darkColorHex: String = ""
    private var defaultMarkColor: MarkColor = MarkColor.DEFAULT

    override fun createPanel() = panel {
        val settings = project.service<MarkedFilesSettings>()
        lightColorHex = settings.state.lightColorHex
        darkColorHex = settings.state.darkColorHex
        defaultMarkColor = try {
            MarkColor.valueOf(settings.state.defaultMarkColor)
        } catch (_: IllegalArgumentException) {
            MarkColor.DEFAULT
        }

        group(MyMessageBundle.message("settings.default.color.group.title")) {
            row(MyMessageBundle.message("settings.default.color.label")) {
                comboBox(MarkColor.entries.toList(), com.intellij.ui.SimpleListCellRenderer.create { label, value, _ ->
                    label.text = value?.displayName ?: ""
                })
                    .bindItem(::defaultMarkColor.toNullableProperty())
            }
        }

        group(MyMessageBundle.message("settings.group.title")) {
            row(MyMessageBundle.message("settings.light.color.label")) {
                textField()
                    .columns(8)
                    .bindText(::lightColorHex)
                    .validationOnInput {
                        if (!isValidHex(it.text)) error(MyMessageBundle.message("settings.invalid.hex.error", "FFE4AD")) else null
                    }
            }
            row(MyMessageBundle.message("settings.dark.color.label")) {
                textField()
                    .columns(8)
                    .bindText(::darkColorHex)
                    .validationOnInput {
                        if (!isValidHex(it.text)) error(MyMessageBundle.message("settings.invalid.hex.error", "52423D")) else null
                    }
            }
        }
    }

    override fun apply() {
        super.apply()
        val settings = project.service<MarkedFilesSettings>()
        settings.state.lightColorHex = lightColorHex
        settings.state.darkColorHex = darkColorHex
        settings.state.defaultMarkColor = defaultMarkColor.name
    }

    override fun isModified(): Boolean {
        val settings = project.service<MarkedFilesSettings>()
        return super.isModified() || lightColorHex != settings.state.lightColorHex || darkColorHex != settings.state.darkColorHex || defaultMarkColor.name != settings.state.defaultMarkColor
    }

    override fun reset() {
        val settings = project.service<MarkedFilesSettings>()
        lightColorHex = settings.state.lightColorHex
        darkColorHex = settings.state.darkColorHex
        defaultMarkColor = try {
            MarkColor.valueOf(settings.state.defaultMarkColor)
        } catch (_: IllegalArgumentException) {
            MarkColor.DEFAULT
        }
        super.reset()
    }

    private fun isValidHex(hex: String): Boolean {
        val cleaned = hex.removePrefix("#")
        return cleaned.length == 6 && cleaned.all { it in '0'..'9' || it in 'a'..'f' || it in 'A'..'F' }
    }
}
