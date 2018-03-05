(ns text.event.core)

(defn append-listenable [parent-atom child-atom]
  (do
    (-> (:dom (deref parent-atom))
      (.appendChild (:dom (deref child-atom))))
    parent-atom))

(defn atomize [letters]
  (do
    (def atomic-letter (map
      (fn [letter] (do
        (def atomic-letter
          (atom {:dom (js/document.createTextNode (:letter letter))}))
        atomic-letter))
      letters))
    atomic-letter))

(defn textify [atomic-lines]
  (do
    (def atomic-text (atom {:dom (js/document.createElement "div") :lines []}))
    (reduce
      (fn [atomic-text atomic-line] (do
        (append-listenable atomic-text atomic-line)
        (swap! atomic-text assoc :lines (conj (:lines (deref atomic-text)) atomic-line))
        atomic-text))
      atomic-text
      atomic-lines)))

(defn merge-into-line [characters]
  (do
    (def line-atom (atom {:dom (js/document.createElement "div") :characters []}))
    (reduce
      (fn [line-atom character] (do
        (append-listenable atomic-text character)
        (swap! line-atom assoc :characters (conj (:characters (deref line-atom)) character))
        line-atom))
      line-atom
      characters)))

(defn add-context-watch [watching-atom watcher-key watcher context]
  (add-watch watching-atom watcher-key
    (fn [key watching-atom old-value new-value] (do
      (cond (= context (:context (deref watching-atom)))
            (watcher key watching-atom old-value new-value))))))

(defn listeners [lines listener]
  (dorun (map
    (fn [line] (dorun (map
      (fn [character-atom]
        (add-context-watch character-atom :listener listener "letter"))
      (:characters (deref line)))))
    (:lines (deref lines)))))
