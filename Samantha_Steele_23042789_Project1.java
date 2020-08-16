import java.util.*;
import java.io.*;

public class Samantha_Steele_23042789_Project1 {
	static void eval(float c1,float c2,Stack<Character> s1,Stack<Float> s2) {
		
		if(s1.peek()=='+') {
			c1=s2.pop();
			c2=s2.pop();
			s1.pop();
			s2.push(c1+c2);
					
		} else if(s1.peek()=='-') {
			c1=s2.pop();
			c2=s2.pop();
			s1.pop();
			s2.push(c2-c1);			
		} else if(s1.peek()=='/') {
			c1=s2.pop();
			c2=s2.pop();
			s1.pop();
			s2.push(c2/c1);
					
		}else {
			c1=s2.pop();
			c2=s2.pop();
			s1.pop();
			s2.push(c1*c2);
					
		}

}

static void isDecimal(Stack<Character> s1) {
	if(!s1.empty() && s1.peek()=='d')
			s1.pop();
}

static void NegativeZero(Stack<Character>s1,Stack<Float>s2) {
	if(!s1.empty()&&s1.peek()=='n') {
			s2.push(-s2.pop());
			s1.pop();
	}
}
	
	
	
	
public static void main(String[] args) {
		
		Stack<Character> stack = new Stack<Character>();
		Stack<Float> num = new Stack<Float>();
		float f1= 0f;
		float f2=0f;
		float d =.1f;
		PrintWriter f= null;
		
		
		try {
			f=new PrintWriter(new File("project1_output.txt"));
			BufferedReader file = new BufferedReader(new FileReader("Expressions.txt"));
			String line;					
			
								//calculate each line
								while((line=file.readLine()) != null) {
										
									
									for(int i=0;i<line.length();i++) {
										char c = line.charAt(i);
										
									
										if(Character.isDigit(c)) {
								
												f1=Character.getNumericValue(c);
												//find negative <1&>0
												if(!stack.empty() && stack.peek()=='n'&& f1==0) {
													num.push(f1);
													continue;
												}
												
												//find negative
												if(!stack.empty() && stack.peek()== 'n') {
														f1*=-1;
														stack.pop();
														
												}	
												
												//decimal value
												if(!stack.empty() && stack.peek()== 'd') {
														f2=f1*d;
														
														if(num.peek()<0f) num.push(num.pop()-f2);
														else num.push(num.pop()+f2);
														d*=.1f;
														continue;
												}
											
												//multiple digit number
												if(i!=0 && Character.isDigit(line.charAt(i-1))) { 
												
														if(f2>=10) f2= f2*10+f1; //positive multi digit number
														if(f2<0f) f2=f2*10-f1;//negative multi digit number
														f1+= (Character.getNumericValue(line.charAt(i-1))*10);
														if(f2==0f)	f2=f1;
														if(num.peek()<0 && f2<100f && f2>=10f)f2=-f2;
														num.pop();
												
												}
											
											if(f2>=10||f2<0f)num.push(f2);
											else num.push(f1);
										
										}//digit
										
										if(c=='.')
											stack.push('d');
								
							
										if(c=='-'&&Character.isDigit(line.charAt(i+1))) 
											stack.push('n');
										
										
										else if(c=='(') {
											d=.1f;
											isDecimal(stack);
											NegativeZero(stack,num);
											f2=0f;
											stack.push(c);
										}
										
										else if(c=='*'||c=='/') {
											d=.1f;
											isDecimal(stack);
											NegativeZero(stack,num);
											f2=0f;
											/**
											 * checks if sign of same precedence is on top of the stack
											 * then pulls out the sign and solves by pulling the 2 most recent numbers 
											 * off the number stack operating on them then popping off of the operator 
											 * stack
											 */
												if(!stack.empty()&&(stack.peek()=='*'||stack.peek()=='/')) {
														if(stack.peek()=='*') {
															f1=num.pop();
															f2=num.pop();
															stack.pop();
															num.push(f1*f2);
															f2=0f;
														}
														else {
															f1=num.pop();
															f2=num.pop();
															stack.pop();
															num.push(f2/f1);
															f2=0f;
														}
												
														stack.push(c);
													}
									
												else 
															stack.push(c);
												
										}//*/
										
										else if(c=='+'||c=='-') {
											d=.1f;
											isDecimal(stack);
											NegativeZero(stack,num);
											f2=0f;
											/**
											 * has lowest precedence so +- only get pushed on either an
											 * empty stack or parenthesis
											 */
												if(stack.empty()||stack.peek()=='(') 
														stack.push(c);
												
												else if(!stack.empty() &&(stack.peek()=='+'||stack.peek()=='-')){
														if(stack.peek()=='+') {
																f1=num.pop();
																f2=num.pop();
																stack.pop();
																num.push(f1+f2);
																f2=0f;
														}
														else {
																f1=num.pop();
																f2=num.pop();
																stack.pop();
																num.push(f2-f1);
																f2=0f;
														}
													stack.push(c);
												}
												
												else {
														if(stack.peek()=='*') {
																f1=num.pop();
																f2=num.pop();
																stack.pop();
																num.push(f1*f2);
																f2=0f;
														}
														else {
															f1=num.pop();
															f2=num.pop();
															stack.pop();
															num.push(f2/f1);
															f2=0f;
														}
								
														stack.push(c);
												}
										}//+-
										
										else if(c==')') {
											d=.1f;
											isDecimal(stack);
											NegativeZero(stack,num);
											while(stack.peek()!='(') 
												eval(f1,f2,stack,num);
													
											f2=0f;
											if(stack.peek()=='(')
														stack.pop();
											
										}
								
													
									}//for
									
									
									f2=0f;
									isDecimal(stack);
									NegativeZero(stack,num);
									d=.1f;
									while(!stack.empty()) eval(f1,f2,stack,num);
									
									double round = Math.round(num.peek() * 100.0) / 100.0;
									System.out.println(line+" = "+round);//what i need to print
									
									while(!num.empty())num.pop();//pop the result out of the stack
									
									f.println(line+" ="+round);
									//line=file.readLine();
							
								}//while
								
					file.close();
		}//try
		catch(Exception e) {
			System.out.print("file not found");
		}
		
		f.close();
			
	}//main




}
