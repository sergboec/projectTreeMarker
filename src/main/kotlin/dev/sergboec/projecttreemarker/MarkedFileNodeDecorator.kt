package dev.sergboec.projecttreemarker

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.openapi.components.service
import com.intellij.ui.LayeredIcon

class MarkedFileNodeDecorator : ProjectViewNodeDecorator {
    override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
        val project = node.project ?: return
        val virtualFile = node.virtualFile ?: return

        val service = project.service<MarkedFilesService>()
        if (!service.isMarked(virtualFile)) return

        val baseIcon = data.getIcon(false) ?: return
        val layered = LayeredIcon(2)
        layered.setIcon(baseIcon, 0)
        layered.setIcon(MarkerIcons.MarkBadge, 1)
        data.setIcon(layered)
    }
}
