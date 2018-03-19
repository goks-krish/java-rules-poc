package com.goks.test;

import org.openl.rules.runtime.RulesEngineFactory;

public class OpenLTest {


	public static void main(String[] args) {
		System.out.println("Start");
		
		OpenLTest openLTest = new OpenLTest();
		openLTest.executerules();
		
		System.out.println("complete");
	}
	
	private void executerules() {
		String ruleFile = "src/main/resources/com/rule/rules.xlsx";
		RulesEngineFactory<IRule> rulesFactory = new RulesEngineFactory<IRule>(ruleFile,IRule.class);
		
		IRule rules = (IRule) rulesFactory.newInstance();
		rules.hello(12);
	}

}
