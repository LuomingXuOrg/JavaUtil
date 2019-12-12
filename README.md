java工具类
=

[![Build status](https://ci.appveyor.com/api/projects/status/c5uviv5vhwo07t5i?svg=true)](https://ci.appveyor.com/project/LuomingXu/javautil)
[![image](https://img.shields.io/badge/maven-v2.7.2-blue.svg)](https://search.maven.org/search?q=g:com.github.luomingxuorg%20JavaUtil)
[![image](https://img.shields.io/badge/License-Apache__v2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

    储存不知道什么时候就会用到的工具类

名称|位置
---------------|:--------:
密码|[pwd]
对list排序|[sort][]
对list进行分页操作|[page][]
entity之间的转化|[converter][]
从sql生成框架需要的文件|[frameworkFile][]
aop实现类|[aop]
分布式ID生成类|[snowflake]
全国高等学校名单(2019/6/15)|[college]
mybatis column配置生成|[mybatisColumns]

#### Test, PR

如果有测试的代码请新建"/test"目录, 在此目录下进行测试<br>

![image](dir-tree.png)

#### ID Generate Example
```java
public class GenerateID
{
    public static long nextID()
    {
        Snowflake idGen = new Snowflake(1, 1, 1566885913535L);
        return idGen.nextId();
    }
}
// if you do not use default callback
public class CustomSnowflake extends Snowflake
{
    public CustomSnowflake(long dataCenterId, long workerId, long projectStartEpoch)
    {
        super(dataCenterId, workerId, projectStartEpoch);
    }

    @Override
    protected boolean timeBackwardsCallback(long timestamp, long lastTimestamp)
    {
        // do something
        return true;
    }
}
```

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

#### MyBatis columns gen
```java
public class gen
{
    public static void main(String[] args)
    {
        String path = "C:\\Users\\Desktop\\script.sql";
        String formerColumnsResult = "<id column=\"id\" jdbcType=\"BIGINT\" property=\"id\"/>\n" +
                "<result column=\"comment\" jdbcType=\"VARCHAR\" property=\"comment\"/>\n" +
                "<result column=\"creator_id\" jdbcType=\"BIGINT\" property=\"creatorId\"/>\n" +
                "<result column=\"creator_name\" jdbcType=\"VARCHAR\" property=\"creatorName\"/>\n" +
                "<result column=\"create_time\" jdbcType=\"TIMESTAMP\" property=\"createTime\"/>";
        
        CompleteFullColumns.generate(path, formerColumnsResult);
    }
    
    /*
    result will like: 
    comment.id as comment_id,
    comment.comment as comment_comment,
    comment.creator_id as comment_creator_id,
    comment.creator_name as comment_creator_name,
    comment.create_time as comment_create_time,
    <id column="comment_id" jdbcType="BIGINT" property="id"/>
    <result column="comment_comment" jdbcType="VARCHAR" property="comment"/>
    <result column="comment_creator_id" jdbcType="BIGINT" property="creatorId"/>
    <result column="comment_creator_name" jdbcType="VARCHAR" property="creatorName"/>
    <result column="comment_create_time" jdbcType="TIMESTAMP" property="createTime"/>
    */
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
[snowflake]:src/main/java/com/github/luomingxuorg/javautil/util/Snowflake.java "ID生成"
[college]:src/main/resources/college.json "全国高等学校名单, 提供sql与json格式两种"
[mybatisColumns]:src/main/java/com/github/luomingxuorg/javautil/util/CompleteFullColumns.java "column配置生成"