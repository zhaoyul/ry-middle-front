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


(def mocking-ds
  (repeat 9 {:icon "reconciliation" :product-name "产品名称1" :vender-num 111 :price "¥10"
             :sold 100 :visit 11 :order 1 :create-time "2019-08-10 10:10:10"}))

(def columns [{:title "商品图片"
               :render
               #(r/as-element
                 [:> ant/Icon {:type "project" :theme="twoTone"}])}
              {:title "商品名称" :dataIndex "product-name" :key "product-name" }
              {:title "商家编号" :dataIndex "vender-num" :key "vender-num"}
              {:title "价格"
               :align "center"
               :dataIndex "price" :key "price"}
              {:title "销量"
               :align "center"
               :dataIndex "sold" :key "sold"}
              {:title "访问量"
               :align "center"
               :dataIndex "visit" :key "visit"}
              {:title "排序"
               :align "center"
               :dataIndex "order" :key "order"}
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

(defn search-form []
  (fn [props]
    (let [this (utils/get-form)]
      [:> ant/Form {:layout "inline"}
       [:> ant/Row
        [:> FormItem {:label "输入搜索:"}
         (utils/decorate-field
          this "商品名称"
          [:> ant/Input {:placeholder "商品名称"}])]
        [:> FormItem {:label "品类:"}
         (utils/decorate-field
          this "品类"
          [:> ant/Input {:placeholder "品类"}])]
        [:> FormItem {:label "编号:"}
         (utils/decorate-field
          this "编号"
          [:> ant/Input {:placeholder "编号"}])]]
       [:> ant/Row
        [:> FormItem {:label "标签："}
         (utils/decorate-field
          this "标签"
          [:> (.-Group ant/Radio)
           [:> ant/Radio {:value 1} "下架"]
           [:> ant/Radio {:value 2} "上架"]])]]])))

(defn product-page []
  (let [counter (r/atom 0)
        data (r/atom mocking-ds)]
    (fn []
      [:div
       [:> ant/PageHeader {:title "商品列表"}]
       [:> ant/Card
        {:title "筛选搜索"
         :extra (r/as-element
                 [:div
                  [:> ant/Button "重置" ]
                  [:> ant/Button {:type "primary"
                                  :style {:marginLeft 16}}
                   "查询结果"]])}
        (utils/create-form search-form)]
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
