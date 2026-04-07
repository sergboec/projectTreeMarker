package dev.sergboec.projecttreemarker

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object MarkerIcons {
    @JvmField
    val MarkBadge: Icon = IconLoader.getIcon("/icons/mark-badge.svg", MarkerIcons::class.java)

    @JvmField
    val MarkBadgePink: Icon = IconLoader.getIcon("/icons/mark-badge-pink.svg", MarkerIcons::class.java)

    @JvmField
    val MarkBadgeYellow: Icon = IconLoader.getIcon("/icons/mark-badge-yellow.svg", MarkerIcons::class.java)

    @JvmField
    val MarkBadgeDarkBlue: Icon = IconLoader.getIcon("/icons/mark-badge-darkblue.svg", MarkerIcons::class.java)

    fun forColor(color: MarkColor): Icon = when (color) {
        MarkColor.DEFAULT -> MarkBadge
        MarkColor.PINK -> MarkBadgePink
        MarkColor.YELLOW -> MarkBadgeYellow
        MarkColor.DARK_BLUE -> MarkBadgeDarkBlue
    }
}
