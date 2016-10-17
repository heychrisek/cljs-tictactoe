(ns tictactoe.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)


(defn new-board [n]
  (vec (repeat n (vec (repeat n "B")))))

(def board-size 3)

(defonce app-state
  (atom {:text "Welcome to Tic Tac Toe"
         :board (new-board board-size)}))

(defn computer-move [board]
  (let [board (:board @app-state)
        remaining-spots (for [i (range board-size)
                              j (range board-size)
                              :when (= (get-in board [j i]) "B")]
                          [i j])
        move (rand-nth remaining-spots)
        path (into [:board] (reverse move))]
     (swap! app-state assoc-in path "C")))

(defn blank [i j]
  [:rect {:width 0.9
          :height 0.9
          :x (+ 0.05 i)
          :y (+ 0.05 j)
          :fill "#86C67C"
          :on-click
          (fn rect-click [e]
            (swap! app-state assoc-in [:board j i] "P")
            (computer-move [:board]))}])

(defn circle [i j]
  [:circle
    {:r 0.38
     :stroke "#75A1D0"
     :stroke-width 0.07
     :fill "none"
     :cx (+ 0.5 i)
     :cy (+ 0.5 j)}])

(defn cross [i j]
 [:g {:stroke "darkred"
      :stroke-width 0.1
      :stroke-linecap "round"
      :transform
      (str "translate(" (+ 0.15 i) "," (+ 0.15 j) ") "
           "scale(0.7)")}
  [:line {:x1 0 :y1 0 :x2 1 :y2 1}]
  [:line {:x1 1 :y1 0 :x2 0 :y2 1}]])

(defn tictactoe []
  [:center
    [:h1 (:text @app-state)]
    (into
      [:svg
        {:view-box (str "0 0 " board-size " " board-size)
        :width 500
        :height 500}]
      (for [i (range (count (:board @app-state)))
            j (range (count (:board @app-state)))]
        (case (get-in @app-state [:board j i])
          "B" [blank i j]
          "C" [circle i j]
          "P" [cross i j])))
    [:p
     [:button
      {:on-click
       (fn new-game-click [e]
           (swap! app-state assoc :board (new-board board-size)))}
      "New Game"]]
   ])

(reagent/render-component [tictactoe]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  (prn :board @app-state))
