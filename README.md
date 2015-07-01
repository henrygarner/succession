# Succession

A Clojure library for calculating statistics of time series data.

## Usage

The library currently exposes two functions: `log-likelihood` and `aic`.

```clojure
(:require [succession.core :as s])

(def ts (range 10))

;; Calculate the log likelihood of an ARMA(2,2) model
;; with the paramaters [0.1 0.2 0.3 0.4]

(s/log-likelihood [0.1 0.2 0.3 0.4] 2 2 ts)
;; -24.87389765998291

;; Calculate the AIC

(s/aic [0.1 0.2 0.3 0.4] 2 2 (range 10))
;; 59.74779531996582
```

## License

Copyright © 2015 Henry Garner

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
