
### 添加计算逻辑：

1. 支持`Instant - Instant`，得到`OraDecimal`类型的天数，可以为负数。
2. 支持`Instant`与`Number`类型相加（谁在前谁在后都一样），`Number`类型为天数（支持小数），计算得到`Instant`对象
3. 支持`Instant`与`Number`类型相减（必须`Instant`类型在前），`Number`类型为天数（支持小数），计算得到`Instant`对象

### 删除计算逻辑

1. 删除字符串与日期的比较逻辑，改为直接抛出`CompareNotSupportedException`异常


TODO

1. 支持新的contentType类型，不要用枚举锁死扩展的可能性
2. 自定义aviator和stepFlow框架解耦，新开一个项目
3. 做复杂公式的单测，尽量用到aviator语法。该单测属于aviator单测
4. 按照大模型