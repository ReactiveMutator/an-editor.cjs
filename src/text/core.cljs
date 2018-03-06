(ns text.core)

(defn enumerate
  "Enumerate the lines with characters setting line and character indices for all line characters"
  [lines]
  (map-indexed
    (fn [line-index line]
      (map-indexed
        (fn [character-index letter]
          {:character-index character-index :line-index line-index :letter letter})
        line))
    lines))
