package dev.sergboec.projecttreemarker

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.xmlb.annotations.XCollection
import java.util.concurrent.ConcurrentHashMap

@Service(Service.Level.PROJECT)
@State(name = "MarkedFilesState", storages = [Storage("markedFiles.xml")])
class MarkedFilesService(private val project: Project) : PersistentStateComponent<MarkedFilesService.State> {
    class State {
        @XCollection(elementName = "path", style = XCollection.Style.v2)
        val markedPaths: MutableSet<String> = ConcurrentHashMap.newKeySet()
    }

    private val myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState.markedPaths.clear()
        myState.markedPaths.addAll(state.markedPaths)
    }

    fun isMarked(file: VirtualFile): Boolean {
        val relativePath = toRelativePath(file) ?: return false
        return myState.markedPaths.contains(relativePath)
    }

    fun toggleMarked(file: VirtualFile) {
        val relativePath = toRelativePath(file) ?: return
        if (!myState.markedPaths.add(relativePath)) {
            myState.markedPaths.remove(relativePath)
        }
    }

    fun clearAll() {
        myState.markedPaths.clear()
    }

    fun hasAnyMarks(): Boolean = myState.markedPaths.isNotEmpty()

    fun updatePath(oldRelativePath: String, newRelativePath: String) {
        if (myState.markedPaths.remove(oldRelativePath)) {
            myState.markedPaths.add(newRelativePath)
        }
    }

    fun removePath(relativePath: String) {
        myState.markedPaths.remove(relativePath)
    }

    fun toRelativePath(file: VirtualFile): String? {
        val projectDir = project.guessProjectDir() ?: return null
        return VfsUtilCore.getRelativePath(file, projectDir)
    }
}
