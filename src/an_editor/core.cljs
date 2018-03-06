(ns an_editor.core
  (:require (an_editor.editor :as editor :refer [IEditor])
            (text.event.core :as event)
            (text.character :as text-character)
            (text.core :as text)
            (text.control :as text-control)))

(enable-console-print!)

(def line-atoms
  "Text line atoms usable for index access and listening to"
  (map event/atomize (text/enumerate ["W\n" "b\n" "cde\n"])))

(def text
  "Text usable for attaching into DOM"
  (event/textify (map event/join-into-line line-atoms)))

(def body-atom (atom {:dom js/document.body}))

(event/append-atomic body-atom text)

(event/listeners text
  (fn [key character old-value new-value]
    (let [new-dom-value
      (js/document.createTextNode (:letter (:element new-value)))] (do
        (->
          (:dom (deref character))
          (.replaceWith new-dom-value))
        (swap! character text-character/update-dom new-dom-value)))))

(text-control/attach-keyboard js/window line-atoms)

(def first-letter
  (text-character/indexed {:line-index 0 :character-index 0} line-atoms))

(swap! first-letter text-character/update-letter "d")
(swap! first-letter text-character/update-letter "e")
(swap! first-letter text-character/update-letter "j")
