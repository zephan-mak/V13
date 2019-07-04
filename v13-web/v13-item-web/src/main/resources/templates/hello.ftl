<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    hello,${username}
    <hr/>
    id:${student.id}
    name:${student.name}
    date:${student.date?date}
    date:${student.date?time}
    date:${student.date?datetime}
    <hr/>
    <#list list as stu>
        id:${student.id}
        name:${student.name}
        date:${student.date?date}
    </#list>
    <hr/>
    <#if (age>=18)>
        成年
        <#elseif (age<18)>
            未成年
        <#else >
    </#if>
    <hr/>
    password:${password!}
    password:${password!'值为空'}
    <#if password??>
        值不为null
        <#else >
        值为null
    </#if>
</body>
</html>