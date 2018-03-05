(ns an_editor.text_line)

(defn makeFrom
  [characters]
  (do
    (def dom (atom { :dom (js/document.createElement "div") } )))
    (:dom (deref dom)))
