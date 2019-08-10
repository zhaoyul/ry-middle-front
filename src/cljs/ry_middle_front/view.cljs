(ns ry-middle-front.view
  (:require
   [kee-frame.core :as kf]
   [reagent.core :as r]
   [re-frame.core :as rf]
   ["antd" :as ant]
   [ry-middle-front.utils :as utils]
   [ry-middle-front.pages.product :as product]))


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
    [:> MenuItem {:key "4"} "主品类列表"]
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

(defn tform []
  (fn [props]

    (let [the-form (utils/get-form)
          {:keys [getFieldDecorator
                  getFieldsError
                  getFieldError
                  isFieldTouched]} the-form
          usernameError (and (isFieldTouched "username")
                             (getFieldError "username"))]

      [:> ant/Form {:layout "inline"}
       [:> FormItem {:validateStatus (if usernameError "error" "")
                     :help (str usernameError " ")}
        (utils/decorate-field
         the-form "username" {:rules [{:required true}]}
         [:> ant/Input
          {:prefix (r/as-element [:> ant/Icon {:type "user"}])}])]])))

;;(form/create-form tform)

(defn pages []
  [:div
   [:> ant/Breadcrumb
    [:> BreadcrumbItem "good"]
    [:> BreadcrumbItem "good"]]

   [kf/switch-route (fn [route] (get-in route [:data :name]))
    :home home-page
    :about about-page
    :products product/product-page
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
