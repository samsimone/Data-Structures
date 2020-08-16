import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;


public class Samantha_Steele_23042789_Project2 {

	public class Node {
		
		String data;
		Node left, right;
		
		Node(String d){
			data=d;
			left=null;
			right=null;		
		}
		
		
		 public class ExpressionTree {
			
			//delete d from the top of the stack
			void isDecimal(Stack<Character> s1) {
				if(!s1.empty() && s1.peek()=='d')
						s1.pop();
			}

			//delete n from the top of the stack
		    void NegativeZero(Stack<Character>s1,Stack<Float>s2) {
				if(!s1.empty()&&s1.peek()=='n') {
						s2.push(-s2.pop());
						s1.pop();
				}
			}
				
			/**
			 * takes input string from file and creates an arraylist
			 * of nodes
			 * @param s
			 * @return
			 */
			public ArrayList<Node> postorder(String s){
				ArrayList<Node>p=new ArrayList<Node>();
				Node val;
				Stack<Float> num= new Stack<>();
				Stack<Character> stack= new Stack<>();
				float f1=0f,d=.1f,f2=0f;
				String convert;
				
				//find numbers and characters to go inside the array
				for(int i=0;i<s.length();i++) {
					char c = s.charAt(i);
					
					//finds numbers in the string
					if(Character.isDigit(c)) {
						
						f1=Character.getNumericValue(c);
						
						//find negative zero float
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
								d*=.1f; //shifts digits
								continue;
						}
					
						//multiple digit number
						if(i!=0 && Character.isDigit(s.charAt(i-1))) { 
						
								if(f2>=10) f2= f2*10+f1; //positive multidigit number
								if(f2<0f) f2=f2*10-f1;//negative multidigit number
								f1+= (Character.getNumericValue(s.charAt(i-1))*10);
								if(f2==0f)	f2=f1;
								if(num.peek()<0 && f2<100f && f2>=10f)f2=-f2;
								num.pop();
						
						}
					
					if(f2>=10||f2<0f)num.push(f2); //push in the multidigit number
					else num.push(f1); //push in a single digit number
				
					}//digit
					
					
					//decimal
					if(c=='.')
						stack.push('d');
			
					//negative
					if(c=='-'&&Character.isDigit(s.charAt(i+1))) 
						stack.push('n');
					
					else if(c=='(') {
						d=.1f;
						isDecimal(stack);
						NegativeZero(stack,num);
						f2=0f;
						stack.push(c);
						//put numbers into the arraylist p
						if(!num.empty()) {
							convert=String.valueOf(num.peek());
							val=new Node(convert);
							p.add(val);
							num.pop();
						}
						
					}//(open parenthesis
					
					else if(c=='*'||c=='/') {
						d=.1f;
						isDecimal(stack);
						NegativeZero(stack,num);
						f2=0f;
						
						//put numbers into the arraylist p
						if(!num.empty()) {
							convert=String.valueOf(num.peek());
							val=new Node(convert);		
							p.add(val);
							num.pop();
						}
						
						/**
						 * checks if sign of same precedence is on top of the stack
						 * then adds the previous operator to the arraylist p
						 * 
						 */
							if(!stack.empty()&&(stack.peek()=='*'||stack.peek()=='/')) {
										
								//put operator into arraylist p
									convert=Character.toString(stack.peek());
									val=new Node(convert);
									p.add(val);
									stack.pop();
							}
							stack.push(c);		
					}//*/
					
					else if(c=='+'||c=='-') {
						d=.1f;
						isDecimal(stack);
						NegativeZero(stack,num);
						f2=0f;
						//put numbers into the arraylist p
						if(!num.empty()) {
							convert=String.valueOf(num.peek());
							val=new Node(convert);
							p.add(val);
							num.pop();
						}
						/**
						 * has lowest precedence so +- only get pushed on either an
						 * empty stack or parenthesis
						 */
							if(stack.empty()||stack.peek()=='(') 
									stack.push(c);
							
							else {
								
								//put operator into arraylist p
								convert=Character.toString(stack.peek());
								val=new Node(convert);
								p.add(val);
								stack.pop();
								stack.push(c);
							}
							
					}//+-
					
					else if(c==')') {
						d=.1f;
						isDecimal(stack);
						NegativeZero(stack,num);
						f2=0f;
						//put numbers into the arraylist p
						if(!num.empty()) {
							convert=String.valueOf(num.peek());
							val=new Node(convert);
							p.add(val);
							num.pop();
						}
						
						while(stack.peek()!='(') {
							//put operator into arraylist p
							convert=Character.toString(stack.peek());
							val=new Node(convert);
							p.add(val);
							stack.pop();
							
						}	
					
						if(stack.peek()=='(')
							stack.pop();
						
					}//) closed parenthesis
					
					
				}//for
				
