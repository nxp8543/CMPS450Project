package edu.louisiana.cacs.csce450Project;

import java.io.BufferedReader;
import java.io.CharConversionException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.commons.lang3.StringUtils;

public class Parser{
	
	
	
	/* for reading file */
	FileReader reader = null;
	BufferedReader br ;
	
	
	/*stack for storing after shift action 
	 * In this we store element is integer array holding two elements
	 * in an array we store grammar at index 0 and symbol at index 1 and then this array is stored as an element in stack
	 */
	Stack<String> s_stack = new Stack<String>(); 
	
	/*for storing input string to parse ,
	 * we are going to use a Queue
	 */
	Queue<String> inputtokens_queue = new LinkedList<String>();
	
	
	
	/* A temporary stack to store elements of "s_stack" after reduce operation*/
	Stack<String> temp_Stack = new Stack<String>();
	
	/* this is the variable holding the action lookup value and this is also used as a condition to halt the program*/
	String[] action_lookup ={"0","0"};
	
	/* this variable holds current action value to look up in action table*/
	String action_value = "";
	
	/* this value stores the value of goto look up value*/
	String[] goto_lookup={"0","0"};
	
	/* this variable stores the length of LHS*/
	String value_Of_LHS= "";
	
	/* this variable stores the length of RHS of the rule */
	int length_RHS=0;
	
	/* this variable stores the goto value in Goto table */
	String goto_value =null;
	/* action to be performed on the stack*/
	
	/* this is used to store the parse tree stack*/
	Stack<String> parse_tree_stack = new Stack<String>();
	
	/* two dimensional array to hold elements of Action and goto table of size 12x9 */
	String[][] action_table = new String[12][9] ;
	String[][] goto_table = new String[12][3] ;
	
	HashMap<String,Integer> column_vlaues = new HashMap<String,Integer>();
	String[] rule1 = {"E","E","+","T"};
	String[] rule2 = {"E","T"};
	String[] rule3 = {"T","T","*","F"};
	String[] rule4 = {"T","F"};
	String[] rule5 = {"F","(","E",")"};
	String[] rule6 = {"F","id"};
	
