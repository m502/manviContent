package com.cg.ibs.im.ui;

import java.util.Scanner;

import com.cg.ibs.im.service.ClientService;


public class InvestmentUI {
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		System.out.println("	Press 1 for customer and 2 for bank representative");
		System.out.println("--------------------");
		int num= sc.nextInt();
		
			Menu choice = null;
			BankMenu option=null;
		System.out.println("Enter the userId");
		String userId= sc.next();
		System.out.println("Enter the password");
		String password= sc.next();
		ClientService service= new ClientService();
		if(num==1){
		if(service.isValid(userId, password)){
		while (choice != Menu.Quit) {
		System.out.println("Choice");
		System.out.println("--------------------");
		for (Menu menu : Menu.values()) {
			System.out.println(menu.ordinal() + "\t" + menu);
		}
		System.out.println("Choice");
		
		int ordinal = sc.nextInt();
		if (ordinal >= 0 && ordinal < Menu.values().length) {
			choice = Menu.values()[ordinal];

			switch (choice) {
			case viewMyInvestment:
				service.isValid(userId, password);
				break;
			case viewGoldPrice:
				
				break;
			case viewSilverPrice:
				
				break;
			case viewMFplans:
				
				break;
			case buyGold:
				
				break;
            case sellGold:
				
				break;
            case buySilver:
				
				break;
           case sellSilver:
				
				break;
           case depositMFplan:
				
				break;
           case WithdrawMFplan:
				
				break;
           case Quit:
        	   System.out.println("thank you for coming");
        	   break;
			}
		} else {
			System.out.println("Invalid Option!!");
			choice = null;

		}

	}}
		
}
		else if(num==2){
			while(option!=BankMenu.Quit){
				System.out.println("Choice");
				System.out.println("--------------------");
				for (BankMenu menu : BankMenu.values()) {
					System.out.println(menu.ordinal() + "\t" + menu);
				}
				System.out.println("Choice");
				
				int ordinal = sc.nextInt();
				if (ordinal >= 0 && ordinal < BankMenu.values().length) {
					option = BankMenu.values()[ordinal];

					switch (option) {
					case updateGoldPrice:
						service.isValid(userId, password);
						break;
					case updateSilverPrice:
						
						break;
					case updateMFplans :
						
						break;
					case Quit:
						
						break;
			
		}
		sc.close();
		}}}}}
	
		
		
	


