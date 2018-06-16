package cn.feng;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class Generator {

	public static void main(String[] args) throws Exception {
		System.out.println("99999999999999");
		// MBG 执行过程中的警告信息
		List<String> warnings = new ArrayList<>();
		// 当生成的代码重复时，覆盖原代码
		boolean overwrite = true;
		// 读取 MBG 配置文件
		InputStream inputStream = Generator.class.getResourceAsStream("/generator/generatorConfig.xml");

		ConfigurationParser configurationParser = new ConfigurationParser(warnings);

		Configuration configuration = configurationParser.parseConfiguration(inputStream);

		// 关闭流
		inputStream.close();

		DefaultShellCallback shellCallback = new DefaultShellCallback(overwrite);

		// 创建 MBG
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, shellCallback, warnings);

		// 执行生成代码
		myBatisGenerator.generate(null);

		// 输出警告信息
		for (String warning : warnings) {
			System.out.println(warning);
		}
		System.out.println("00000000");
	}
}
