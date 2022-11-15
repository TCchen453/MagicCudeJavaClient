# MagicCudeJavaClient
Graduation project

如何启动
---
这是个java项目，可以直接把src放进你的eclipse项目里，或者双击start.bat文件快速启动(但我真的不确定会不会有什么编码问题，我尽力了)。  
这里以eclipse这个编译器为例，你只需要在eclipse里新建一个java project，然后在src里面塞入对应的包和java文件就行了。如果出现中文乱码，那就用记事本打开java文件，再把里面的代码手动复制一下就行了。  
之后直接运行cubeDemo包下的Main.java文件，如果一切顺利，那么你将看到一个显示了魔方的java窗口。

鼠标操作
---
拖动操作：拖动可旋转整个魔方的视图，没有别的任何用处(指不能扭转魔方)。  
单击操作：点击魔方的任意色块可以选中单一色块，选中之后会有一个RGB七彩炫光飞天彩虹渐变无敌半透明螺旋好看的框包裹住色块，这个时候右上角会出现6个颜色，点击就能使选中块改变成对应颜色。单击黑色部分取消选中。  
滚轮操作：在选中一个色块后，滚轮可以扭转该色块所在的那一面，向下滚动则顺时针转，反之逆时针。  

键盘操作
---
按数字键123456就能旋转对应的面，按住shift就能逆时针旋转，但要记得关闭输入法。  
逆时针旋转判断的是!@#$%^这些英文字符，已经对一部分中文字符尽可能做了适配，但是这中文输入法的标点有些时候包含两个字符，导致判断十分之麻烦。  

更多细节：  
数字123456分别对应魔方的6个面上下左右前后(UDLRFB)，选中色块后出现的6个颜色也是按照这个顺序从左至右从上到下排列的。  
默认颜色为：  

$\color{#000000}{上}$ $\color{#ffff00}{下}$  
$\color{#ff0000}{左}$ $\color{#ffc800}{右}$  
$\color{#0000ff}{前}$ $\color{#00ff00}{后}$  

我说黑就是白。  

命令行操作
---
这里面包含了所有有用的和没用的功能，十分之重量级！  

这里只说最主要的一条指令  
```
/solve
```
功能：复原魔方。(没了)  

如果在后面多输入一个t，即：  
```
/solve t
```
复原魔方的时候会在每个关键状态停一下，做这个只是方便我自己观察和学习。  

以下包含所有指令，纯复制，跟```/help```里看到的是一样的，只是为了方便查阅。  
```
/help <页数> 显示帮助页
/autoRotate [true/false] 开关自动旋转 简写: /ar /auto
/rotateAngle <旋转角度> 显示或设置每秒自动旋转角度 简写: /ra
/wrongPaint [true/false] 切换错误的涂色方式 简写: /wp
/magicCubeRotate [0~5] 顺时针旋转某个面 简写: /mcr
/changeCode "Srting" 改变操作序列 UDLRFB 简写: /cc
/test [0~5] 测试旋转某个面的动画
/rotateFrames [2~∞] 显示更改每次旋转所消耗的帧数 简写: /rf
/solve (true)降群法+双向bfs解魔方,后输入true则不跳过等待
/getState 获取当前魔方情况
/rangomRotate [int n]随机顺时针旋转n次默认300次 简写: /rr
/autoTest [int n]自动测试n次(打乱，复原) 默认5次 简写: /at
/reverse U1D2L3F2 倒序操作序列 简写: /re
/changeColor x y k 将x面的第y片改成颜色k 简写: /cco
/changeColor 开关改变颜色功能  简写: /lock /col
/takePhoto 向机器人发送拍照指令 简写: /pho
/getAns 向机器人发送获取求解udlr的指令 机器人会开始搜索解法 简写: /ans
/getMode 获取机器人上次扫描魔方的数据 简写: /mode
/robotRotate 根据机器人内部储存的操作序列操作 魔方图像将实时同步 简写: /rodo
/sendSta 用于纠错，向机器人发送当前魔方数据 简写: /ssta
/initColor 重置魔方颜色 简写: /initc
```

建议玩一玩```/ar```和```/wp```，是一些无伤大雅的视觉效果。  

其他玩法
---
每个色块都能改颜色，所以一个显而易见的玩法就是你可以在现实中打乱一个魔方，然后把颜色填上去，得到解之后复原现实魔方。  

在玩透了命令行操作之后，你甚至可以让魔方转成任何你想要的花式造型，举个例子，在魔方复原的情况下输入这个试试：  
```
/mcr 411225155544000404400444214440
```
只要找到对应的公式，比如这个公式是```FD2L2BDB'F2U'FUF2U2F'LDF'U```  
输入``` /cc FD2L2BDB'F2U'FUF2U2F'LDF'U ```就能吧这个公式转换成对应的数字，再输入```/mcr 得到的数字```就行了  