	int t__count=0;
	public Parser(String fileName)
	{
		
		
		
		
		//System.out.println("File to parse : "+fileName);
		System.out.println("Stack\t"+"\tinput tokens\t\t"+"action lookup\t"+"action value\t"+"value of LHS\t"+"length of RHS\t"+"temp stack\t"+"goto lookup\t"+"goto value\t"+"stack action\t"+"parse tree stack");
		
		System.out.println("_________________________________________________________________________________________________________________________________________________________________________________________________________________________");
		
		//get input string
		SebestaScannerMain ss = new SebestaScannerMain();
		inputtokens_queue=ss.getInputData(fileName);
		//System.out.println(inputtokens_queue);
		
		/*initializing action_table table with empty string
		 * column name     corresponding column values
		 * ------------    ---------------------------
		  	id					0
		  		
		  	+					1
		  
		  	*					2
		  
		  	(					3
		 
		  	)					4
		  
		  			
		  	$					5
		  
		  	E					6
		  
		  	T					7
		  	
		  	F                   8
		  
		  
		 */
		
		/*
		 * 
		 * 
		 * READING DATA FROM external file
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
		
		
		for( int i = 0 ; i < 12 ; i++ )
		{
			for( int j = 0 ; j < 9 ;j++ )
			{
				action_table[i][j]=null;//initializing with empty string
			}
		}
		
		column_vlaues.put("id",0);
		column_vlaues.put("+",1);
		column_vlaues.put("*",2);
		column_vlaues.put("(",3);
		column_vlaues.put(")",4);
		column_vlaues.put("$",5);
		column_vlaues.put("E",6);
		column_vlaues.put("T",7);
		column_vlaues.put("F",8);

		/*initializing goto_table table with empty string
		 * column name     corresponding column values
		 * ------------    ---------------------------
		  	E					0
		  		
		  	T					1
		  
		  	F					2
		  */

		for( int i = 0 ; i < 12 ; i++ )
		{
			for( int j = 0 ; j < 3 ;j++ )
			{
				goto_table[i][j]=" ";//initializing with empty string
			}
		}
		  
		//external initialization of action_table array
			//row -0
			action_table[0][0] = "S5";
			action_table[0][3] = "S4";
			
			
			action_table[0][6] = "1";
			action_table[0][7] = "2";
			action_table[0][8] = "3";
			
			//row-1
			action_table[1][1] = "S6";
			action_table[1][5] = "accept";
			
			//row-2
			action_table[2][1] = "R2";
			action_table[2][2] = "S7";
			action_table[2][4] = "R2";
			action_table[2][5] = "R2";
			
			//row-3
			action_table[3][1] = "R4";
			action_table[3][2] = "R4";
			action_table[3][4] = "R4";
			action_table[3][5] = "R4";
			
			//row-4
			action_table[4][0] = "S5";
			action_table[4][3] = "S4";
			action_table[4][6] = "8";
			action_table[4][7] = "2";
			action_table[4][8] = "3";
			
			//row-5
			
			action_table[5][1] = "R6";
			action_table[5][2] = "R6";
			action_table[5][4] = "R6";
			action_table[5][5] = "R6";
			
			//row-6
			action_table[6][0] = "S5";
			action_table[6][3] = "S4";
			action_table[6][7] = "9";
			action_table[6][8] = "3";
			
			//row -7
			action_table[7][0] = "S5";
			action_table[7][3] = "S4";
			action_table[7][8] = "10";
			
			//row- 8
			action_table[8][1] = "S6";
			action_table[8][4] = "S11";

			//row -9
			action_table[9][1] = "R1";
			action_table[9][2] = "S7";
			action_table[9][4] = "R1";
			action_table[9][5] = "R1";
			
			//row -10
			action_table[10][1] = "R3";
			action_table[10][2] = "R3";
			action_table[10][4] = "R3";
			action_table[10][5] = "R3";
			
			//row -11
			action_table[11][1] = "R5";
			action_table[11][2] = "R5";
			action_table[11][4] = "R5";
			action_table[11][5] = "R5";
			
		
		
		s_stack.push("0");
		
		
		
		
		
	}//end of constructor
	/*
	* Dummy code
	*/
	public void printParseTree(){
		
		//System.out.println("In printPaseTree method");
		int count=0;
		Pattern p = Pattern.compile("[A-Za-z*]");
		 Matcher m ;
		while(!(action_value.equalsIgnoreCase("accept"))) 
		{
			//count++;
			String input_stack ="";
			for(String e:s_stack)
			{
				input_stack =input_stack.concat(e);
			}
			
			
			
			//String input_string = StringUtils.join(inputtokens_queue, ", ");
			//input_string = input_string.replaceAll(",","");
			String input_string="";
			for(String s:inputtokens_queue){
				input_string+=s+" ";
			}
			
			
			if(s_stack.peek() != null )
			{
				//System.out.print("[");
				if(s_stack.size()==1)
				{
					
					action_lookup[0] = s_stack.peek();
					
					
				}
				else
				{
					if(s_stack.peek().length()==2)
						action_lookup[0] =Character.toString(s_stack.peek().charAt(1));
					else
					{
						int c_length = s_stack.peek().length() ;
						String s_peek = s_stack.peek();
						int i=0;
						i= index_of_digit(s_peek);
						action_lookup[0] = s_stack.peek().substring(i);
						
					}
				}
				
				
				action_lookup[1] = inputtokens_queue.peek();
				
				action_value = action_table[Integer.parseInt(action_lookup[0])][column_vlaues.get(action_lookup[1])];
			
				if(action_value.contains("S"))
				{
					
					
					if(action_value.trim().length()==2)//ex: S5
						s_Action(action_lookup[1],String.valueOf(action_value.charAt(1)));
					
					else if(action_value.trim().length()>2)//Ex: S11
					{
						s_Action(action_lookup[1],action_value.substring(1));
					}
				}
				else if(action_value.contains("R"))//if action_value contains 'R'
				{
					if(action_value.trim().length()==2)//ex: S5
						r_Action(action_lookup[1],String.valueOf(action_value.charAt(1)));
					
					else if(action_value.trim().length()>2)//Ex: S11
					{
						r_Action(action_lookup[1],action_value.substring(1));
					}
					
				}
				
				
				
				
				
			}
			
			
			/* printing elements of parse tree stack*/
						
			int s_size=parse_tree_stack.size();
			String all = "";
			
			while(s_size > 0)
			{
				
				all = all.concat(parse_tree_stack.get(s_size-1));
				
				s_size--;
			}
			
			String output_temp_stack="";
			for(String e:temp_Stack)
			{
				output_temp_stack= output_temp_stack.concat(e);
			}


						
			if(action_value.contains("S"))
						
			{	
				
				
				System.out.format("%-14s%-25s%-9s%-20s%-9s%-9s%-30s%-14s%-14s%-14s%-50s",input_stack,input_string,Arrays.toString(action_lookup),action_value,"","","","","",s_stack.peek(),all);
			
			}
			else if(action_value.contains("R"))
				System.out.format("%-14s%-25s%-9s%-20s%-9s%-9d%-30s%-14s%-14s%-14s%-50s",input_stack,input_string,Arrays.toString(action_lookup),action_value,value_Of_LHS,length_RHS,output_temp_stack,Arrays.toString(goto_lookup),goto_value,s_stack.peek(),all);
			
			else
				System.out.format("%-14s%-25s%-9s%-20s%-122s",input_stack,input_string,Arrays.toString(action_lookup),action_value,all);
			
				
			temp_Stack.removeAllElements();
			System.out.println();
			
		}//end of while loop
		
		
		
		/* printing parse tree for the grammar*/
		
		System.out.println("\n\n");
		int square_brackets=1;
		boolean flag= false;
		Stack<String> s = new Stack<String>();
		String data = parse_tree_stack.peek();
		String e="id";
		boolean isid=false;
		for(int i=0;i<data.length();i++)
		{
			
			//System.out.println(Character.toString(data.charAt(i)));
			
			s.push(Character.toString(data.charAt(i)));
		}
		
			for(int i=0;i<s.size();i++)
			{
				
				//System.out.println("In for loop");
				if(s.get(i).matches("[\\]]"))
				{
					
						square_brackets--;
					
					
				}
				if(s.get(i).matches("[\\[]"))
				{
					//System.out.println("In open bracket");
					if(flag==true)
					{
						square_brackets++;
					}
					if(flag == false)
					{
						flag = true;
					}
					
				}
				if(s.get(i).matches("[A-Za-z*+]"))
				{
					int c = square_brackets;
					//System.out.println("In string matcjing");
					if(s.get(i).matches("[*+]"))
					{
					
						while(c>0)
						{
							System.out.print("\t");
							c--;
						}
						System.out.print(s.get(i));
					}
					else
					{
						while(c>0)
						{
							System.out.print("\t");
							c--;
						}
						System.out.print(s.get(i));
					}
					System.out.println();
				}
				
				
			}
		//}
		
		
		
	}

