package dev.sergboec.projecttreemarker

import com.intellij.openapi.components.*
import com.intellij.ui.JBColor
import java.awt.Color

@Service(Service.Level.PROJECT)
@State(name = "MarkedFilesSettings", storages = [Storage("markedFilesSettings.xml")])
class MarkedFilesSettings : PersistentStateComponent<MarkedFilesSettings.State> {

    class State {
        var lightColorHex: String = "FFE4AD"
        var darkColorHex: String = "52423D"
        var defaultMarkColor: String = MarkColor.DEFAULT.name
    }

    private var myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState = state
    }

    fun getMarkColor(): JBColor {
        val light = parseColor(myState.lightColorHex, DEFAULT_LIGHT_COLOR)
        val dark = parseColor(myState.darkColorHex, DEFAULT_DARK_COLOR)
        return JBColor(light, dark)
    }

    companion object {
        val DEFAULT_LIGHT_COLOR: Color = Color(0xFFE4AD)
        val DEFAULT_DARK_COLOR: Color = Color(0x52423D)

        private fun parseColor(hex: String, fallback: Color): Color {
            return try {
                Color(hex.removePrefix("#").toLong(16).toInt())
            } catch (_: NumberFormatException) {
                fallback
            }
        }
    }
}
