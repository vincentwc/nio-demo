这是一个学习nio的demo项目

NIO 核心类：
- Channel 通道
  - 双向【可读可写】
  - 非阻塞
  - 操作唯一性【操作Channel的唯一方式是使用Buffer，通过Buffer操作Channel实现数据块的读写。Buffer本质上就是一个字节数组】
  - Channel核心实现类
    - 文件类 FileChannel
    - UDP类 DatagramChannel
    - TCP类 ServerSocketChannel/SocketChannel
- Buffer 缓冲区
  - 提供唯一与channel进行交互的方式
    - 读写Channel中的数据
    - 一块可以从中读取或写入的内存区域。被NIO包装成一个NIO Buffer对象，并提供一组便于操作内存的方法
  - Buffer属性
    - Capacity：容量
        - 数组可以容纳的最大字节长度，超出则需要将其清空才能重新写入
    - Position：位置
        - 写模式：表示当前位置，初始位置为0，最大为capacity-1
        - 读模式：重置为0
    - Limit：上限
        - 写模式：最多能往Buffer中写的数据量，此时等于Capacity
        - 读模式：此时等于写模式下的Position的值
    - Mark：标记
        - 标记一个特定的Position的位置，可通过Buffer的Reset()方法恢复到标记位置
- Selector 选择器|多路复用器
 - 四种就绪状态
    - OP_CONNECT：连接就绪。连接操作，Client端支持的一种操作
    - OP_ACCEPT：接收就绪。连接可接受操作，仅ServerSocketChannel支持
    - OP_READ：读就绪
    - OP_WRITE：写就绪
    
NIO的工作流程步骤：
1. 首先是先创建ServerSocketChannel 对象，和真正处理业务的线程池
2. 然后给刚刚创建的ServerSocketChannel 对象进行绑定一个对应的端口，然后设置为非阻塞
3. 然后创建Selector对象并打开，然后把这Selector对象注册到ServerSocketChannel 中，并设置好监听的事件，监听 SelectionKey.OP_ACCEPT
4. 接着就是Selector对象进行死循环监听每一个Channel通道的事件，循环执行 Selector.select() 方法，轮询就绪的 Channel
5. 从Selector中获取所有的SelectorKey（这个就可以看成是不同的事件），如果SelectorKey是处于 OP_ACCEPT 状态，说明是新的客户端接入，调用 ServerSocketChannel.accept 接收新的客户端。
6. 然后对这个把这个接受的新客户端的Channel通道注册到ServerSocketChannel上，并且把之前的OP_ACCEPT 状态改为SelectionKey.OP_READ读取事件状态，并且设置为非阻塞的，然后把当前的这个SelectorKey给移除掉，说明这个事件完成了
7. 如果第5步的时候过来的事件不是OP_ACCEPT 状态，那就是OP_READ读取数据的事件状态，然后调用本文章的上面的那个读取数据的机制就可以了
