package com.cg.ibs.investment.ui;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cg.ibs.investment.bean.AccountBean;
import com.cg.ibs.investment.bean.BankMutualFund;
import com.cg.ibs.investment.bean.Frequency;
import com.cg.ibs.investment.bean.InvestmentTransaction;
import com.cg.ibs.investment.bean.MFType;
import com.cg.ibs.investment.bean.MutualFund;
import com.cg.ibs.investment.exception.IBSException;
import com.cg.ibs.investment.service.BankService;
import com.cg.ibs.investment.service.CustomerService;
@Component
public class CustomerUi {
	static Scanner sc=new Scanner(System.in);
	//static int status = 3;
	static Logger log = Logger.getLogger(InvestmentUI.class.getName());
	


	// Declaring objects of Client Service and Bank Service
	/*CustomerService service = new CustomerServiceImpl();
	BankService bankservice = new BankServiceImpl();
	*/
	@Autowired
	CustomerService service ;
	@Autowired
	BankService bankservice ;
	
	// Customer views his/her Investments
		public void viewMyInvestments(String userId) {
			try {
				String format1 = "%1$-20s%2$-20s%3$-20s\n";

				String string7 = "Asset";
				String string8 = "Asset units";
				String string9="Asset Value";
				String string10="Gold";
				String string11="Silver";
				System.out.println(
						"---------------------------------------------------------------");
				System.out.format(format1, string7, string8, string9);
				System.out.println(
						"---------------------------------------------------------------");
				System.out.format(format1, string10,service.viewInvestments(userId).getGoldunits() , service.viewInvestments(userId).getGoldunits()*service.viewGoldPrice());
				System.out.format(format1, string11,service.viewInvestments(userId).getSilverunits() , service.viewInvestments(userId).getSilverunits()*service.viewSilverPrice());
				System.out.println("");
				System.out.println("");
				
				

				List<MutualFund> InvstmntList = new ArrayList<>(service.viewInvestments(userId).getFunds());
				List<MutualFund> ActiveinvstmntList = new ArrayList<>();
				List<MutualFund> closedinvstmntList = new ArrayList<>();
				for (int i = 0; i < InvstmntList.size(); i++) {
					if (InvstmntList.get(i).getClosingDate() == null) {
						ActiveinvstmntList.add(InvstmntList.get(i));

					} else {
						closedinvstmntList.add(InvstmntList.get(i));
					}

				}

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
				String format = "%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n";

				String string = "MfId";
				String string1 = "Title";
				String string2 = "NAV";
				String string3 = "Mf Units";
				String string4 = "Opening Date";
				String string5 = "Closing Date";
				String string6 = "NA";
				Double amount=service.viewInvestments(userId).getGoldunits()*service.viewGoldPrice()+service.viewInvestments(userId).getSilverunits()*service.viewSilverPrice();

				System.out.println(
						"-----------------------------------------------------------------------------------------------------------------");
				System.out.format(format, string, string1, string2, string3, string4, string5);
				System.out.println(
						"-----------------------------------------------------------------------------------------------------------------");
				for (int i = 0; i < ActiveinvstmntList.size(); i++) {
					MutualFund temp = ActiveinvstmntList.get(i);

					System.out.format(format, temp.getFolioNumber(), temp.getBankMutualFund().getTitle(),
							service.viewMFPlans().get(temp.getBankMutualFund().getMfPlanId()).getNav(),
							Math.round(temp.getMfUnits()*100.00)/100.00, formatter.format(temp.getOpeningDate()), string6);
					amount= amount+(service.viewMFPlans().get(temp.getBankMutualFund().getMfPlanId()).getNav()*(Math.round(temp.getMfUnits()*100.00)/100.00));

				}

				for (int i = 0; i < closedinvstmntList.size(); i++) {
					MutualFund temp = closedinvstmntList.get(i);
					System.out.format(format, temp.getFolioNumber(), temp.getBankMutualFund().getTitle(),
							service.viewMFPlans().get(temp.getBankMutualFund().getMfPlanId()).getNav(),
							Math.round(temp.getMfUnits() * 100.00)/ 100.00, formatter.format(temp.getOpeningDate()),
							formatter.format(temp.getClosingDate()));
					amount= amount+(service.viewMFPlans().get(temp.getBankMutualFund().getMfPlanId()).getNav()*(Math.round(temp.getMfUnits()*100.00)/100.00));

				}
				
				
				System.out.println(
						"-----------------------------------------------------------------------------------------------------------------");
				System.out.println("");
				System.out.println("NET ASSET WORTH:   Rs."+amount);
				System.out.println("");
				
				log.info("Investments viewed successfully");
			} catch (IBSException exp) {
				log.error(exp);
				System.out.println(exp.getMessage());
			}

		}
		// Customer sells his/her gold
		public void sellGold(String userId) {
			boolean success = true;
			System.out.println("Enter number of gold units to sell(in grams):");
			String goldUnits = sc.next();
			double GoldUnits = 0;

			try {
				while (success) {
					if (goldUnits.matches("[+]?[0-9]*\\.?[0-9]+")) {
						GoldUnits = Double.parseDouble(goldUnits);
						if (GoldUnits > 0) {

							success = false;
						} else {
							System.out.println("Please  Re-enter the value");
							goldUnits = sc.next();
						}

					} else {

						System.out.println("Please  Re-enter the value");
						goldUnits = sc.next();

					}
				}

				service.sellGold(GoldUnits, userId);
				System.out.println("You have sold "+GoldUnits+" unit of gold for Rs."+ GoldUnits*service.viewGoldPrice()+"  successfully");
				System.out.println("The available balance : Rs." +service.viewInvestments(userId).getAccount().getBalance());
				log.info("User sells gold");
			} catch (IBSException e) {
				log.error(e);
				System.out.println(e.getMessage());
			}
		}
		// Customer buys gold
		public void buyGold(String userId) {
			boolean success = true;
			System.out.println("Enter number of gold units to buy(in grams):");
			String goldUnits = sc.next();
			double GoldUnits = 0;

			try {
				while (success) {
					if (goldUnits.matches("[+]?[0-9]*\\.?[0-9]+")) {
						GoldUnits = Double.parseDouble(goldUnits);
						if (GoldUnits > 0) {

							success = false;
						} else {
							System.out.println("Please  Re-enter the value");
							goldUnits = sc.next();
						}
					} else {

						System.out.println("Please  Re-enter the value");
						goldUnits = sc.next();

					}
		

				service.buyGold(GoldUnits, userId);
				System.out.println("You have bought "+GoldUnits+" unit of gold for Rs."+ GoldUnits*service.viewGoldPrice()+"  successfully");
				System.out.println("The available balance : Rs." +service.viewInvestments(userId).getAccount().getBalance());
				log.info("User buys gold");
			} }catch (IBSException e) {
				log.error(e);
				System.out.println(e.getMessage());
			}
		}

