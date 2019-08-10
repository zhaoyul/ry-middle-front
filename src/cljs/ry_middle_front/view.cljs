(ns ry-middle-front.view
  (:require
   [kee-frame.core :as kf]
   [reagent.core :as r]
   [re-frame.core :as rf]
   ["antd" :as ant]
   [ry-middle-front.form :as form]))


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

;; <Icon type="reconciliation" theme="twoTone" />
;; <Icon type="project" theme="twoTone" />
(def mocking-ds
  (repeat 100 {:icon "reconciliation" :product-name "产品名称1" :product-num 111 :is-active true :create-time "2019-08-10 10:10:10"}))

(def columns [{:title "图标"
               :render
               #(r/as-element
                 [:> ant/Icon {:type "project"}])}
              {:title "产品名称" :dataIndex "product-name" :key "product-name" }
              {:title "商品数" :dataIndex "product-num" :key "product-num"}

              {:title "是否激活"
               :render
               #(r/as-element
                 [:> ant/Switch])}
              {:title "设置"
               :align "center"
               :render
               #(r/as-element
                 [:div
                  [:> ant/Button
                   "查看下级"]])}
              {:title "创建时间"
               :align "center"
               :dataIndex "create-time" :key "create-time"}
              {:title "操作"
               :align "center"
               :render
               #(r/as-element
                 [:div
                  [:> ant/Button
                   "编辑"]
                  [:> ant/Button {:type "danger" :style {:margin "0px 20px"}}
                   "删除"]])}])


(defn product-page []
  (let [data (r/atom mocking-ds)]
    [:div
     [:> ant/PageHeader {:title "商品列表"}]
     [:> ant/Card
      [:> ant/Row
       [:> ant/Col {:span 12} [:h1 "数据列表"]]
       [:> ant/Col {:span 12} [:> ant/Button {:type "primary"
                                              :style {:float "right"}}
                               [:> ant/Icon {:type "plus"}]
                               "增加数据"]]]]
     [:div {:style  {:border "1px solid #E8E8E8"
                     :padding 10}}
      [:> ant/Table {:columns columns
                     :dataSource @data}]]

     [:div {:style {:marginTop 16}}
      [:> ant/Dropdown
       {:overlay
        (r/as-element
         [:> ant/Menu
          {:on-click
           (fn [js-event]
             (prn "刚才我们选了:"
                  (-> js-event
                      (js->clj :keywordize-keys true)
                      :key)))}
          [:> MenuItem {:key "delete"} "删除"]])}
       [:> ant/Button "批量操作"
        [:> ant/Icon {:type "down"
                      :style {:marginLeft 100}}]]]

      [:> ant/Button {:type "primary"
                      :style {:marginLeft 20}}
       "确定"]]]))

(def routes [{:path "index"
              :breadcrumbName "first-level Menu"}
             {:path "index1"
              :breadcrumbName "sec-level Menu"}])

(defn tform []
  (fn [props]

    (let [the-form (form/get-form)
          {:keys [getFieldDecorator
                  getFieldsError
                  getFieldError
                  isFieldTouched]} the-form
          usernameError (and (isFieldTouched "username")
                             (getFieldError "username"))]

      [:> ant/Form {:layout "inline"}
       [:> FormItem {:validateStatus (if usernameError "error" "")
                     :help (str usernameError " ")}
        (form/decorate-field
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
    :products product-page
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
