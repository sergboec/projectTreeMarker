# File Marker

File Marker is an IntelliJ IDEA plugin that lets you mark and highlight files and directories in the Project View with colored badge overlays.

## Features

- **Toggle File Marks:** Mark or unmark files with a single action or keyboard shortcut (`Alt+Shift+M`).
- **Multiple Mark Colors:** Choose from several mark colors (Default, Pink, Yellow, Dark Blue).
- **Customizable Colors:** Customize highlight colors for light and dark themes in Settings.
- **Persistent Storage:** Marks are saved per project and persist between IDE restarts.
- **Auto-update:** Marks automatically update when files are moved or renamed.
- **Clear All Marks:** Remove all file marks at once when needed.
- **Multi-selection Support:** Mark or unmark multiple files at once.

## Usage

1. In the **Project View**, right-click on a file or directory.
2. Use the **File Marker** submenu to:
   - **Toggle File Mark** — mark or unmark the selected file.
   - **Mark with Color** — choose a specific color for the mark.
   - **Clear All File Marks** — remove all marks from the project.
3. Alternatively, use the keyboard shortcut `Alt+Shift+M` to toggle a mark on the selected file.

## Settings

Go to **Settings → Tools → File Marker** to customize highlight colors for light and dark themes.

## Installation

### From JetBrains Marketplace

Search for **File Marker** in **Settings → Plugins → Marketplace** and install it.

### From Source

1. Clone the repository.
2. Run `./gradlew buildPlugin`.
3. The plugin ZIP will be in `build/distributions/`.
4. In IntelliJ IDEA, go to **Settings → Plugins → Install Plugin from Disk...** and select the ZIP file.