		// Customer sells his/her Silver
		public void sellSilver(String userId) {
			boolean success = true;
			System.out.println("Enter number of silver units to sell(in grams):");
			String goldUnits = sc.next();
			double GoldUnits = 0;

			try {
				while (success) {
					if (goldUnits.matches("[+]?[0-9]*\\.?[0-9]+")) {
						GoldUnits = Double.parseDouble(goldUnits);
						if (GoldUnits > 0) {

							success = false;
						} else {
							System.out.println("Please  Re-enter the value");
							goldUnits = sc.next();
						}
					} else {

						System.out.println("Please  Re-enter the value");
						goldUnits = sc.next();

					}
				}

				service.sellSilver(GoldUnits, userId);
				System.out.println("You have sold "+GoldUnits+" unit of silver for Rs."+ GoldUnits*service.viewSilverPrice()+"  successfully");
				System.out.println("The available balance : Rs." +service.viewInvestments(userId).getAccount().getBalance());
				log.info("User sells silver");
			} catch (IBSException e) {
				log.error(e);
				System.out.println(e.getMessage());
			}

		}

		// Customer views available Mutual Fund plans
		public void viewMFPlans(String a) {
			try {
			
			String format = "%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s\n";
			String string1 = "ID";
			String string2 = "Title";
			String string3="Type";
			String string4 = "NAV(INR)";
			String string5="LAUNCH DATE";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
			
			
			System.out.println("----------------------------------------------------");
			System.out.format(format, string1, string2, string3,string4,string5);
			System.out.println("----------------------------------------------------");
			if (a == "SIP") {
				for (Entry<Integer, BankMutualFund> entry : service.viewMFPlans().entrySet()) {
					if (entry.getValue().getSipStatus() == true ) {
						System.out.format(format, entry.getValue().getMfPlanId(), entry.getValue().getTitle(), "SIP",
								entry.getValue().getNav(),formatter.format(entry.getValue().getLaunchDate()));
					}

				}
			} else if (a == "DIRECT") {
				for (Entry<Integer, BankMutualFund> entry : service.viewMFPlans().entrySet()) {
					if (entry.getValue().getStatus() == true) {
						System.out.format(format, entry.getValue().getMfPlanId(), entry.getValue().getTitle(), "DIRECT",
								entry.getValue().getNav(),formatter.format(entry.getValue().getLaunchDate()));
					}

				}
			} else if (a == "SIP_AND_DIRECT") {
				for (Entry<Integer, BankMutualFund> entry : service.viewMFPlans().entrySet()) {
					
						System.out.format(format, entry.getValue().getMfPlanId(), entry.getValue().getTitle(),
								"SIP/DIRECT", entry.getValue().getNav(),formatter.format(entry.getValue().getLaunchDate()));
					
				}
			}
				log.info("User views Mutual fund offerd by bank");
			} catch (IBSException e) {
				log.error(e);
				System.out.println(e.getMessage());
			}

		}

