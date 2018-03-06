(ns text.character)

(defn update-dom
  "Update DOM view of the character in a text"
  [character new-value]
  (assoc
    (assoc character :dom new-value)
    :context "dom"))

(defn update-letter
  "Update model of the character in a text"
  [character new-value]
  (assoc
    (assoc-in character [:element] {:letter new-value})
    :context "letter"))

(defn indexed
  "Get the indexed character from an enumerated text"
  [indices enumerated]
  (nth
    (nth (vec enumerated) (:line-index indices))
    (:character-index indices)))
