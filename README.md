# PracticeJavaFx


## 关于 

练习JavaFx客户端技术, 与[PracticeSpringboot](https://github.com/NAVERON/PracticeSpringboot)是兄弟项目  
作为兄弟项目的展示端, 主要练习JavaFx和服务端交互相关的技术  

- [ ] RPC  
- [ ] HTTP Client  
- [ ] Message Queue  异步队列  
- [ ] zookeeper 作为配置中心  


## 笔记 

1. 需要使用FXTest 进行JavaFx项目测试  
2. 配置gradle 设置主启动类后, 不需要重写Laucher  
3. module-info requests 的添加顺序必须按照依赖关系进行  
4. 数据模型应当和视图模型分开, 便于修改, 直接在视图中创建数据模型对象, 间接调用, 当前简单实现 直接写到图形中混合  
5. http需要有服务发现的功能，确定当前服务可用, 当前暂且不管，先完成基本功能  
6. 组件点击事件后 http请求可以使用task或者future 来回调显示, 这样界面就不会卡顿了  
7. 整体的逻辑基本这样  按钮点击生成的点 只显示不持久化到服务端; 手动点击生成的立即传送到服务端  
8. 请求轨迹点的过程中  返回的存储成功对吸纳更createTime是null  数据库中却是正常的， 后续再解决  



## 发展 

融合贯通服务端和客户端的问题, 通过练习解决各种问题, 通过问题找到编程的感觉  
先解决程序能够跑起来的为难题, 再提高程序的健壮性  

测试url 
```
所有用户 localhost:8080/api/v1/users  

```


## 结束 

本项目仅供练习  