		// Customer buys Silver
		public void buySilver(String userId) {
			boolean success = true;
			System.out.println("Enter number of silver units to buy(in grams):");
			String goldUnits = sc.next();
			double GoldUnits = 0;

			try {
				while (success) {
					if (goldUnits.matches("[+]?[0-9]*\\.?[0-9]+")) {
						GoldUnits = Double.parseDouble(goldUnits);
						if (GoldUnits > 0) {

							success = false;
						} else {
							System.out.println("Please  Re-enter the value");
							goldUnits = sc.next();
						}
					} else {

						System.out.println("Please  Re-enter the value");
						goldUnits = sc.next();

					}
				}

				service.buySilver(GoldUnits, userId);
				System.out.println("You have bought "+GoldUnits+" unit of silver for Rs."+ GoldUnits*service.viewSilverPrice()+"  successfully");
				System.out.println("The available balance : Rs. " +service.viewInvestments(userId).getAccount().getBalance());
				log.info("user buys silver");
			} catch (IBSException e) {
				log.error(e);
				System.out.println(e.getMessage());
			}
		}

		// Customer invests in a Mutual Fund
		public void investMFPlan(String userId,String a) {

			/*String format = "%1$-20s%2$-20s%3$-20s\n";
			String string = "ID";
			String string2 = "Title";
			String string3 = "NAV(INR)";
			System.out.println("----------------------------------------------------");
			System.out.format(format, string, string2, string3);
			System.out.println("----------------------------------------------------");
			try {
				for (Entry<Integer, BankMutualFund> entry : service.viewMFPlans().entrySet()) {

					System.out.format(format, entry.getValue().getMfPlanId(), entry.getValue().getTitle(),
							entry.getValue().getNav());
				}

			} catch (IBSException e) {
				log.error(e);
				System.out.println(e.getMessage());
			}*/
			
			

			try {
				if(a=="SIP") {
					viewMFPlans(a);
					
				MutualFund mutualFund= new MutualFund();
				BankMutualFund bankMutualFund= new BankMutualFund();
				int mfId = 0;
				boolean check = true;
				boolean check1 = true;
				double mfAmount = 0;
				System.out.println("Enter the mutual fund Id:");
				String temp = sc.next();
				while (check) {

					if (temp.matches("[1-9][0-9]{2,10}")) {

						mfId = Integer.parseInt(temp);
						if (service.viewMFPlans().containsKey(mfId)) {

							check = false;
						} else {
							System.out.println("Please enter valid MfId");
							temp = sc.next();

						}

					}

					else {
						System.out.println("Please Re-enter the value");
						temp = sc.next();

					}

				}
				bankMutualFund=service.viewMFPlans().get(mfId);
				mutualFund.setBankMutualFund(bankMutualFund);
				
				
				System.out.println("Enter the frequency");
				System.out.println("1.MONTHLY");
				System.out.println("2.QUARTERLY");
				System.out.println("3.HALFYEARLY");
				System.out.println("4.ANNUALLY");
				System.out.println("5.DAILY");
				System.out.println("Enter the choice number of the frequency");
				check1=true;
				
				 String tempUpdate = sc.next();
					int stat = 0;
					while (check1) {
						if (tempUpdate.matches("[1-5]")) {
							stat= Integer.parseInt(tempUpdate);
							
								check1 = false;
							
						} else {
							System.out.println("Please Re-enter the value");
							tempUpdate = sc.next();
						}

					}
					
				if(stat==1) {
					mutualFund.setFrequency(Frequency.MONTHLY);					
					System.out.println("Enter the  duration in number of months");
					check1=true;
					
					 tempUpdate = sc.next();
						int month = 0;
						while (check1) {
							if (tempUpdate.matches("[1-9]")) {
								month= Integer.parseInt(tempUpdate);
								
									check1 = false;
								
							} else {
								System.out.println("Please Re-enter the value");
								tempUpdate = sc.next();
							}

						}
						mutualFund.setDuration(month);

					
					
					
				}
				else if(stat==2) {
					mutualFund.setFrequency(Frequency.QUATERLY);
					System.out.println("Enter the duration in number of quarters");
					check1=true;
					
					 tempUpdate = sc.next();
						int quarter = 0;
						while (check1) {
							if (tempUpdate.matches("[1-9]")) {
								quarter= Integer.parseInt(tempUpdate);
								
									check1 = false;
								
							} else {
								System.out.println("Please Re-enter the value");
								tempUpdate = sc.next();
							}

						}
						mutualFund.setDuration(quarter);

					
				}
				else if(stat==3) {
					mutualFund.setFrequency(Frequency.HALFYEARLY);
					System.out.println("Enter the number of half-years");
					check1=true;
					
					 tempUpdate = sc.next();
						int half_years = 0;
						while (check1) {
							if (tempUpdate.matches("[1-9]")) {
								half_years= Integer.parseInt(tempUpdate);
								
									check1 = false;
								
							} else {
								System.out.println("Please Re-enter the value");
								tempUpdate = sc.next();
							}

						}
						mutualFund.setDuration(half_years);

				}
				else if(stat==4) {
					mutualFund.setFrequency(Frequency.ANNUALLY);
					System.out.println("Enter the number of years");
					check1=true;
					
					 tempUpdate = sc.next();
						int years = 0;
						while (check1) {
							if (tempUpdate.matches("[1-9]")) {
								years= Integer.parseInt(tempUpdate);
								
									check1 = false;
								
							} else {
								System.out.println("Please Re-enter the value");
								tempUpdate = sc.next();
							}

						}
						mutualFund.setDuration(years);

				}
				else if(stat==5) {
					mutualFund.setFrequency(Frequency.DAILY);
					System.out.println("Enter the number of days");
					check1=true;
					
					 tempUpdate = sc.next();
						int days = 0;
						while (check1) {
							if (tempUpdate.matches("[1-9]")) {
								days= Integer.parseInt(tempUpdate);
								
									check1 = false;
								
							} else {
								System.out.println("Please Re-enter the value");
								tempUpdate = sc.next();
							}

						}
						mutualFund.setDuration(days);

				}

				
				
				

				System.out.println("Enter the amount to invest");
				temp = sc.next();
				check1=true;
				while (check1) {
					if (temp.matches("[+]?[0-9]*\\.?[0-9]+")) {
						mfAmount = Double.parseDouble(temp);
						if (mfAmount > 0 && mfAmount>service.viewMFPlans().get(mfId).getMinAmtSip()) {
							check1 = false;
						} else {
							System.out.println("Please Re-enter the value");
							temp = sc.nextLine();

						}

					} else {
						System.out.println("Please Re-enter the value");
						temp = sc.next();

					}

				}
				System.out.println("Enter the date you want to start the investment in the format DD/MM/YYYY");
				
				String date= null;
				check1=true;
				boolean check2= true;
			    while(check1) {
			    	 date= sc.next();
			    	 while(check2) {
			    		 if(date.matches("[^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$]")) {
			    			check2=false; 
			    		 }
			    		 System.out.println("Enter the date again");
			    		 date=sc.next();
			    	 }
			      
			    	 
			    	 
			    	 
					String arr[]=date.split("[-]");
					int days= Integer.parseInt(arr[0]);
					int month= Integer.parseInt(arr[1]);
					int year=Integer.parseInt(arr[2]);
			     
				if(isValidDate(days, month, year)) {
					check1=false;
					
					
				}
				System.out.println("Please enter valid date");
				date= sc.next();
				
			    }
				LocalDate buydate=new SimpleDateFormat("dd/MM/yyyy").parse(date);
				mutualFund.setBuyDate(buyDate);
				 
				service.investSipMF(userId, mutualFund);
				
				
	
				System.out.println("transaction completed");}
				else if(a=="DIRECT") {
					viewMFPlans(a);
					MutualFund mutualFund= new MutualFund();
					BankMutualFund bankMutualFund= new BankMutualFund();
					int mfId = 0;
					boolean check = true;
					boolean check1 = true;
					double mfAmount = 0;
					System.out.println("Enter the mutual fund Id:");
					String temp = sc.next();
					while (check) {

						if (temp.matches("[1-9][0-9]{2,10}")) {

							mfId = Integer.parseInt(temp);
							if (service.viewMFPlans().containsKey(mfId)) {

								check = false;
							} else {
								System.out.println("Please enter valid MfId");
								temp = sc.next();

							}

						}

						else {
							System.out.println("Please Re-enter the value");
							temp = sc.next();

						}

					}
					
					System.out.println("Enter the amount to invest");
					temp = sc.next();
					check1=true;
					while (check1) {
						if (temp.matches("[+]?[0-9]*\\.?[0-9]+")) {
							mfAmount = Double.parseDouble(temp);
							if (mfAmount > 0 && mfAmount>service.viewMFPlans().get(mfId).getMinAmtSip()) {
								check1 = false;
							} else {
								System.out.println("Please Re-enter the value");
								temp = sc.nextLine();

							}

						} else {
							System.out.println("Please Re-enter the value");
							temp = sc.next();

						}

					}
					//function of service
					service.investDirMF(mfAmount, userId, mfId);


					
				}
			
				log.info("User invests in mutual fund");
			} catch (IBSException e) {
				log.error(e);
				System.out.println(e.getMessage());

			}
		}
       
		
		
