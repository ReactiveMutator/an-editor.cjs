(ns text.control
  (:require (text.character :as character)))

(def atomic-keyboard (atom {}))

(defn set-key-state
  [atomic-keyboard key-code key-state]
  (assoc atomic-keyboard (keyword (js/String.fromCharCode key-code)) key-state))

(defn key-on
  [atomic-keyboard key-code]
  (set-key-state atomic-keyboard key-code true))

(defn key-off
  [atomic-keyboard key-code]
  (set-key-state atomic-keyboard key-code false))

(def atomic-cursor (atom {:line-index 0 :character-index 0}))

(defn typing-handler
  [char-code atomic-cursor enumerated-text]
  (swap! (character/indexed
    (deref atomic-cursor)
      enumerated-text)
    character/update-letter
    (js/String.fromCharCode char-code)
    :context "letter"))

(defn attach-keyboard
  [window-dom enumerated-text]
  (do
   (window-dom.addEventListener "keydown"
    (fn [event] (swap! atomic-keyboard key-on (.-keyCode event))) false)
   (window-dom.addEventListener "keyup"
    (fn [event] (swap! atomic-keyboard key-off (.-keyCode event))) false)
   (window-dom.addEventListener "keypress"
    (fn [event] (typing-handler (.-charCode event) atomic-cursor enumerated-text)) false)))
