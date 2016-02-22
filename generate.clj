(use '[leiningen.exec :only (deps)])

(deps '[[hiccup "1.0.5"]])

(require '[hiccup.core :refer :all]
         '[clojure.edn :as edn]
         '[clojure.java.io :as io])

(defn job-html [job]
  [:section.dated-item.job
   [:div.dated-item-head
    [:h3.name (:name job)]
    [:div.details
     [:span.position (:title job)] ", " [:span.date (:date job)]]]
   [:ul.highlights
    (map #(vector :li %) (:highlights job))]])

(defn project-html [project]
  [:section.dated-item.project
   [:div.dated-item-head
    [:h3.name [:a {:href (:link project)} (:name project)]]
    [:div.details
     [:span.date (:date project)]]]
   [:ul.highlights
    (map #(vector :li %) (:highlights project))]])

(defn education-html [education]
  [:section.dated-item.education
   [:div.dated-item-head
    [:h3.name (:name education)]
    [:div.details
     [:span.program (:program education)] ", " [:span.date (:date education)]]]
   [:ul.highlights
    (map #(vector :li %) (:highlights education))]])

(defn contact-html [contact]
  [:div.contact-container
   [:span.email [:a {:href (str "mailto:" (:email contact))} (:email contact)]] " "
   [:span.phone (:phone contact)] " "
   [:span.web [:a {:href (str "http://" (:web contact))} (:web contact)]] " "
   [:span.twitter [:a {:href (str "http://twitter.com/" (:twitter contact))} "@" (:twitter contact)]] " "
   [:span.github [:a {:href (str "http://github.com/" (:github contact))} "github.com/" (:github contact)]]])

(defn generate-resume [data]
  [:html
   [:head
    [:title "Marc Mailhot"]
    [:link {:href "main.css" :rel "stylesheet"}]
    [:link {:href "https://fonts.googleapis.com/css?family=Lato:400,700,300,300italic,400italic,700italic" :rel "stylesheet"}]]
   [:body
    [:header
     [:h1 "Marc Mailhot"]
     [:p "Student Developer - University of Waterloo Computer Science"]
     (contact-html (:contact data))]
    [:div.container
     [:div.right-col
      [:section#about
       [:h2 "About"]
       [:p (:about data)]]
      [:section#skills.skills-list-container
       [:h2 "Skills"]
       [:div.skills-list
        [:h3 "Languages"]
        [:ul (map #(vector :li %) (:languages (:skills data)))]]
       [:div.skills-list
        [:h3 "Technologies"]
        [:ul (map #(vector :li %) (:technologies (:skills data)))]]]]
     [:div.left-col
      [:section#expyerience.dated-list
       [:h2 "Experience"]
       [:div (map job-html (:experience data))]]
      [:section#projects.dated-list
       [:h2 "Projects"]
       [:div (map project-html (:projects data))]]
      [:section#education.dated-list
       [:h2 "Education"]
       [:div (map education-html (:education data))]]]
     ]
    [:div.footer
     [:a.download {:href "resume.pdf"} "Download as a PDF"]]]])


(with-open [rdr (java.io.PushbackReader. (io/reader "data/resume.edn"))
            wrtr (io/writer "index.html")]
  (.write wrtr (html (generate-resume (edn/read rdr)))))
