package com.goks.test;

import com.goks.model.Employee;
import com.goks.model.SupportDay;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.PackageBuilder;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;
import org.drools.core.WorkingMemory;

public class DroolsTest {
	
	int totalEmployees = 10;
	int totalDays = 6;
	
	public static void main(String[] args) throws DroolsParserException, IOException {
	
		System.out.println("Start");
		
		DroolsTest droolsTest = new DroolsTest();
		droolsTest.executeDrools();
		
		System.out.println("complete");
	
	}
	
	public static Integer[] randomGenerator(int max) {
		Integer[] arr = new Integer[max];
	    for (int i = 1; i <= arr.length; i++) {
	        arr[i-1] = i;
	    }
	    Collections.shuffle(Arrays.asList(arr));
		return arr;
	}
	
	public void executeDrools() throws DroolsParserException, IOException {
		PackageBuilder packageBuilder = new PackageBuilder();

		String ruleFile = "/com/rule/Rules.drl";
		InputStream resourceAsStream = getClass().getResourceAsStream(ruleFile);

		Reader reader = new InputStreamReader(resourceAsStream);
		packageBuilder.addPackageFromDrl(reader);
		org.drools.core.rule.Package rulesPackage = packageBuilder.getPackage();
		RuleBase ruleBase = RuleBaseFactory.newRuleBase();
		ruleBase.addPackage(rulesPackage);

		
		SupportDay[] supportDays = new SupportDay[totalDays];
		Employee[] employees = new Employee[totalEmployees];
		String[] employeeNames = {"Tom","Adam", "Evan", "Adit", "Glen","Sara","Arjun","Raja","Max","Krishb"};
		
		for(int i=0;i<supportDays.length;i++) {
			supportDays[i] = new SupportDay();
			supportDays[i].setId(i);
			supportDays[i].setToday(new Date(new Date().getTime() + 86400000 + (i*86400000)).toString().substring(0, 10));
			supportDays[i].setYesterday(new Date(new Date().getTime()  + (i*86400000)).toString().substring(0, 10));
			supportDays[i].setFirstShiftEmpId(-1);
			supportDays[i].setLastShiftEmpId(-1);
			supportDays[i].setTotalEmployeeCount(totalEmployees);
		}
		
		for(int i=0;i<employees.length;i++) {
			employees[i] = new Employee();
			employees[i].setId(i);
			employees[i].setName(employeeNames[i]); //From array
		}
 		
		for(int i=0;i<supportDays.length;i++) {
			
			int j=0;
			while(supportDays[i].getFirstShiftEmpId()==-1) {
				WorkingMemory workingMemory = ruleBase.newStatefulSession();
				workingMemory = ruleBase.newStatefulSession();
				
				supportDays[i].setFirstShiftEmpId(employees[j].getId());
				workingMemory.insert(employees[j]);
				workingMemory.insert(supportDays[i]);
				workingMemory.fireAllRules();
				j++;
				
			}
			employees[supportDays[i].getFirstShiftEmpId()].setPreviousShiftDate(supportDays[i].getToday());
			employees[supportDays[i].getFirstShiftEmpId()].setTotalShifts(employees[supportDays[i].getFirstShiftEmpId()].getTotalShifts()+1); 
System.out.println(employees[supportDays[i].getFirstShiftEmpId()].getTotalShifts());
			/*j=0;			
			while(supportDays[i].getLastShiftEmpId()==-1) {
				WorkingMemory workingMemory = ruleBase.newStatefulSession();
				workingMemory = ruleBase.newStatefulSession();
				
				supportDays[i].setLastShiftEmpId(employees[j].getId());
				workingMemory.insert(employees[j]);
				workingMemory.insert(supportDays[i]);
				workingMemory.fireAllRules();
				j++;
			}
			employees[supportDays[i].getLastShiftEmpId()].setPreviousShiftDate(supportDays[i].getToday());
			*/
		}
		
		System.out.println("\n\n\t\t******Results\n");
		for(int i=0;i<supportDays.length;i++) {
			System.out.println("Date: " + supportDays[i].getToday() + " --> first shift: "+ supportDays[i].getFirstShiftEmpId() + " & second shift: "+supportDays[i].getLastShiftEmpId());
		}
		
	
	}

}
