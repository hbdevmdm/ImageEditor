package com.app.imageeditor.editor.texteditor

interface TextEditorListener {
    fun onDone(inputText: String?, colorCode: Int)
}