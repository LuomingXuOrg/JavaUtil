java工具类
=

[![Build status](https://ci.appveyor.com/api/projects/status/7llsmbpr5nh8mapr?svg=true)](https://ci.appveyor.com/project/LuomingXu/javautil)
[![image](https://img.shields.io/badge/maven-v2.2.0-blue.svg)](https://search.maven.org/search?q=g:com.github.luomingxuorg)
[![image](https://img.shields.io/badge/License-Apache__v2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

    储存不知道什么时候就会用到的工具类

|名称|位置|
|---------------|--------|
|密码|[pwd]|
|对list排序|[sort][]|
|对list进行分页操作|[page][]|
|entity之间的转化|[converter][]|
|从do生成groovy文件|[groovy][]|

#### Test, PR

如果有测试的代码请新建"src/main/java/Test"目录, 在此目录下进行测试<br>
此目录不会被提交到仓库, 以保证目录的整洁

[![image]()]

#### maven dependency
```xml
<dependency>
  <groupId>com.github.luomingxuorg</groupId>
  <artifactId>JavaUtil</artifactId>
  <version>$version</version>
</dependency>
```
[pwd]:src/main/java/com/github/luomingxuorg/javaUtil/Util/PwdPbkdf2.java
[sort]:src/main/java/com/github/luomingxuorg/javaUtil/Util/SortUtil.java "排序"
[page]:src/main/java/com/github/luomingxuorg/javaUtil/Util/ListPageHelper.java "分页"
[converter]:src/main/java/com/github/luomingxuorg/javaUtil/Util/EntityConverter.java "转化"
[groovy]:src/main/java/com/github/luomingxuorg/javaUtil/Util/Do2Groovy.java "groovy文件"