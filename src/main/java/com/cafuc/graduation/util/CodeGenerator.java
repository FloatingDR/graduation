package com.cafuc.graduation.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * mybatis-plus 代码生成辅助类
 * </p>
 *
 * @author taylor
 * @date 2020/2/17 19:08
 */
public class CodeGenerator {

    /**
     * 作者名
     */
    private static final String AUTHOR = "flyeat";

    /**
     * 工程目录
     */
    private static final String PROJECT_PATH = "/Users/taylor/springboot/graduation/";

    /**
     * 模块名
     */
    private static final String MODULE_NAME = "user";

    /**
     * 表名
     */
    private static final String TABLE_NAME = "user_interface_confine";

    /**
     * 包名
     */
    private static final String PACKAGE = "com.cafuc.graduation";

    /**
     * 数据源配置
     */
    private static final String URL = "jdbc:mysql://localhost:3306/graduation_photo?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8&useSSL=true";
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String USERNAME = "taylor";
    private static final String PASSWORD = "taylor";


    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = PROJECT_PATH;
        globalConfig.setOutputDir(projectPath + "src/main/java")
                .setAuthor(AUTHOR)
                .setOpen(false)
                // XML ResultMap
                .setBaseResultMap(true)
                // XML columLisL
                .setBaseColumnList(false)
                .setBaseResultMap(true)

                .setSwagger2(true);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(URL)
                .setDriverName(DRIVER_NAME)
                .setUsername(USERNAME)
                .setPassword(PASSWORD);

        // 包配置
        PackageConfig packageInfo = new PackageConfig();
        packageInfo.setModuleName(MODULE_NAME)
                .setEntity("entity.po")
                .setXml("mapper")
                .setMapper("dao")
                .setParent(PACKAGE);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置,如果自定义mapper生成路径可以配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" +
                        "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        //  自定义配置开启
//        cfg.setFileOutConfigList(focList);


        globalConfig.setEntityName("%sPo");
        globalConfig.setMapperName("%sDao");
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(new TemplateConfig().getXml());

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                .setSuperEntityClass("java.io.Serializable")
                .setEntitySerialVersionUID(true)
                .setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper")
                .setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl")
                .setInclude(TABLE_NAME).setControllerMappingHyphenStyle(true)
                .setTablePrefix(packageInfo.getModuleName() + "_");

        autoGenerator.setStrategy(strategy)
                .setPackageInfo(packageInfo)
                .setTemplate(templateConfig)
                .setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setTemplateEngine(new FreemarkerTemplateEngine())
                .setCfg(cfg)
                .execute();
    }

}
