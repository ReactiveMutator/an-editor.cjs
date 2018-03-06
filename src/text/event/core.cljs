(ns text.event.core)

(defn append-atomic
  "Append atomic child to atomic parent"
  [atomic-parent atomic-child]
  (do
    (-> (:dom (deref atomic-parent))
      (.appendChild (:dom (deref atomic-child))))
    atomic-parent))

(defn atomize
  "Convert the letters characters to atomic letters"
  [letters]
  (do
    (def atomic-letter (map
      (fn [letter] (do
        (def atomic-letter
          (atom {:dom (js/document.createTextNode (:letter letter))}))
        atomic-letter))
      letters))
    atomic-letter))

(defn textify
  "Construct atomic text from atomic lines array"
  [atomic-lines]
  (do
    (def atomic-text (atom {:dom (js/document.createElement "div") :lines []}))
    (reduce
      (fn [atomic-text atomic-line] (do
        (append-atomic atomic-text atomic-line)
        (swap! atomic-text assoc :lines (conj (:lines (deref atomic-text)) atomic-line))
        atomic-text))
      atomic-text
      atomic-lines)))

(defn join-into-line
  "Join characters array into atomic line"
  [characters]
  (do
    (def atomic-line (atom {:dom (js/document.createElement "div") :characters []}))
    (reduce
      (fn [atomic-line character] (do
        (append-atomic atomic-text character)
        (swap! atomic-line assoc :characters (conj (:characters (deref atomic-line)) character))
        atomic-line))
      atomic-line
      characters)))

(defn add-context-watch
  "Add context watcher to watching atom triggering by changing some field with specifing particular context"
  [watching-atom watcher-key watcher context]
  (add-watch watching-atom watcher-key
    (fn [key watching-atom old-value new-value] (do
      (cond (= context (:context (deref watching-atom)))
            (watcher key watching-atom old-value new-value))))))

(defn listeners
  "Set the listeners for listening changes of letters in lines for the specified lines"
  [lines listener]
  (dorun (map
    (fn [line] (dorun (map
      (fn [character-atom]
        (add-context-watch character-atom :listener listener "letter"))
      (:characters (deref line)))))
    (:lines (deref lines)))))
