package com.ceb.platform.autumnframework.cebfreemarkergenerator;

import com.ceb.platform.autumnframework.cebfreemarkergenerator.util.CodeGeneratorUtil;

/**
 * @description 创建模板
 * @author Administrator
 *
 */
public class MainStarter {

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		System.out.println("start");
		CodeGeneratorUtil cgu = new CodeGeneratorUtil();
		try {
			cgu.generate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}

}
