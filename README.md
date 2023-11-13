##Google Batch + mongodb

spring boot program to connect mongodb on GOOGLE Batch

1.Set environment variables(value=Method to be executed)
```
JAVA_ARGS=updateAttr
or
JAVA_ARGS=updateSubSkusWithColor
```
2.edit application.properties
```yaml
spring.data.mongodb.uri=
```
3.Execution order
```
JobCommandRunner ---> execution method(updateSubSkusWithColor or updateAttr) ---> system exit
```
