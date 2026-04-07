package dev.sergboec.projecttreemarker

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.vcs.FileStatusManager

class ToggleMarkAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val virtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY) ?: return
        val service = project.service<MarkedFilesService>()
        val settings = project.service<MarkedFilesSettings>()
        val defaultColor = try {
            MarkColor.valueOf(settings.state.defaultMarkColor)
        } catch (_: IllegalArgumentException) {
            MarkColor.DEFAULT
        }
        virtualFiles.forEach { file ->
            service.toggleMarked(file, defaultColor)
        }

        FileStatusManager.getInstance(project).fileStatusesChanged()
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val virtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        e.presentation.isEnabledAndVisible = project != null && !virtualFiles.isNullOrEmpty()

        if (project != null && !virtualFiles.isNullOrEmpty()) {
            val service = project.service<MarkedFilesService>()
            val allMarked = virtualFiles.all { service.isMarked(it) }
            e.presentation.text = if (allMarked) {
                MyMessageBundle.message("action.unmark.selected")
            } else {
                MyMessageBundle.message("action.mark.selected")
            }
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}

class MarkWithColorAction(private val color: MarkColor) : DumbAwareAction(
    MyMessageBundle.message("action.mark.with.color", color.displayName),
    MyMessageBundle.message("action.mark.with.color.description", color.displayName),
    MarkerIcons.forColor(color)
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val virtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY) ?: return
        val service = project.service<MarkedFilesService>()

        virtualFiles.forEach { file ->
            service.markWithColor(file, color)
        }

        FileStatusManager.getInstance(project).fileStatusesChanged()
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val virtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        e.presentation.isEnabledAndVisible = project != null && !virtualFiles.isNullOrEmpty()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}

class MarkWithColorGroup : DefaultActionGroup() {
    init {
        isPopup = true
        templatePresentation.text = MyMessageBundle.message("group.mark.with.color")
        for (color in MarkColor.entries) {
            add(MarkWithColorAction(color))
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val virtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        e.presentation.isEnabledAndVisible = project != null && !virtualFiles.isNullOrEmpty()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
