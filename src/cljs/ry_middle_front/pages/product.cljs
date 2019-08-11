(ns ry-middle-front.pages.product
  (:require
   [kee-frame.core :as kf]
   [reagent.core :as r]
   [re-frame.core :as rf]
   ["antd" :as ant]
   [ry-middle-front.utils :as utils]))

(def SubMenu (.-SubMenu ant/Menu))
(def MenuItem (.-Item ant/Menu))
(def Header (.-Header ant/Layout))
(def Sider (.-Sider ant/Layout))
(def Content (.-Content ant/Layout))
(def Footer (.-Footer ant/Layout))
(def BreadcrumbItem (.-Item ant/Breadcrumb))
(def FormItem (.-Item ant/Form))

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



(defn product-page []
  (let [counter (r/atom 0)]
    (fn []
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
        [:> ant/Table
         #_{:columns columns
            ;;:bordered true
            :dataSource @data
            :rowSelection
            {:on-change
             (fn [row-keys rows]
               (prn "row-keys:" row-keys " rows:" rows)
               (let [selected (js->clj rows :keywordize-keys true)]
                 (reset! counter (count selected))
                 ((.-info ant/message) (str "你刚才选择了: " @counter #_(map :product-name selected)))))}}]]

       [:div {:style {:marginTop 16}}
        (when (pos? @counter)
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
                          :style {:marginLeft 100}}]]])

        [:span {:style {:marginLeft 8}}
         (str "选择条数："@counter)]]])))
