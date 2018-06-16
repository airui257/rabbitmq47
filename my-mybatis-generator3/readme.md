第一步：https://github.com/mybatis/generator/releases 下载插件并安装
第二步：eclipse 插件的运行方式有点特殊，JDBC驱动需要通过classPathEntry进行配置，其他定制的只要在当前项目或当前项目的classpath中即可使用。
指定数据库驱动 jar 包
<classPathEntry location="E:\repositories\mvnReposotories\mysql\mysql-connector-java\5.1.46\mysql-connector-java-5.1.46.jar"/>
第三步：
<context id="Mysql" defaultModelType="flat" targetRuntime="MyBatis3Simple">
	    <property name="beginningDelimiter" value="`"/>
    	<property name="endingDelimiter" value="`"/>
    	<property name="javaFileEncoding" value="utf-8"/>
    	在和targetProject有关的相对路径中需要增加当前的项目名称。
    	<javaModelGenerator
                targetPackage="cn.feng.dao.model"
                targetProject="my-mybatis-generator3/src/main/java">
     </javaModelGenerator>
</context>
第四步：在配置文件中单击鼠标右键，选择 Generator Mybatis/iBATIS Artifacts

注：@mbggenerated标记
	在默认的注释生成器中，生成的实体类、mapper 接口、mapper XML 文件中都有这个标记。使用eclipse插件的时候，不要手动删除注释中的这个标记，这样当重新生成表的相关代码时，这些代码都会被自动合并。
		当需要在自动生成的代码中添加自己的代码时，不要在自己添加的内容中增加 @mbggenerated 标记，这样就可以保证在重新生成时不会覆盖之前所做的改动，这个优点在给实体类增加字段、给 mapper 接口添加方法、给 mapper XML 文件添加方法时非常有用，尤其在初期改动相对频繁时特别有用。