	/*9492466243
	* Dummy code
	*/
	public void parse(){
		//System.out.println("In parse method");
        printParseTree();
	}
	
	String action_Lookup(String row, String col)
	{
		//System.out.println("row="+row+"\t"+"column="+col+"\t modified col value ="+column_vlaues.get(col));
		return action_table[Integer.parseInt(row)][column_vlaues.get(col)];
	}
	
	void s_Action(String arg1,String arg2)
	{
		//remove first element of queue
		inputtokens_queue.remove();
		//knowing the rule array
		//String[] rule_value = find(Integer.parseInt(arg2));
		//System.out.print("\t\t\t\t");//==========================================================================================value of LHS
		//System.out.print("\t\t\t\t");//==========================================================================================length of RHS
		//System.out.print("\t\t\t\t");//==========================================================================================temp stack
		//System.out.print("\t\t\t\t");//==========================================================================================goto value
		//System.out.println("In s_Action method");
		//System.out.println("arg1="+arg1);
		//System.out.println("arg2="+arg2);
		//push "arg1arg2" into s_stack
		String s_data = arg1.concat(arg2);
		//System.out.print("push\t"+s_data);//================================================================================================stack action
		s_stack.push(s_data);
		
		//System.out.println("Push :"+s_data);
		
		if(parse_tree_stack.size()<1)
		{
			parse_tree_stack.push("id");
		//	System.out.print(parse_tree_stack);//==================================================================================parse tree stack
		}
		else if(!arg1.matches("^[*+\\(\\)]"))
		{
			//adding to parse tree stack
			add_ParseTree_Stack(arg1,arg2);
			//System.out.print(parse_tree_stack);//==================================================================================parse tree stack
		}
		
		//System.out.println("s_stack ="+s_stack);
	}//s_Action method
	void r_Action(String arg1, String arg2)
	{
		//System.out.println("In r_action method");
		//knowing the rule array
		String[] rule_value = find(Integer.parseInt(arg2));
		
		
		
		//value of LHS of rule value contained in 'action_value' variable
		value_Of_LHS = rule_value[0];
		//System.out.print(value_Of_LHS+"\t\t\t");//=================================================================================value of LHS
		length_RHS = rule_value.length-1;
		//System.out.print(length_RHS+"\t\t\t");//===================================================================================length of RHS
		//contents of stack before pop operation on stack
		//System.out.println("Before poping elements of the stack ="+Arrays.toString(s_stack.toArray()));
		//pop stack elements as many times as length of RHS of rule
		int pop_count=length_RHS;
		while(pop_count>0)
		{
			//System.out.print("poped element :"+s_stack.pop()+"\t\t\t");
			s_stack.pop();
			pop_count--;
		}
		//temp_Stack = s_stack;
		
		for(String e:s_stack)
		{
			temp_Stack.push(e);
		}
		
		//System.out.print(t__count+"\t\t\t"+output_temp_stack+"\n");
		//System.out.println(temp_Stack+"\t\t\t");//=================================================================================temp stack
		int i = index_of_digit(temp_Stack.peek());
		//finding goto_lookup value
		goto_lookup[0] = temp_Stack.peek().substring(i);
		goto_lookup[1] = value_Of_LHS;
		
	//	System.out.print(goto_lookup+"\t\t\t");//================================================================================goto lookup
		//System.out.println("Goto lookup:"+goto_lookup[0]+goto_lookup[1]);
		//finding goto_value from table by using goto_lookup array value
		goto_value = action_Lookup(goto_lookup[0], goto_lookup[1]);
		//System.out.print("\t\t"+goto_value);
	//	System.out.print(goto_value+"\t\t\t");//===================================================================================goto value
		
		
	//	System.out.print("push\t"+goto_lookup[1].concat(goto_value));//============================================================stack action
		//System.out.println("stack action : push\t"+goto_lookup[1]+goto_value);
		//push column value of action_Table from previous step and got0_value together in s_stack
		String[] push_array = {goto_lookup[1],goto_value};
		s_stack.push(goto_lookup[1].concat(goto_value));
		//System.out.println("push\t"+Arrays.toString(push_array).trim());
		//adding to parse tree stack
		add_ParseTree_Stack(goto_lookup[1],arg2);
		//System.out.print(parse_tree_stack);//====================================================================================parse tree stack
		
		
		
	}//end of r_Action method
	
