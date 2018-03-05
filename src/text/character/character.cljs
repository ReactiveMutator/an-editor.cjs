(ns text.character)

(defn update-dom [character new-value]
  (assoc
    (assoc character :dom new-value)
    :context "dom"))

(defn update-letter [character new-value]
  (assoc
    (assoc-in character [:element] {:letter new-value})
    :context "letter"))

(defn indexed [indices enumerated]
  (nth
    (nth (vec enumerated) (:line-index indices))
    (:character-index indices)))
