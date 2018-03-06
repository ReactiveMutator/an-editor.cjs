(ns text.control
  (:require (text.character :as character)))

(def atomic-keyboard (atom {}))

(defn set-key-state
  [atomic-keyboard key-code key-state]
  (assoc atomic-keyboard (keyword (str key-code)) key-state))

(defn key-on
  [atomic-keyboard key-code]
  (set-key-state atomic-keyboard (keyword (str key-code)) true))

(defn key-off
  [atomic-keyboard key-code]
  (set-key-state atomic-keyboard (keyword (str key-code)) false))

(def atomic-cursor (atom {:line-index 0 :character-index 0}))

(defn typing-handler
  [char-code enumerated-text]
  (swap! (character/indexed
    (deref atomic-cursor)
      enumerated-text)
    character/update-letter
    (js/String.fromCharCode char-code)))

(defn move-cursor
  [cursor delta]
  (do
    (assoc
      (assoc cursor
        :line-index (+ (:line-index cursor) (:delta-line-index delta)))
        :character-index (+ (:character-index cursor) (:delta-character-index delta)))))

(defn cursor-handler
  [key atomic-keyboard old-keyboard new-keyboard] (do
    (let [keyboard (deref atomic-keyboard)]
      (cond
        (= ((keyword ":38") keyboard) true)
          (swap! atomic-cursor move-cursor {:delta-line-index -1 :delta-character-index 0})
        (= ((keyword ":40") keyboard) true)
          (swap! atomic-cursor move-cursor {:delta-line-index 1 :delta-character-index 0})
        (= ((keyword ":37") keyboard) true)
          (swap! atomic-cursor move-cursor {:delta-line-index 0 :delta-character-index -1})
        (= ((keyword ":39") keyboard) true)
          (swap! atomic-cursor move-cursor {:delta-line-index 0 :delta-character-index 1})))))

(defn attach-keyboard
  [window-dom enumerated-text]
  (do
   (window-dom.addEventListener "keydown"
     (fn [event] (cond (not= (.-keyCode event) 0) (swap! atomic-keyboard key-on (.-keyCode event))) false))
   (window-dom.addEventListener "keyup"
     (fn [event] (cond (not= (.-keyCode event) 0) (swap! atomic-keyboard key-off (.-keyCode event))) false))
   (window-dom.addEventListener "keypress"
     (fn [event] (cond (not= (.-charCode event) 0) (typing-handler (.-charCode event) enumerated-text)) false))
   (add-watch atomic-keyboard :cursor-handler cursor-handler)))
