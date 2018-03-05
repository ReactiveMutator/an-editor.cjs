(ns text.control)

(def keyboard (atom {}))

(defn log-key-code [event] (println (.-keyCode event)))
(defn log-char-code [event] (println (.-charCode event)))

(defn attach-keyboard [window-dom enumerated-text] (do
   (window-dom.addEventListener "keydown" log-key-code false)
   (window-dom.addEventListener "keyup" log-key-code false)
   (window-dom.addEventListener "keypress" log-char-code false)))
