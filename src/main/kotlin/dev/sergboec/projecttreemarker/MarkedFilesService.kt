package dev.sergboec.projecttreemarker

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import java.util.concurrent.ConcurrentHashMap

@Service(Service.Level.PROJECT)
@State(name = "MarkedFilesState", storages = [Storage("markedFiles.xml")])
class MarkedFilesService(private val project: Project) : PersistentStateComponent<MarkedFilesService.State> {
    class State {
        var markedPathColors: MutableMap<String, String> = mutableMapOf()
    }

    private val myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState.markedPathColors.clear()
        myState.markedPathColors.putAll(state.markedPathColors)
    }

    fun isMarked(file: VirtualFile): Boolean {
        val relativePath = toRelativePath(file) ?: return false
        return myState.markedPathColors.containsKey(relativePath)
    }

    fun getMarkColor(file: VirtualFile): MarkColor {
        val relativePath = toRelativePath(file) ?: return MarkColor.DEFAULT
        val colorName = myState.markedPathColors[relativePath] ?: return MarkColor.DEFAULT
        return try {
            MarkColor.valueOf(colorName)
        } catch (_: IllegalArgumentException) {
            MarkColor.DEFAULT
        }
    }

    fun toggleMarked(file: VirtualFile, color: MarkColor = MarkColor.DEFAULT) {
        val relativePath = toRelativePath(file) ?: return
        if (myState.markedPathColors.containsKey(relativePath)) {
            myState.markedPathColors.remove(relativePath)
        } else {
            myState.markedPathColors[relativePath] = color.name
        }
    }

    fun markWithColor(file: VirtualFile, color: MarkColor) {
        val relativePath = toRelativePath(file) ?: return
        myState.markedPathColors[relativePath] = color.name
    }

    fun clearAll() {
        myState.markedPathColors.clear()
    }

    fun hasAnyMarks(): Boolean = myState.markedPathColors.isNotEmpty()

    fun updatePath(oldRelativePath: String, newRelativePath: String) {
        val color = myState.markedPathColors.remove(oldRelativePath)
        if (color != null) {
            myState.markedPathColors[newRelativePath] = color
        }
    }

    fun removePath(relativePath: String) {
        myState.markedPathColors.remove(relativePath)
    }

    fun toRelativePath(file: VirtualFile): String? {
        val projectDir = project.guessProjectDir() ?: return null
        return VfsUtilCore.getRelativePath(file, projectDir)
    }
}