		static boolean isLeap(int year) 
	    { 
	        // Return true if year is  
	        // a multiple of 4 and not  
	        // multiple of 100. 
	        // OR year is multiple of 400. 
	        return (year % 4 == 0) ; 
	    }
		
	    static boolean isValidDate(int d,  
                int m,  
                int y) 
	    { 
	        // If year, month and day  
	        // are not in given range 
	         
	        if (m < 1 || m > 12) 
	            return false; 
	        if (d < 1 || d > 31) 
	            return false; 
	  
	        // Handle February month 
	        // with leap year 
	        if (m == 2)  
	        { 
	            if (isLeap(y)) 
	                return (d <= 29); 
	            else
	                return (d <= 28); 
	        } 
	  
	        // Months of April, June,  
	        // Sept and Nov must have  
	        // number of days less than 
	        // or equal to 30. 
	        if (m == 4 || m == 6 ||  
	            m == 9 || m == 11) 
	            return (d <= 30); 
	  
	        return true; 
	    } 
	  
		
		// Customer withdraws from a Mutual Fund
		public void withdrawMFPlan(String userId) {
			
			MutualFund mutualFund = null;
			boolean check = true;
			try {
				String a="DIRECT";
				viewMFPlans(a);
				
				
				System.out.println("Enter the mutual fund Id:");
				String temp = sc.next();
				
				int mfId=0;
				int count=0;
				Set<MutualFund> funds= service.viewInvestments(userId).getFunds();
				for (MutualFund f:funds) {
					if(f.getType()==MFType.DIRECT) {
						count=count+1;
					}
				}
				
				if(count>0) {
				while (check) {

					if (temp.matches("[1-9][0-9]{2,10}")) {

						mfId = Integer.parseInt(temp);
						if (service.viewMFPlans().containsKey(mfId)) {

							check = false;
						} else {
							System.out.println("Please enter valid MfId");
							temp = sc.next();

						}

					}

					else {
						System.out.println("Please Re-enter the value");
						temp = sc.next();

					}

				}
				for(MutualFund f:funds) {
					if(f.getBankMutualFund().getMfPlanId()==mfId&&f.getType()==MFType.DIRECT) {
					mutualFund=f;
					}
				}
				service.withdrawDirMF(userId, mutualFund);
					System.out.println("transaction completed");
				} else {
					System.out.println("Sorry!! you have to buy new plans");
				}
				log.info("user withdraws from mutual fund");
			} catch (IBSException exp) {
				log.error(exp);
				System.out.println(exp.getMessage());
			}

		}

		
		
		
		
		

