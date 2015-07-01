(ns succession.core
  (:require [incanter.core :as i]
            [incanter.stats :as s]))

(defn log-likelihood* [{:keys [vt ft at pt t z h]} y]
  (let [v  (i/minus y (i/mmult z at))
        zt (i/trans z)
        f  (inc (first (i/mmult z pt zt)))
        kt (-> (i/mmult t pt zt)
               (i/plus h)
               (i/div f))
        at (i/plus (i/mmult t at)
                   (i/mmult kt v))
        lt (i/minus t (i/mmult kt z))
        jt (i/minus h kt)
        pt (i/plus (i/mmult t pt (i/trans lt))
                   (i/mmult h (i/trans jt)))]    
    {:vt (cons v vt)
     :ft (cons f ft)
     :at at :pt pt :t t :h h :z z}))

(defn log-likelihood [coefs p q ys]
  (let [m     (max p q)
        phi   (take m (concat (take p coefs) (repeat 0)))
        theta (take m (concat (drop p coefs) (repeat 0)))
        init {:at (i/matrix 0 m 1)
              :pt (i/identity-matrix m)
              :t  (i/bind-columns
                   (i/matrix phi)
                   (i/bind-rows
                    (i/identity-matrix (dec m))
                    (i/matrix 0 1 (dec m))))
              :h (i/plus phi theta)
              :z (first (i/identity-matrix m))}
        ll    (reduce log-likelihood* init ys)
        sigma (-> (i/sq  (:vt ll))
                  (i/div (:ft ll))
                  (s/mean))
        n (count ys)]
    (* -0.5 (+ n
               (* n (i/log (* 2 Math/PI)))
               (* n (i/log sigma))
               (i/sum (i/log (:ft ll)))))))

(defn aic [coefs p q ys]
  (+ (* -2 (log-likelihood coefs p q ys))
     (* 2 (+ p q 1))))
