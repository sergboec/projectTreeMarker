package dev.sergboec.projecttreemarker

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vfs.*
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.*

class MarkedFilesVfsListener : ProjectActivity {
    override suspend fun execute(project: Project) {
        project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, object : BulkFileListener {
            override fun after(events: MutableList<out VFileEvent>) {
                val service = project.service<MarkedFilesService>()
                var changed = false

                for (event in events) {
                    when (event) {
                        is VFileDeleteEvent -> {
                            val relativePath = getRelativePath(project, event.file)
                            if (relativePath != null) {
                                service.removePath(relativePath)
                                changed = true
                            }
                        }
                        is VFileMoveEvent -> {
                            val oldParentRelative = getRelativePath(project, event.oldParent)
                            val newRelativePath = getRelativePath(project, event.file)
                            if (oldParentRelative != null && newRelativePath != null) {
                                val oldRelativePath = "$oldParentRelative/${event.file.name}"
                                service.updatePath(oldRelativePath, newRelativePath)
                                changed = true
                            }
                        }
                        is VFilePropertyChangeEvent -> {
                            if (event.propertyName == VirtualFile.PROP_NAME) {
                                val parent = event.file.parent
                                val parentRelative = if (parent != null) getRelativePath(project, parent) else null
                                if (parentRelative != null) {
                                    val oldRelativePath = "$parentRelative/${event.oldValue}"
                                    val newRelativePath = "$parentRelative/${event.newValue}"
                                    service.updatePath(oldRelativePath, newRelativePath)
                                    changed = true
                                }
                            }
                        }
                    }
                }

                if (changed) {
                    com.intellij.openapi.vcs.FileStatusManager.getInstance(project).fileStatusesChanged()
                }
            }
        })
    }

    private fun getRelativePath(project: Project, file: VirtualFile): String? {
        val projectDir = project.guessProjectDir() ?: return null
        return VfsUtilCore.getRelativePath(file, projectDir)
    }
}
