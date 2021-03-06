package CompilerCode;

public class Token {
	protected String token;
	protected type_enum type;
	protected int lineNumber;
	enum type_enum {
		
		//basic arithmetic
		additionOperator,
		subtractionOperator,
		divisionOperator,
		multiplicationOperator,
		modulusOperator,

		//assignment arithmetic
		assignmentOperator,
		incrementOperator,
		decrementOperator,
		subtractionAssignmentOperator,
		additionAssignmentOperator,
		multiplicationAssignmentOperator,
		divisionAssignmentOperator,

		//logic operators
		notEqualOperator,
		equalOperator,
		orLogicOperator,
		andLogicOperator,
		
		//binary operations
		notOperator,
		orOperator,
		andOperator,
		xorOperator,
		
		//brackets and semicolon
		openCurlyBracket,
		closedCurlyBracket,
		openParenthesis,
		closedParenthesis,
		semicolon,
		colon,
		comma,
		
		//identifiers and keywords
		character,
		number,
		identifier,
		variable,
		
		//keywords
		k_break,
		k_char,
		k_else,
		k_goto,
		k_if,
		k_int,
		k_return,
		k_void,
		k_while,
		k_for,
		
		//Nodes of parse tree
		program,
		declarationList,
		declaration,
		variableDeclaration,
		variableTypeSpecifier,
		variableDeclarationList,
		variableDeclarationInitialize,
		variableDeclarationID,
		functionDeclaration,
		functionTypeSpecifier,
		functionDeclarationID,
		parameterList,
		parameter,
		statementList,
		statement,
		gotoStatement,
		gotoJumpPlace,
		returnStatement,
		breakStatement,
		whileStatement,
		forStatement,
		ifStatement,
		expressionStatement,
		expression,
		simpleExpression,
		andExpression,
		bitOrExpression,
		bitXorExpression,
		bitAndExpression,
		compareExpression,
		compareOperator,
		sumExpression,
		sumOperator,
		term,
		productOperator,
		notExpression,
		factor,
		call,
		argList,
		constant
	};
	
	
	Token(type_enum type) {
		this.type = type;
	}
	
	Token(String token){
		this.token = token;
		this.addTokenIdentity();
	}
	
	Token(String token, int lineNumber){
		this.token = token;
		this.lineNumber = lineNumber;
		this.addTokenIdentity();
	}


	private void addTokenIdentity() {
		this.type = null;
		char t = this.token.charAt(0);
		switch(t) {
		case ';':
			this.type = type_enum.semicolon;
			break;
		case ':':
			this.type = type_enum.colon;
			break;
		case ',':
			this.type = type_enum.comma;
			break;
		case '(':
			this.type = type_enum.openParenthesis;
			break;
		case ')':
			this.type = type_enum.closedParenthesis;
			break;
		case '{':
			this.type = type_enum.openCurlyBracket;
			break;
		case '}':
			this.type = type_enum.closedCurlyBracket;
			break;
		case '+':
			if(this.token.equals("+=")) {
				this.type = type_enum.additionAssignmentOperator;
			} else if(this.token.equals("++")) {
				this.type = type_enum.incrementOperator;
			} else {
				this.type = type_enum.additionOperator;
			}
			break;
		case '-':
			if(this.token.equals("-=")) {
				this.type = type_enum.subtractionAssignmentOperator;
			} else if(this.token.equals("--")) {
				this.type = type_enum.decrementOperator;
			} else {
				this.type = type_enum.subtractionOperator;
			}
			break;
		case '*':
			if(this.token.equals("*=")) {
				this.type = type_enum.multiplicationAssignmentOperator;
			} else {
				this.type = type_enum.multiplicationOperator;
			}
			break;
		case '/':
			if(this.token.equals("/=")) {
				this.type = type_enum.divisionAssignmentOperator;
			} else {
				this.type = type_enum.divisionOperator;
			}
			break;
		case '%':
			this.type = type_enum.modulusOperator;
			break;
		case '=':
			if(this.token.equals("==")) {
				this.type = type_enum.equalOperator;
			} else {
				this.type = type_enum.assignmentOperator;
			}
			break;
		case '!':
			if(this.token.equals("!=")) {
				this.type = type_enum.notEqualOperator;
			} else {
				this.type = type_enum.notOperator;
			}
			break;
		case '^':
			this.type = type_enum.xorOperator;
			break;
		case '|':
			if(this.token.equals("||")) {
				this.type = type_enum.orLogicOperator;
			} else {
				this.type = type_enum.orOperator;
			}
			break;
		case '&':
			if(this.token.equals("&&")) {
				this.type = type_enum.andLogicOperator;
			} else {
				this.type = type_enum.andOperator;
			}
			break;
		}
		if(this.type != null) {
			return;
		}
		//String keywords[] = {/*"auto",*/ "break", /*"case",*/ "char", /*"const", "continue",*/
		//		/*"default", "do", "double",*/ "else", /*"enum", "extern", "float", "for",*/
		//		"goto", "if", "int", /*"long", "register", */"return", /*"short", "signed",*/
		//		/*"sizeof", "static", "struct", "switch", "typedef", "union", "unsigned",*/
		//		"void", /*"volatile",*/ "while"};
		switch(this.token) {
		case "break":
			this.type = type_enum.k_break;
			break;
		case "char":
			this.type = type_enum.k_char;
			break;
		case "goto":
			this.type = type_enum.k_goto;
			break;
		case "if":
			this.type = type_enum.k_if;
			break;
		case "else":
			this.type = type_enum.k_else;
			break;
		case "int":
			this.type = type_enum.k_int;
			break;
		case "return":
			this.type = type_enum.k_return;
			break;
		case "void":
			this.type = type_enum.k_void;
			break;
		case "while":
			this.type = type_enum.k_while;
			break;
		case "for":
			this.type = type_enum.k_for;
		}
		if(this.type != null) {
			return;
		}
		//could be written better, temporary fix
		if(this.token.charAt(0) >= '0' && this.token.charAt(0) <= '9') {
			this.type = type_enum.number;
			if(token.length() >= 3) {
				if(this.token.subSequence(0, 1).equals("0x")) {
					this.token = String.valueOf(Integer.parseInt(this.token, 16));
				}
			}
			return;
		}
		if(this.token.length() == 3 && this.token.charAt(0) == '\'' && this.token.charAt(2) == '\'') {
			this.type = type_enum.character;
			return;
		}
		this.type = type_enum.identifier;
	}
	
}
