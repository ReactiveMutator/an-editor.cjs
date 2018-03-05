(ns an_editor.core
  (:require (an_editor.editor :as editor :refer [IEditor Editor])
            (an_editor.text_line :as text_line)))

(enable-console-print!)

(defn atomize [elements]
  (do
    (def elementAtoms (map
      (fn [element] (do
        (def elementAtom
          (atom {:element element :dom (js/document.createTextNode (:character element))}))
        elementAtom))
      elements))
    elementAtoms))

(defn textify [lineAtoms]
  (do
    (def textAtom (atom {:dom (js/document.createElement "div") :lines []}))
    (reduce
      (fn [textAtom lineAtom] (do
        (-> (:dom (deref textAtom))
          (.appendChild (:dom (deref lineAtom))))
        (swap! textAtom assoc :lines (conj (:lines (deref textAtom)) lineAtom))
        textAtom))
      textAtom
      lineAtoms)))

(defn mergeIntoLine [characters]
  (do
    (def lineAtom (atom {:dom (js/document.createElement "div") :characters []}))
    (reduce
      (fn [lineAtom character] (do
        (-> (:dom (deref lineAtom))
          (.appendChild (:dom (deref character))))
        (swap! lineAtom assoc :characters (conj (:characters (deref lineAtom)) character))
        lineAtom))
      lineAtom
      characters)))

(defn enumerate [lines]
  (map-indexed
    (fn [lineIndex line]
      (map-indexed
        (fn [characterIndex character]
          {:characterIndex characterIndex :lineIndex lineIndex :character character})
        line))
    lines))

(def lineAtoms
  (map atomize (enumerate ["W\n" "b\n" "cde\n"])))

(def text
  (textify (map mergeIntoLine lineAtoms)))

(def bodyAtom (atom {:dom js/document.body}))

(defn add-context-watch [watching-atom watcher-key watcher context]
  (add-watch watching-atom watcher-key
    (fn [key watching-atom old-value new-value] (do
      (cond (= context (:context (deref watching-atom)))
            (watcher key watching-atom old-value new-value))))))

(defn listeners [lines listener]
  (dorun (map
    (fn [line] (dorun (map
      (fn [character-atom]
        (add-context-watch character-atom :listener listener "character"))
      (:characters (deref line)))))
    (:lines (deref lines)))))

(defn appendAtom [parentAtom childAtom]
  (do
    (-> (:dom (deref parentAtom))
      (.appendChild (:dom (deref childAtom))))
    parentAtom))

(appendAtom bodyAtom text)

(defn update-character-dom [character new-value]
  (assoc
    (assoc character :dom new-value)
    :context "dom"))


(listeners text
  (fn [key character old-value new-value]
    (let [new-dom-value
      (js/document.createTextNode (:character (:element new-value)))] (do
        (->
          (:dom (deref character))
          (.replaceWith new-dom-value))
        (swap! character update-character-dom new-dom-value)))))

(defn character [indices enumerated]
    (nth
      (nth (vec enumerated) (:lineIndex indices))
      (:characterIndex indices)))

(defn update-character [character new-value]
  (assoc
    (assoc-in character [:element] {:character new-value})
    :context "character"))

(def firstLetter (character {:lineIndex 0 :characterIndex 0} lineAtoms))

(swap! firstLetter update-character "d")
(swap! firstLetter update-character "e")
(swap! firstLetter update-character "j")
