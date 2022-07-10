# fatrpc：一个自己实现的轻量级rpc框架

[![OSCS Status](https://www.oscs1024.com/platform/badge/fatsnakeok/fatrpc-framework-core.svg?size=small)](https://www.oscs1024.com/project/fatsnakeok/fatrpc-framework-core?ref=badge_small)
## 项目目的
纸上得来终觉浅，绝知此事要躬行
将自己所学只是应用练手，以及公司业务技术部门培训

##  整体结构与基本分层
- 代理层：负责对底层调用细节的封装；
- 路由层：负责在集群目标服务中的调用筛选策略；
  [详细说明](./fatrpc-framework-core/README.md)
- 协议层：负责请求数据的转码封装等作用；
- 链路层：负责执行一些自定义的过滤链路，可以供后期二次扩展；
- 注册中心层：关注服务的上下线，以及一些权重，配置动态调整等功能；
- 序列化层：负责将不同的序列化技术嵌套在框架中；
- 容错层：当服务调用出现失败之后需要有容错层的兜底辅助；
- 接入层：考虑如何与常用框架Spring的接入；
- 公共层：主要存放一些通用配置，工具类，缓存等信息。

注：为了方便进行单层测试，以及方便查看rpc框架演进的过程，不同层将产生不同分支中。待整体框架完成后，最新代码合并至master分支。



