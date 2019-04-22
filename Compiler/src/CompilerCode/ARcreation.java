package CompilerCode;


public class ARcreation {
	public void createAR(IR ir) {
		boolean skip = false;
		int line = 0;
		for(IRelement element : IR.instructions) {
			if(skip) {
				skip = false;
				continue;
			}
			switch(element.cmd) {
			case declare:
				
				if(RegStack.isSwiss()) {
					VarList.declaration(element.parameters.get(0));
				} else {
					//If there is a declare followed by the variable being set, use a push, otherwise push 0;
					VarList.declaration(element.parameters.get(0));
					if(IR.instructions.get(line + 1).cmd == IRelement.command.set && IR.instructions.get(line + 1).parameters.get(0) == element.parameters.get(0)) {
						AR.addCommand(ARelement.command.push, ARParamFromIRparam(IR.instructions.get(line + 1).parameters.get(1)));
						skip = true;
					} else {
						//push zero onto the stack to move esp and instantiate uninitialized variable
						AR.addCommand(ARelement.command.push, "0");
					}
				}
				break;
			case destroy:
				VarList.destroy(element.parameters.get(0));
				break;
			case set:
				if(isHighIntVar(element.parameters.get(0))) {
					AR.addCommand(ARelement.command.push, ARParamFromIRparam(element.parameters.get(1)));
				}
				
				//case where no high int vars are used
				AR.addCommand(ARelement.command.mov, ARParamsFromIRelem(element));
				
				break;
			case add:
			case bor:
			case bxor:
			case band:
			case sub:
			case mul:
			case div:
			case mod:
			case eq:
			case neq:
			case or:
			case and:
				operation(element);
				break;
			case not:
				AR.addCommand(ARelement.command.not, ARParamFromIRparam(element.parameters.get(0)));
				break;
			case jmp: //Jacob
				AR.addCommand(ARelement.command.jmp, element.parameters.get(0));
				break;
			case jmpcnd: //Jacob
				AR.addCommand(ARelement.command.cmp, new String[] {"$0", RegStack.intVarToReg("%1")});
				AR.addCommand(ARelement.command.jne, new String[] {element.parameters.get(0)});
				break;
			case function:
				AR.addCommand(ARelement.command.label, new String[] {"fun_" + element.parameters.get(0)});
				//Account for passed in parameters parameters
				break;
			case call: //Isaiah
				break;
			case ret: //Isaiah
				break;
			case label:
				AR.addCommand(ARelement.command.label, new String[] {element.parameters.get(0)});
				break;
			case endfunction:
				//Does nothing.
				break;
			default:
				//break_, goto_, gotoLabel would all end up here. These commands should be gone because of IRtransformation.
				ErrorHandler.addError("Unaccounted for IRelement.command in IR line " + line);
			}
			line++;
		}
	}
	
	private static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		}
		catch (NumberFormatException ex) {
			return false;
		}
	}
	
	private static String ARParamFromIRparam(String param) {
		if(isInt(param)) {
			return "$" + param;
		} else if(param.charAt(0) == '%') {
			return RegStack.intVarToReg(param);
		} else {
			return RegStack.varToStack(param);
		}
	}
	
	//Reverses the order of parameters and replaces IR parameters with AR equivalent
	private static String[] ARParamsFromIRelem(IRelement element) {
		String[] newparams = new String[element.parameters.size()];
		int i = newparams.length - 1;
		for(String param : element.parameters) {
			if (isInt(param)) {
				newparams[i] = "$" + param;
			}
			else if(param.charAt(0) == '%') {
				newparams[i] = RegStack.intVarToReg(param);
			} else {
				newparams[i] = RegStack.varToStack(param);
			}
			i--;
		}
		return newparams;
	}
	
	private static boolean isHighIntVar(String var) {
		if(var.charAt(0) == '%' && Integer.parseInt(var.substring(1)) > 12) {
			return true;
		}
		return false;
	}
	
	private static void operation(IRelement ir) {
		String params[];
		if(isHighIntVar(ir.parameters.get(0))) {
			//Both parameters are on the stack
			AR.addCommand(ARelement.command.pop, "%r15d");
			AR.addCommand(ARelement.command.pop, "%r14d");
			params = new String [] {"%r15d", "%r14d"};
		} else if(isHighIntVar(ir.parameters.get(1))) {
			//Only the second parameter is on the stack
			AR.addCommand(ARelement.command.pop, "%r14d");
			params = new String [] {"%r14d", "%r13d"};
		} else {
			params = ARParamsFromIRelem(ir);
			AR.addCommand(ARelement.command.add, ARParamsFromIRelem(ir));
		}
		
		
		switch(ir.cmd) {
		case add:
			AR.addCommand(ARelement.command.add, params);
			break;
		case sub:
			AR.addCommand(ARelement.command.sub, params);
			break;
		case bor:
			AR.addCommand(ARelement.command.or, params);
			break;
		case bxor:
			AR.addCommand(ARelement.command.xor, params);
			break;
		case band:
			AR.addCommand(ARelement.command.and, params);
			break;
		case mul: //Chris
			break;
		case div: //Ben
			break;
		case mod: //Chris
			break;
		case eq: //Ben
			break;
		case neq: //Chris
			break;
		case or: //Chris
			break;
		case and: //Ben
			break;
		}
		
		if(isHighIntVar(ir.parameters.get(0))) {
			AR.addCommand(ARelement.command.push, "%r14d");
		}
	}
}