	String[] find(int r)
	{
		String[] array;
		if(r==1)
		{
			array = rule1;
		}
		else if(r==2)
		{
			array =rule2;
		}
		else if(r==3)
		{
			array =rule3;
		}
		else if(r==4)
		{
			array =rule4;
		}
		else if(r==5)
		{
			array =rule5;
		}
		else
			array = rule6;
		return array;
	}
	
	int index_of_digit(String s)
	{ 
		int i=0;
		for(; i<s_stack.peek().length();i++)
		{
			int s_peek_int = Character.getNumericValue(s.charAt(i));
			//System.out.println("int value of charcater:"+s.charAt(i)+s_peek_int);
			if(s_peek_int >= 0 && s_peek_int <=9)
			{
				
				break;
			}
		}
		return i;
	}// end of method
	
	void add_ParseTree_Stack(String element, String rule)
	{
		String s_data;
		
		if(element.equalsIgnoreCase("id"))
		{
			parse_tree_stack.push(element);
			
		}
		else
		{
			String ultimate ;
			String penultimate;
			if(rule.equals("3"))
			{
				/*pop top two elements of the stack and add element to the 2nd top most element
				 *  and combine them with * as 2nd top most is on left side of parse tree and top most
				 *  is on right side 
				 */
				ultimate = parse_tree_stack.pop();
				penultimate = parse_tree_stack.pop();
				
				element = "["+element;
				penultimate = element.concat(penultimate);
				penultimate = penultimate.concat("*"+ultimate+"]");
				
				parse_tree_stack.push(penultimate);
				
			}
			else if(rule.equals("1"))
			{
				ultimate = parse_tree_stack.pop();
				penultimate = parse_tree_stack.pop();
				
				element = "["+element;
				penultimate = element.concat(penultimate);
				penultimate = penultimate.concat("+"+ultimate+"]");
				
				parse_tree_stack.push(penultimate);
			}
			else if(rule.equals("5"))
			{
				ultimate = parse_tree_stack.pop();
				element = "[".concat(element);
				element = element.concat("(");
				
				ultimate =element.concat(ultimate).concat(")").concat("]");
				parse_tree_stack.push(ultimate);
			}
			else
			{
				s_data = parse_tree_stack.pop();
				s_data = element.concat(s_data);
				parse_tree_stack.push("["+s_data+"]");
			}	
		}
	}
	
}
