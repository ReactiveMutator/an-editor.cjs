(ns text.core)

(defn enumerate [lines]
  (map-indexed
    (fn [line-index line]
      (map-indexed
        (fn [character-index letter]
          {:character-index character-index :line-index line-index :letter letter})
        line))
    lines))