				isDecimal(stack);
				NegativeZero(stack,num);
				/**
				 * puts final values into the array
				 */
				if(!num.empty()) {
					//put numbers into the arraylist p
						convert=String.valueOf(num.peek());
						val=new Node(convert);
						p.add(val);
						num.pop();
				}
				
				while(!stack.empty()) {
					//put operator into arraylist p
					convert=Character.toString(stack.peek());
					val=new Node(convert);
					p.add(val);
					stack.pop();
					
				}
				
				return p;
			}//postorder
			
			public float solution(ArrayList<Node> p) {
				Stack<Node> total = new Stack<>();
				Node number;
				float val1,val2,t;
				
				for(Node n: p) {
					
					if(n.data.equals("+")||n.data.equals("-")||
							n.data.equals("/")||n.data.equals("*")) {
						
						//take two children and convert to floats
						val1=Float.valueOf(total.pop().data);
						val2=Float.valueOf(total.pop().data);
						
						
						//take the values and operate on them 
						if(n.data.equals("+")) t=val2+val1;
						else if(n.data.equals("-")) t=val2-val1;
						else if(n.data.equals("/")) t=val2/val1;
						else t=val2*val1;
						
						//create a new node with the solution of the subtree
						//then push the solution into the stack as a new node
						number= new Node(String.valueOf(t));
						total.push(number);
						
						
						
					}
					else {//push numerical values into the stack
						total.push(n);
					}
					
					
				}//for
				
				t=Float.valueOf(total.peek().data);
				return t;
			}
			
			public Node tree(ArrayList<Node> p) {
				Stack<Node> stack= new Stack<>();
				Node parent,L,R;
				
				for(Node n: p) {
					
					//the parent of the tree is an operator
					if(n.data.equals("+")||n.data.equals("-")||
							n.data.equals("/")||n.data.equals("*")) {
						parent=n;
						
						//(leaves) children
						R=stack.pop();
						L=stack.pop();
						
						//link the children to the parent
						parent.left=L;
						parent.right=R;
						
						//take the subtree and put it into the stack
						stack.push(parent);
						
					}
					else {//push numerical values into the stack
						parent=n;
						stack.push(n);
						
					}
					
					
				}//for
				
				parent=stack.peek();
				return parent;
				
			}
			
			
			String pre="";//store preorder string 
			public void preorder(Node r) {
				if (r==null)
					return; 
				pre=pre.concat(r.data+' ');
				preorder(r.left);
				preorder(r.right);
					
			}
			
			
		}//expression tree
		

	}//node
public static void main(String args[]) {
	
	Samantha_Steele_23042789_Project2 s= new Samantha_Steele_23042789_Project2();
	double round;
	PrintWriter f=null,t= null;

	
	try {
		f=new PrintWriter(new File("project2_output.txt"));
		t=new PrintWriter(new File("project2_debug.txt"));
		BufferedReader file = new BufferedReader(new FileReader("Expressions.txt"));
		String line;					
	
	
		
				//calculate each line
				while((line=file.readLine()) != null) {
					Samantha_Steele_23042789_Project2.Node root=s.new Node(line);
					Samantha_Steele_23042789_Project2.Node.ExpressionTree tr= root.new ExpressionTree();
					root = tr.tree(tr.postorder(line));
					tr.preorder(root);
					t.println(tr.pre);//add the preorder to the file t
					tr.pre="";
					
					//rounds the solution to two decimal places;
					round = Math.round(tr.solution(tr.postorder(line)) * 100.0) / 100.0;
					f.println(line + " = "+ round);// add the output to the file f
					
					
				}
					
		file.close();
	}//try
	
	catch(Exception e) {
		System.out.print("file not found");
	}
	f.close();
	t.close();
	
}

	
}//project 2

