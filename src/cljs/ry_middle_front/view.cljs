(ns ry-middle-front.view
  (:require
   [kee-frame.core :as kf]
   [reagent.core :as r]
   [re-frame.core :as rf]
   ["antd" :as ant]
   [ry-middle-front.utils :as utils]
   [ry-middle-front.pages.product :as product]
   [ry-middle-front.pages.category :as category]))


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
    [:> MenuItem {:key "categories"} "主品类列表"]
    [:> MenuItem {:key "5"} "子品类列表"]
    [:> MenuItem {:key "6"} "款式"]
    [:> MenuItem {:key "7"} "风格"]]])



(defn about-page []
  [:section.section>div.container>div.Content
   [:img {:src "/img/warning_clojure.png"}]])

(defn home-page []
  [:section.section>div.container>div.Content
   [:h1 "I am home"]])

;; <Icon type="reconciliation" theme="twoTone" />
;; <Icon type="project" theme="twoTone" />

(def routes [{:path "index"
              :breadcrumbName "first-level Menu"}
             {:path "index1"
              :breadcrumbName "sec-level Menu"}])

(defn bread-crumbs []
  ;; TODO : 需要根据路由变化
  [:> ant/Breadcrumb
   [:> BreadcrumbItem "一级菜单"]
   [:> BreadcrumbItem "二级菜单"]])

(defn pages []
  [:div
   [bread-crumbs]

   [kf/switch-route (fn [route] (get-in route [:data :name]))
    :home home-page
    :about about-page
    :products ry-middle-front.pages.product/product-page
    :categories category/category-page
    nil [:div "路由没找到"]]])

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
                         :padding 25
                         :margin "10px 10px"}}
      [:> ant/Icon {:className "trigger"
                    :type (if-not @collapsed "menu-unfold" "menu-fold")
                    :on-click toggle}]]
     [:> Content {:style {:margin "0px 10px"
                          :padding 20
                          :background "#fff"
                          :minHeight 800}}
      [pages]]
     [:> Footer
      [:div "版权信息"]]]]])
