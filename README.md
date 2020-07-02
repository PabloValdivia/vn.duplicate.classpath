# this branch for reproduce issue report at [5013](https://github.com/eclipse/jetty.project/issues/5013)

# how to run demo to figure it out
1. git clone -b jetty-5013 git@github.com:hieplq/vn.duplicate.classpath.git
1. open eclipse 2020-06 choose vn.duplicate.classpath as workspace
1. import exists project vn.duplicate.classpath.app, vn.duplicate.classpath.lib and vn.duplicate.classpath.web
1. open and active target vn.duplicate.classpath.app/mini.target
1. open debug configuration, create new launch on "OSGI Framework"
1. run that launch
1. at console type WebResource:lookup

```
osgi> WebResource:lookup
/metainfo/zk/config.xml
/metainfo/zk/config.xml
file:/mnt/data/1Dev/project/test/vn.duplicate.classpath/vn.duplicate.classpath.web/WEB-INF/lib/breeze.jar!/metainfo/zk/config.xml
```

breeze.jar inside /WEB-INF/lib so it repeat 2 time  
ckez.jar on /lib don't get repeat
