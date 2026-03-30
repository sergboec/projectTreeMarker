# ProjectTreeMarker

ProjectTreeMarker is an IntelliJ IDEA plugin that allows you to mark files and folders in the Project View with a background color (orange). This is useful for temporarily highlighting files you're currently working on or that need your attention.

## Features

- **Background Coloring:** Marked files and directories get a subtle orange background in the Project View.
- **Context Menu Integration:** Easily toggle marks by right-clicking any file or folder in the Project View.
- **Persistent Storage:** Marks are saved per project and persist between IDE restarts.
- **Multi-selection Support:** Mark or unmark multiple files at once.

## Usage

1. In the **Project View**, right-click on the file or folder you want to mark.
2. Select **Toggle File Mark** from the context menu.
3. The filename will now have an orange background. Repeat the process to remove the mark.

## Installation

This plugin is currently in development. To use it, you can build it from source and install it manually.

1. Clone the repository.
2. Run `./gradlew buildPlugin`.
3. The plugin ZIP will be in `build/distributions/`.
4. In IntelliJ IDEA, go to **Settings > Plugins > Install Plugin from Disk...** and select the ZIP file.
