#!/bin/sh
content="\n Hi, all: \n\n新的安装包已打包成功\n\n---------------[安装地址]---------------\nhttps://www.pgyer.com/erSQ\n"
chmod +x gitlabci/send_email_tool
./gitlabci/send_email_tool   -f  发件人邮箱地址 \
                       -t  发送目标邮箱地址 \
                       -s  邮件服务器域名:端口号 \
                       -xu username \
                       -xp password \
                       -o  tls=auto -o message-charset=utf-8 \
                       -u  "Gitlab CI 自动构建Android最新版本打包成功！" -m $content