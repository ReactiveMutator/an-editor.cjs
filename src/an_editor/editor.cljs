(ns an_editor.editor
  (:require [an_editor.text_line :as text_line]))

(defprotocol IEditor
  "Monospace Text Editor Protocol"
  (bind [this dom] "Bind Editor to DOM")
  (insert [this cursor character] "Insert character into cursor position")
  (backspace [this cursor] "Do backspace at cursor position")
  (delete [this cursor] "Delete character at cursor position")
  (enter [this cursor] "Insert newline at cursor position")
  (toEndOfLine [this cursor] "Move cursor to the end of current line where cursor is located")
  (toStartOfLine [this cursor] "Move cursor to the start of current line where cursor is located"))

(deftype Editor [textLines]
  IEditor
  (bind [this dom]
    (reduce
      (fn [text textLineNode] (do (-> text (.appendChild textLineNode)) text))
      dom
      textLines)))
