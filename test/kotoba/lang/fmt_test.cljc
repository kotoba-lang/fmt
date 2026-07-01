(ns kotoba.lang.fmt-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :as str]
            [kotoba.lang.fmt :as fmt]))

(deftest format-scalar-and-small-collections
  (is (= "nil" (fmt/format-str nil)))
  (is (= "true" (fmt/format-str true)))
  (is (= "42" (fmt/format-str 42)))
  (is (= ":a" (fmt/format-str :a)))
  (is (= "[1 2 3]" (fmt/format-str [1 2 3])))
  (is (= "{:a 1}" (fmt/format-str {:a 1}))))

(deftest format-normalizes-whitespace
  (is (= "{:a 1, :b 2}" (fmt/format "{:a   1,    :b 2}")))
  (is (= "[1 2 3]" (fmt/format "[1   2  3]"))))

(deftest format-breaks-long-collections
  (let [long-vec (vec (range 20))
        out (fmt/format-str long-vec {:margin 20})]
    (is (str/includes? out "\n"))
    (is (str/ends-with? out "]")))
  (let [long-map (into {} (map vector (range 20) (range 20)))
        out (fmt/format-str long-map {:margin 20})]
    (is (str/includes? out "\n"))))

(deftest format-is-idempotent
  (doseq [data [{:a 1 :b [2 3]} {:nested {:deep {:k [1 2 3]}}}]]
    (is (= (fmt/format-str data) (fmt/format (fmt/format-str data))))))

(deftest format-preserves-map-insertion-order
  ;; EDN read preserves order via array-map for small maps; check a small map
  (let [out (fmt/format-str (array-map :z 1 :a 2 :m 3))]
    (is (= "{:z 1, :a 2, :m 3}" out))))

(deftest format-rejects-bad-edn
  (is (thrown? #?(:clj Throwable :cljs :default) (fmt/format "{:a"))))
