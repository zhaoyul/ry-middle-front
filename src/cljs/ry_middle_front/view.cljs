(ns ry-middle-front.view
  (:require
   [kee-frame.core :as kf]
   [reagent.core :as r]
   [re-frame.core :as rf]
   ["antd" :as ant]))

(def SubMenu (.-SubMenu ant/Menu))
(def MenuItem (.-Item ant/Menu))
(def Header (.-Header ant/Layout))
(def Sider (.-Sider ant/Layout))
(def Content (.-Content ant/Layout))
(def Footer (.-Footer ant/Layout))
(def BreadcrumbItem (.-Item ant/Breadcrumb))
(def FormItem (.-Item ant/Form))

(def collapsed (r/atom false))
(defn- toggle []
  (swap! collapsed not))

(defn tform []
  [:> ant/Form {:layout "inline"}
   [:> FormItem
    [:> ant/Input {:prefix (r/as-element
                            [:> ant/Icon {:type "user"
                                          :style {:color "rgba(0,0,0,.25)"}}])
                   :placeholder "test input"}]]])

(defn side-menu []
  [:> ant/Menu {:theme "dark"
                ;;:style {:width 256}
                :defaultSelectedKeys ["1" "4"]
                :defaultOpenKeys ["sub2"]
                :mode "inline"
                :on-click
                (fn [m]
                  (let [k (get (js->clj m) "key")]
                    (rf/dispatch [:nav/route-name (keyword k)])))}
   [:> SubMenu {:key "sub1"
                :title (r/as-element
                        [:div
                         [:> ant/Icon {:type "appstore"}]
                         [:span "订单"]])}
    [:> MenuItem {:key "1"} "订单子菜单1"]
    [:> MenuItem {:key "2"} "订单子菜单2"]]
   [:> SubMenu {:key "sub2"
                :title (r/as-element
                        [:div
                         [:> ant/Icon {:type "appstore"}]
                         [:span "商品管理"]])}
    [:> MenuItem {:key "products"} "商品列表"]
    [:> MenuItem {:key "4"} "主品类列表"]
    [:> MenuItem {:key "5"} "子品类列表"]
    [:> MenuItem {:key "6"} "款式"]
    [:> MenuItem {:key "7"} "风格"]]])


(defn nav-link [title page]
  [:a.navbar-item
   {:href   (kf/path-for [page])
    :class (when (= page @(rf/subscribe [:nav/page])) "is-active")}
   title])


(defn about-page []
  [:section.section>div.container>div.Content
   [:img {:src "/img/warning_clojure.png"}]])

(defn home-page []
  [:section.section>div.container>div.Content
   [:h1 "I am home"]])

(def routes [{:path "index"
              :breadcrumbName "first-level Menu"}
             {:path "index1"
              :breadcrumbName "sec-level Menu"}])

(defn pages []
  [:div
   [:> ant/Breadcrumb
    [:> BreadcrumbItem "good"]
    [:> BreadcrumbItem "good"]]
   [tform]
   [:> ant/PageHeader {:title "haha"}

    [kf/switch-route (fn [route] (get-in route [:data :name]))
     :home home-page
     :about about-page
     :products [:div "商品信息"]
     nil [:div "路由没找到"]]]])

(defn root-component []
  [:div
   [:> ant/Layout
    [:> Sider {:trigger nil
               :collapsible false
               :collapsed @collapsed}
     [:div {:className "logo"}]
     [side-menu]]
    [:> ant/Layout
     [:> Header {:style {:background "#fff"
                         :padding 0}}
      [:> ant/Icon {:className "trigger"
                    :type (if-not @collapsed "menu-unfold" "menu-fold")
                    :on-click toggle}]]
     [:> Content {:style {:margin "4px 0px"
                          :padding 4
                          :background "#fff"
                          :minHeight 280}}
      [pages]]
     [:> Footer
      [:div "版权信息"]]]]])
