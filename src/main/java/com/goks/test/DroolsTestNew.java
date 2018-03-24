package com.goks.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.io.IOException;

import org.drools.compiler.compiler.DroolsParserException;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.goks.fortune.model.Employee;
import com.goks.fortune.model.SupportDay;

public class DroolsTestNew {
	
	int totalEmployees = 10;
	int totalDays = 10;
	
	public static void main(String[] args) throws DroolsParserException, IOException {
	
		System.out.println("Start");
		
		DroolsTestNew droolsTest = new DroolsTestNew();
		droolsTest.executeDrools();
		
		System.out.println("complete");
	
	}
	
	public void executeDrools() throws DroolsParserException, IOException {
		
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        Resource resource = kieServices.getResources().newFileSystemResource("src/main/resources/com/rule/rules.drl");
        
        kieFileSystem.write(resource);
        KieBuilder kbuilder = kieServices.newKieBuilder(kieFileSystem);
        kbuilder.buildAll();
        KieRepository kieRepository = kieServices.getRepository();
        KieContainer kContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
        
		
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
			supportDays[i].setComplete(false);
			while(!supportDays[i].isComplete()) {
				KieSession kSession = kContainer.newKieSession(); 
				supportDays[i].setFirstShiftEmpId(employees[j].getId());
				supportDays[i].setComplete(true);
				kSession.insert(employees[j]);
				kSession.insert(supportDays[i]);
				kSession.fireAllRules();
				j++;
			}
			employees[supportDays[i].getFirstShiftEmpId()].setPreviousShiftDate(supportDays[i].getToday());
			employees[supportDays[i].getFirstShiftEmpId()].setTotalShifts(employees[supportDays[i].getFirstShiftEmpId()].getTotalShifts()+1); 
			j=0;
			supportDays[i].setComplete(false);
			while(!supportDays[i].isComplete()) {
				KieSession kSession = kContainer.newKieSession();		
				supportDays[i].setLastShiftEmpId(employees[j].getId());
				supportDays[i].setComplete(true);
				kSession.insert(employees[j]);
				kSession.insert(supportDays[i]);
				kSession.fireAllRules();
				j++;
			}
			employees[supportDays[i].getLastShiftEmpId()].setPreviousShiftDate(supportDays[i].getToday());
			employees[supportDays[i].getLastShiftEmpId()].setTotalShifts(employees[supportDays[i].getLastShiftEmpId()].getTotalShifts()+1);
		}
		
		System.out.println("\n\n\t\t******Results\n");
		for(int i=0;i<supportDays.length;i++) {
			System.out.println("Date: " + supportDays[i].getToday() + " --> first shift: "+ supportDays[i].getFirstShiftEmpId() + " & second shift: "+supportDays[i].getLastShiftEmpId());
		}
	
	}

}
