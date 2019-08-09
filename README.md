java工具类
=

[![Build status](https://ci.appveyor.com/api/projects/status/c5uviv5vhwo07t5i?svg=true)](https://ci.appveyor.com/project/LuomingXu/javautil)
[![image](https://img.shields.io/badge/maven-v2.5.2-blue.svg)](https://search.maven.org/search?q=g:com.github.luomingxuorg%20JavaUtil)
[![image](https://img.shields.io/badge/License-Apache__v2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

    储存不知道什么时候就会用到的工具类

|名称|位置|
|---------------|:--------:|
|密码|[pwd]|
|对list排序|[sort][]|
|对list进行分页操作|[page][]|
|entity之间的转化|[converter][]|
|从sql生成框架需要的文件|[frameworkFile][]|
|aop实现类|[aop]|

#### Version

版本号|说明|当前版本
--------|------------------------------|:--:
major|主版本号|2
minor|添加了一些新的功能|5
build|对功能增加了一些方法, 优化了一些东西|0
error|错误修正|null

#### Test, PR

如果有测试的代码请新建"src/main/java/Test"目录, 在此目录下进行测试<br>
此目录不会被提交到仓库, 以保证目录的整洁<br>

![image](dir-tree.png)

#### Cglib Converter Example
```java
@Slf4j
public class ExampleConverter implements Converter
{
    /**
     * 自定义拷贝
     *
     * @param value   源对象
     * @param target  目标对象的class
     * @param context context目标对象set方法(就是个String类型的, 不知道为什么要用Object)
     * @return 目标对象
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object convert(Object value, Class target, Object context)
    {
        log.info("value: {}", value.getClass().toString());
        log.info("target: {}", target.getName());
        log.info("context: {}", context.getClass().toString());

        if (value instanceof InnerModelDO)
        {
            InnerModelDTO obj = new InnerModelDTO();
            WrapperConverter.copy(value, obj, false, null);
            return obj;
        }

        if (value instanceof List)
        {
            List list = (List) value;
            if (!list.isEmpty() && list.get(0) instanceof InnerModelDO)
            {
                return WrapperConverter.copyList(list, InnerModelDTO.class, false, null);
            }
            else
            {
                return Collections.emptyList();
            }
        }

        return value;
    }
}
```

#### maven dependency
```xml
<dependency>
    <groupId>com.github.luomingxuorg</groupId>
    <artifactId>JavaUtil</artifactId>
    <version>$version</version>
    <!-- if you have "SLF4J: Class path contains multiple SLF4J bindings." warning
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
        </exclusion>
    </exclusions>
    -->
</dependency>
```
[pwd]:src/main/java/com/github/luomingxuorg/javautil/util/PwdPbkdf2.java "密码"
[sort]:src/main/java/com/github/luomingxuorg/javautil/util/SortUtil.java "排序"
[page]:src/main/java/com/github/luomingxuorg/javautil/util/ListPageHelper.java "分页"
[converter]:src/main/java/com/github/luomingxuorg/javautil/util/WrapperConverter.java "bean拷贝"
[frameworkFile]:src/main/java/com/github/luomingxuorg/javautil/util/GenerateFrameworkFile.java "groovy文件"
[aop]:src/main/java/com/github/luomingxuorg/javautil/util/AspectLog.java "aop"