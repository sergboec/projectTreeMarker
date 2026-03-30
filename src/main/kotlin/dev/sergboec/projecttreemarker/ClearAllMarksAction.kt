package dev.sergboec.projecttreemarker

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vcs.FileStatusManager

class ClearAllMarksAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val result = Messages.showYesNoDialog(
            project,
            MyMessageBundle.message("action.clear.all.confirm"),
            MyMessageBundle.message("action.clear.all.title"),
            Messages.getQuestionIcon()
        )
        if (result != Messages.YES) return

        project.service<MarkedFilesService>().clearAll()
        FileStatusManager.getInstance(project).fileStatusesChanged()
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null && project.service<MarkedFilesService>().hasAnyMarks()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
