(ns ry-middle-front.view
  (:require
   [kee-frame.core :as kf]
   [reagent.core :as r]
   [re-frame.core :as rf]
   ["antd/es/layout" :default layout]
   ["antd/es/menu" :default menu]
   ["antd/es/icon" :default icon]
   ["antd/es/button" :default button]))

(def sub-menu (.-SubMenu menu))
(def menu-item (.-Item menu))
(def header (.-Header layout))
(def sider (.-Sider layout))
(def content (.-Content layout))
(def footer (.-Footer layout))


(def collapsed (r/atom false))
(defn- toggle []
  (swap! collapsed not))

(defn side-menu []
  [:> menu {:theme "dark"
            ;;:style {:width 256}
            :defaultSelectedKeys ["1" "4"]
            :defaultOpenKeys ["sub2"]
            :mode "inline"
            :on-click
            (fn [m]
              (let [k (get (js->clj m) "key")]
                (rf/dispatch [:nav/route-name (keyword k)])))}
   [:> sub-menu {:key "sub1"
                 :title (r/as-element
                         [:div
                          [:> icon {:type "appstore"}]
                          [:span "订单"]])}
    [:> menu-item {:key "1"} "订单子菜单1"]
    [:> menu-item {:key "2"} "订单子菜单2"]]
   [:> sub-menu {:key "sub2"
                 :title (r/as-element
                         [:div
                          [:> icon {:type "appstore"}]
                          [:span "商品管理"]])}
    [:> menu-item {:key "products"} "商品列表"]
    [:> menu-item {:key "4"} "主品类列表"]
    [:> menu-item {:key "5"} "子品类列表"]
    [:> menu-item {:key "6"} "款式"]
    [:> menu-item {:key "7"} "风格"]]])


(defn nav-link [title page]
  [:a.navbar-item
   {:href   (kf/path-for [page])
    :class (when (= page @(rf/subscribe [:nav/page])) "is-active")}
   title])


(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]])

(defn home-page []
  [:section.section>div.container>div.content
   [:h1 "I am home"]])

(defn pages []
  [kf/switch-route (fn [route] (get-in route [:data :name]))
   :home home-page
   :about about-page
   :products [:div "商品信息"]
   nil [:div "路由没找到"]])

(defn root-component []
  [:div
   [:> layout
    [:> sider {:trigger nil
               :collapsible false
               :collapsed @collapsed}
     [:div {:className "logo"}]
     [side-menu]]
    [:> layout
     [:> header {:style {:background "#fff"
                         :padding 0}}
      [:> icon {:className "trigger"
                :type (if-not @collapsed "menu-unfold" "menu-fold")
                :on-click toggle}]]
     [:> content {:style {:margin "24px 16px"
                          :padding 24
                          :background "#fff"
                          :minHeight 280}}
      [pages]]

     [:> footer
      [:div "版权信息"]
      ]]]
   ])
