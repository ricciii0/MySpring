# MySpring - 轻量级 Spring 框架实现

正在学习 Spring 框架，于是尝试自己搭建一个简单的 Spring 框架。

## 项目简介

本项目是一个简化版的 Spring IoC 容器实现，核心功能包括：

- ✅ **IoC (控制反转)**：对象创建由容器管理
- ✅ **DI (依赖注入)**：通过 BeanReference 自动注入依赖
- ✅ **单例管理**：确保 Bean 只创建一次
- ✅ **策略模式实例化**：支持反射/CGLIB 两种实例化策略
- ✅ **属性注入**：通过 PropertyValues 配置属性

## 整体架构

```
BeanFactory (接口)
    ↑
AbstractBeanFactory (抽象层) - 模板方法模式
    ↑
AbstractAutowireCapableBeanFactory (核心实现)
    ↑
DefaultListableBeanFactory (具体实现 + Bean 注册)
```

### 核心组件

| 组件 | 作用 |
|------|------|
| `BeanDefinition` | 封装 Bean 的元数据（类信息 + 属性配置） |
| `BeanFactory` | 定义获取 Bean 的接口契约 |
| `AbstractBeanFactory` | 实现模板方法，定义获取 Bean 的骨架流程 |
| `AbstractAutowireCapableBeanFactory` | 核心实现，负责 Bean 的创建和属性注入 |
| `DefaultListableBeanFactory` | 具体实现，维护 Bean 定义注册表 |
| `DefaultSingletonBeanRegistry` | 单例 Bean 缓存管理 |
| `InstantiationStrategy` | 实例化策略接口（反射/CGLIB） |

## Bean 生命周期

完整的 Bean 创建流程如下：

```
用户调用：beanFactory.getBean("UserService")
    ↓
AbstractBeanFactory.doGetBean("UserService", null)
    ↓
1️⃣ 查单例缓存：getSingleton("UserService") → null（首次）
    ↓
2️⃣ 获取定义：getBeanDefinition("UserService") → BeanDefinition{UserService.class, PropertyValues}
    ↓
3️⃣ 创建 Bean：createBean("UserService", beanDefinition, null)
    ↓
AbstractAutowireCapableBeanFactory.createBean()
    ↓
4️⃣ 实例化：createBeanInstance("UserService", beanDefinition, null)
    ↓
    ├─ 扫描 UserService 的所有构造函数
    ├─ args==null → 找无参构造 → UserService()
    └─ CglibSubclassingInstantiationStrategy.instantiate()
        └─ Enhancer.create() → 通过 CGLIB 创建代理实例
    ↓
    现在有了：UserService instance（uID=null, userDao=null）
    ↓
5️⃣ 属性注入：applyPropertyValues("UserService", beanInstance, beanDefinition)
    ↓
    遍历 PropertyValues：
    │
    ├─ 第1个属性：name="uID", value="21322"
    │   └─ BeanUtil.setFieldValue(bean, "uID", "21322")
    │       └─ bean.uID = "21322" ✅
    │
    └─ 第2个属性：name="userDao", value=BeanReference("userDao")
        ↓
        检测到 BeanReference → 依赖注入
        ↓
        递归调用：getBean("userDao")
            ↓
            （重复上述流程...）
            最终返回：UserDao instance ✅
        ↓
        BeanUtil.setFieldValue(bean, "userDao", userDaoInstance)
            └─ bean.userDao = userDaoInstance ✅
    ↓
    现在：UserService(uID="21322", userDao=UserDao实例)
    ↓
6️⃣ 注册单例：addSingleton("UserService", beanInstance)
    └─ singletonObjects.put("UserService", beanInstance)
    ↓
7️⃣ 返回：return beanInstance → UserService(uID="21322", userDao=UserDao实例)
```

## 核心设计模式

| 设计模式 | 应用位置 | 作用 |
|---------|---------|------|
| **模板方法模式** | `AbstractBeanFactory` | 定义获取 Bean 的骨架流程，子类实现具体创建逻辑 |
| **策略模式** | `InstantiationStrategy` | 可切换的实例化策略（反射/CGLIB） |
| **工厂模式** | `BeanFactory` 层级 | 创建和管理 Bean 对象 |
| **单例模式** | `DefaultSingletonBeanRegistry` | 管理 Bean 单例缓存 |
| **依赖注入** | `applyPropertyValues` | 自动装配 Bean 之间的依赖关系 |

## 使用示例

```java
// 1. 创建 Bean 工厂
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

// 2. 注册 userDao
beanFactory.registryBeanDefinition("userDao", new BeanDefinition(UserDao.class));

// 3. 创建 userService 并配置属性
PropertyValues propertyValues = new PropertyValues();
propertyValues.addPropertyValue(new PropertyValue("uID", "21322"));
propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
beanFactory.registryBeanDefinition("UserService", beanDefinition);

// 4. 获取并使用 Bean
UserService userService = (UserService) beanFactory.getBean("UserService");
userService.queryUserInfo();  // 输出：查询用户信息：Ricci
```

## 项目结构

```
src/main/java/com/ricci/springframework/
├── beans/
│   ├── BeanException.java                    # Bean 异常类
│   ├── PropertyValue.java                    # 属性值封装
│   └── PropertyValues.java                   # 属性值集合
├── beans/factory/
│   ├── BeanFactory.java                      # Bean 工厂接口
│   ├── config/
│   │   ├── BeanDefinition.java               # Bean 定义
│   │   ├── BeanReference.java                # Bean 引用（用于依赖注入）
│   │   └── SingletonBeanRegistry.java        # 单例注册接口
│   └── support/
│       ├── AbstractAutowireCapableBeanFactory.java  # 核心实现
│       ├── AbstractBeanFactory.java          # 抽象工厂（模板方法）
│       ├── BeanDefinitionRegistry.java       # Bean 定义注册接口
│       ├── CglibSubclassingInstantiationStrategy.java  # CGLIB 实例化策略
│       ├── DefaultListableBeanFactory.java   # 默认可列表 Bean 工厂
│       ├── DefaultSingletonBeanRegistry.java # 默认单例注册器
│       ├── InstantiationStrategy.java        # 实例化策略接口
│       └── SimpleInstantiationStrategy.java  # 反射实例化策略
```

## 运行测试

```bash
mvn test -Dtest=ApiTest#test_BeanFactory
```

## 技术栈

- **JDK**: 1.8+
- **构建工具**: Maven
- **核心依赖**:
  - `cglib:cglib:3.3.0` - CGLIB 动态代理
  - `cn.hutool:hutool-all:5.8.43` - Hutool 工具库
  - `junit:junit:4.13.1` - 单元测试

## 核心实现特性

相比参考实现 [small-spring-step-04](https://github.com/fuzhengwei/small-spring/blob/main/small-spring-step-04)，本项目在以下方面进行了增强：

1. **更严格的构造函数匹配**：通过 `isMatchParamTypes` 验证参数类型兼容性
2. **更健壮的空值处理**：在多处添加了 null 检查，避免 NPE
3. **基本类型支持**：处理基本类型与包装类型的转换
4. **完整的异常链**：保留原始异常信息，方便调试
5. **修复了 CGLIB 实例化策略的 bug**：正确处理无参构造的情况

## 学习资源

本项目参考了 [fuzhengwei/small-spring](https://github.com/fuzhengwei/small-spring) 项目，是学习 Spring 源码的入门实践。

## License

MIT License
