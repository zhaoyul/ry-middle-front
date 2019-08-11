(ns ry-middle-front.pages.category
  (:require
   [kee-frame.core :as kf]
   [reagent.core :as r]
   [re-frame.core :as rf]
   ["antd" :as ant]
   [ry-middle-front.utils :as form]))

(def SubMenu (.-SubMenu ant/Menu))
(def MenuItem (.-Item ant/Menu))
(def Header (.-Header ant/Layout))
(def Sider (.-Sider ant/Layout))
(def Content (.-Content ant/Layout))
(def Footer (.-Footer ant/Layout))
(def BreadcrumbItem (.-Item ant/Breadcrumb))
(def FormItem (.-Item ant/Form))


(def mocking-ds
  (repeat 9 {:icon "reconciliation" :product-name "产品名称1" :product-num 111 :is-active true :create-time "2019-08-10 10:10:10"}))

(def columns [{:title "图标"
               :render
               #(r/as-element
                 [:> ant/Icon {:type "project" :theme="twoTone"}])}
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


(defn category-page []
  (let [data (r/atom mocking-ds)
        counter (r/atom 0)]
    (fn []
      [:div
       [:> ant/PageHeader {:title "品类列表"}]
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
         {:columns columns
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
