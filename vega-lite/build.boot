(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[org.clojure/clojurescript "1.10.597"]
                  [cljsjs/boot-cljsjs "0.10.5" :scope "test"]
                  [cljsjs/vega "5.25.0-0"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "5.14.1")
(def +version+ (str +lib-version+ "-0"))

(task-options!
  pom {:project     'cljsjs/vega-lite
       :version     +version+
       :description "A high-level grammar for visual analysis, built on top of Vega."
       :url         "https://vega.github.io/vega-lite"
       :scm         {:url "https://github.com/cljsjs/packages"}})

(deftask package []
  (task-options! push {:ensure-branch nil})
  (comp
    (download
     :url (format "https://unpkg.com/vega-lite@%s/build/vega-lite.js" +lib-version+)
     :checksum "19414437E008B1584DA83E324A8BC16E")
    (download
     :url (format "https://unpkg.com/vega-lite@%s/build/vega-lite.min.js" +lib-version+)
     :checksum "A285C4348158A7B69FA4A4556DF5016D")
    (sift :move {(re-pattern "^vega-lite.js$") "cljsjs/development/vega-lite.inc.js"
                 (re-pattern "^vega-lite.min.js$") "cljsjs/production/vega-lite.min.inc.js"})
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.vega-lite"
               :requires ["cljsjs.vega"])
    (pom)
    (jar)))
