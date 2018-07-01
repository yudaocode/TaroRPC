# 通信

* [ 需要优化 ] 心跳检测连接
* [ 需要优化 ] 超时处理
* [ 需要优化 ] 逻辑线程池
* [ 需要优化 ] callback
* [ 需要优化 ] 超时
* [ 需要优化 ] 增加 MessageHandler
* [ 需要优化 ] Codec
* [ 需要优化 ] 分层优化
* [] 优化：协议的字节占用
* [ ] 调用错误时的链接检测
* [ ] 重连
* [ ] 非关键方法的实现
* [ ] 多连接
* [ ] 连接状态
* [ ] 优化，decode 只解析部分，更细的解析，在多线程里。即 motan NettyMessage 的做法

## 序列化

* [ ] 优化：参考 Dubbo Output 的设计，优化序列化逻辑
* [ ]

# RPC

* [ ] Protocol
* [ ] Invoker Result Invocation
* [ ] 心跳和普通请求的兼容
* [ ] Exporter
* [ ] 方法参数