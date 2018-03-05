(ns an_editor.cursor)

(defprotocol ICursor
  "Editor Cursor"
  (line [this step] "Line number at time step")
  (character [this step] "Line character number at time step"))
