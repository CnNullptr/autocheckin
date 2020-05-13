# 自动打卡

每天定时通过 http 请求自动打卡

# 使用说明

Prerequisites：jre 1.8 及以上

使用项目根目录下 maven wrapper 的 `mvnw` 即可快速构建。

todo list：

  - [ ] 填写配置文件中的邮箱信息（当程序发生异常，没有正常打卡时会通过邮箱通知）
  - [ ] 填写 Constant 类中的账户信息
  - [ ] 进行一次正常打卡，使用抓包软件抓取到 Json 格式