		public void viewMyTransactions(String userId) {
			try {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY hh:mm");
				String format = "%1$-10s%2$-20s%3$-35s%4$-20s%5$-20s%6$-20s\n";

				String string = "TransId";
				String string1 = "TransDate";
				String string2 = "Description";
				String string3 = "Amount(INR)";
				String string4 = "Type";
				String string5 = "Units";
				String string6 = "PricePerUnit/NAV(INR)";

				System.out.println(
						"-------------------------------------------------------------------------------------------------------------------");
				System.out.format(format, string, string1, string2, string3, string4, string5, string6);
				System.out.println(
						"-------------------------------------------------------------------------------------------------------------------");

				List<InvestmentTransaction> tsBeans = service.getTransactions(userId);
				for (InvestmentTransaction t : tsBeans) {

					System.out.format(format, t.getTransactionId(), formatter.format(t.getTransactionDate()),
							t.getTransactionDescription(), t.getTransactionAmount().setScale(2, BigDecimal.ROUND_DOWN),
							t.getTransactionType(), Math.round(t.getUnits() * 100.00) / 100.00, t.getPricePerUnit());
				}

				log.info("User views transaction");
			} catch (IBSException e) {
				log.error(e);
				System.out.println(e.getMessage());
			}

		}

		public void linkAccount(String userId) {
			try {
				int i = 1;
				boolean check = true;
				for (AccountBean a : service.getAccountList(userId)) {
					System.out.println(
							i + "\t\t" + a.getAccNo() + "\t\t" + a.getBalance().setScale(2, BigDecimal.ROUND_DOWN));
					i++;
				}
				System.out.println("Choose the account number you want to link");

				String temp = sc.next();
				int a = 0;
				while (check) {
					if (temp.matches("[0-9]{1,2}")) {
						a = Integer.parseInt(temp);
						if (a <= service.getAccountList(userId).size() && a > 0) {
							check = false;
						} else {
							System.out.println("Please Re-enter the choice");
							temp = sc.next();

						}

					} else {

						System.out.println("Please Re-enter the choice");
						temp = sc.next();

					}
				}

				BigInteger accountNumber = service.getAccountList(userId).get(a - 1).getAccNo();
				System.out.println("You have chosen the account---" + accountNumber.toString());
				service.linkMyAccount(accountNumber, userId);
			} catch (IBSException e) {
				System.out.println(e.getMessage());

			}

		}	
}
