# Purpose
This module wraps the bitpost API so that it's easier to experiment and play with.

# Install
Check https://jitpack.io/#co.bitpost/interface-java/main-SNAPSHOT . Or, if you are using maven, add jitpack to your repositories 
and the following dependency:

```xml
<project>
<!--...-->

<dependencies>
    <!--...-->
    <dependency>
        <groupId>co.bitpost</groupId>
        <artifactId>interface-java</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>

<repositories>
    <!--...-->
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

</project>>
```



# Disclaimer
The code is distributed mostly for demo purposes, don't use it on production without proper review. Beware: HTTP requests are done synchronously!

# More info
* [API documentation](https://docs.bitpost.co)
* [OpenAPI reference](https://apidocs.bitpost.co)
* [Code examples](https://github.com/bitpostAPI/examples)
