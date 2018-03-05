(ns an_editor.text)

(defprotocol IText
  "Text Abstract Data Structure"
  (insert [this lineNumber characterNumber characterValue]
    "Insert character into line with number lineNumber and character with number characterNumber")
  (delete [this lineNumber characterNumber]
    "Delete character from line with number lineNumber and character with number characterNumber")
  (linesCount [this]
    "Count of lines in the text")
  (linesRange [this fromNumber toNumber]
    "Lines of text with range from fromNumber to toNumber"))
