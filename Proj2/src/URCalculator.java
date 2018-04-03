import java.util.*;


public class URCalculator {
    private static Scanner scanner = new Scanner( System.in );
    private static Map<String, Double> symbolTable = new HashMap<>();
    private static Map<String, String> functionTable = new HashMap<>();
    static
    {
        functionTable.put("_sin[]","sin[x]");functionTable.put("_cos[]","cos[x]");functionTable.put("_tan[]","tan[x]");
    } // Note, Trig functions are a special case and we don't actually utilize the value of each map key
    private static final Map<String, Integer> orderOfOps = new HashMap<>();
    static
    {
        orderOfOps.put(")", 4); orderOfOps.put("^", 3); orderOfOps.put("*", 2); orderOfOps.put("/", 2);
        orderOfOps.put("plus", 1); orderOfOps.put("-", 1); orderOfOps.put("(", 0); orderOfOps.put(" ", -1);
        orderOfOps.put("=", -1);orderOfOps.put("+",1);orderOfOps.put("minus",1);
    } // Note This Map some symbols with value -1, these are not used for orderOfOps, but are also operators

    private static ArrayList<String> infixToPostFix(ArrayList<String> inputArray){ // Takes in standard infix expression
        ArrayList<String> result = new ArrayList<>();
        Stack<String> operatorStack = new Stack<>();
        for(String element : inputArray){
            if(orderOfOps.containsKey(element)){ // Element is an Operator
                int operatorPriority = orderOfOps.get(element);
                if(operatorStack.isEmpty()){ // If the operator stack is empty add the operator
                    operatorStack.push(element);
                }
                else{
                    switch (element) {
                        case ")":
                            while (!operatorStack.isEmpty()) {
                                if (operatorStack.peek().equals("(")) {
                                    operatorStack.pop();
                                    break;
                                } else {
                                    result.add(operatorStack.pop());
                                }
                            }
                            break;
                        case "(":
                            operatorStack.push(element);
                            break;
                        default:
                            while (!operatorStack.isEmpty())
                                if (operatorPriority <= orderOfOps.get(operatorStack.peek())) {
                                    result.add(operatorStack.pop());
                                } else break;
                            operatorStack.push(element);
                            break;
                    }
                }
            }

            else{ // Element is a number
                result.add(element);
            }
        }
        while(!operatorStack.isEmpty()){
            result.add(operatorStack.pop());
        }
        return result;
    }

    private static double evalPostfix(ArrayList<String> postfix){
        Stack<Double> opperands = new Stack<>();
        for (String element : postfix) {
            if (orderOfOps.containsKey(element)) { // If the element is an operator
                if (opperands.size() >= 2) {
                    switch (element) {
                        case "*":
                            double b = opperands.pop();
                            double a = opperands.pop();
                            opperands.push(a * b);
                            break;
                        case "/":
                            b = opperands.pop();
                            a = opperands.pop();
                            if(b == 0) exception("Argument divisor is 0");
                            opperands.push(a / b);
                            break;
                        case "plus":
                            b = opperands.pop();
                            a = opperands.pop();
                            opperands.push(a + b);
                            break;
                        case "minus":
                            b = opperands.pop();
                            a = opperands.pop();
                            opperands.push(a - b);
                            break;
                        case "^":
                            b = opperands.pop();
                            a = opperands.pop();
                            opperands.push(Math.pow(a, b));
                            break;
                    }
                }
            } else { // The element must be a number or a variable
                if(symbolTable.containsKey(element)){
                    opperands.push(symbolTable.get(element));
                }
                else if(symbolTable.containsKey(element.replace("-",""))){
                    opperands.push(-1.*symbolTable.get(element.replace("-","")));
                }
                else {
                    try {
                        opperands.push(Double.parseDouble(element));
                    }
                    catch(NumberFormatException e){
                        exception("Variable "+element+" does not exist");
                    }
                }
            }
        }
        if(opperands.size()>1){
            exception("Invalid Math Expression");
        }
        return opperands.pop();
    }


    private static ArrayList<String> toArrayList(String expression){ // Separates Numbers by operators or spaces
        ArrayList<String> infixExp = new ArrayList<>();
        String[] expressionArray = expression.split("");
        StringBuilder element = new StringBuilder();
        for (String s : expressionArray) {
            if (orderOfOps.containsKey(s)) { // Element is an Operator
                if (!element.toString().equals("")) {
                    infixExp.add(element.toString());
                }
                if (!s.equals(" ")) infixExp.add(s);
                element = new StringBuilder();
            } else element.append(s); // Element is part of a number
        }
        if(!element.toString().equals("")) infixExp.add(element.toString());
        Collections.reverse(infixExp);
        while(infixExp.contains("-")){
            int index = infixExp.indexOf("-");
            if(!orderOfOps.containsKey(infixExp.get(index -1))||(infixExp.get(index-1).equals("("))){ // If prev num
                if(index+1 < infixExp.size()){ // If the next element exists
                    if(orderOfOps.containsKey(infixExp.get(index+1))&&(!infixExp.get(index+1).equals(")"))){
                        infixExp.remove(index); // If an operator follows the -, slide it into the numbers dm's
                        infixExp.set(index-1, "-"+infixExp.get(index-1));
                    }
                    else{
                        infixExp.set(index,"minus");
                    }
                }
                else{
                    infixExp.remove(index);
                    infixExp.set(index-1, "-"+infixExp.get(index-1));
                }
            }
        }
        for(int i = 0; i < infixExp.size(); i++){
            String s = infixExp.get(i);
            int count = s.length() - s.replace("-", "").length(); // Counts number of -'s
            if(count%2 == 0){
                infixExp.set(i,s.replace("-", "")); // Even number of -'s -> remove all
            }
            else{
                infixExp.set(i,"-"+s.replace("-", "")); // Odd number of -'s -> leave one
            }
        }
        while(infixExp.contains("+")){
            int i = infixExp.indexOf("+");
            if(i+1 < infixExp.size()){
                if(infixExp.get(i).equals("+")&&orderOfOps.containsKey(infixExp.get(i+1))){
                    infixExp.remove(i);
                }
                else{
                    infixExp.set(i,"plus"); // Need to mark the operator once I've made sure it is legal
                }
            }
            else if(infixExp.get(i).equals("+")){
                infixExp.remove(i);
            }
        }
        Collections.reverse(infixExp);
        return infixExp;
    }

    private static double assignVariables(ArrayList<String> infixExp){
        int indexOfEquals = infixExp.indexOf("=");
        String variableName = infixExp.get(indexOfEquals - 1);
        ArrayList<String> variableValueArray = new ArrayList<>(infixExp.subList(indexOfEquals+1,infixExp.size()));
        double variableValue;
        if(variableValueArray.contains("=")){ // Recurse Down
            variableValue = assignVariables(variableValueArray);
            symbolTable.put(variableName, variableValue);
            return variableValue;
        }
        else{
            ArrayList<String> postfix = infixToPostFix(variableValueArray);
            symbolTable.put(variableName, evalPostfix(postfix));
            return evalPostfix(postfix);
        }

    }

    private static boolean balanceParentheses(String input) {
        char[] inputArray = input.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (char c : inputArray) {
            if (c == '{' || c == '[' || c == '(') {
                stack.push(c);
            }
            switch (c) {
                case '}':
                    if (stack.peek() == '{') stack.pop();
                    else stack.push(c);
                    break;
                case ')':
                    if (stack.peek() == '(') stack.pop();
                    else stack.push(c);
                    break;
                case ']':
                    if (stack.peek() == '[') stack.pop();
                    else stack.push(c);
                    break;
            }
        }
        return stack.size() == 0;
    }

    private static void evalInput(String input){
        if(input.contains("(")||input.contains(")")||input.contains("{")||input.contains("}")
                ||input.contains("[")||input.contains("]")){
            if(balanceParentheses(input)){
                input = input.replaceAll("\\{","(");
                input = input.replaceAll("\\[","(");
                input = input.replaceAll("}",")");
                input = input.replaceAll("]",")");
            }
            else{
                exception("Parentheses Format Error");
            }
        }
        if(input.contains("exit")||input.contains("Exit")){
            System.out.println("Exit");
            System.exit(0);
        }
        else if(input.equals("Clear All")||input.equals("clear all")){
            symbolTable.clear();
        }
        else if(input.contains("clear")||input.contains("clear")){
            String[] inputs = input.split("\\s+");
            if(symbolTable.containsKey(inputs[1])){
                symbolTable.remove(inputs[1]);
            }
            else{
                System.out.println(inputs[1]+" does not exist");
            }
        }
        else if(input.contains("Show all")||input.contains("show all")){
            System.out.println(symbolTable);
        }
        else if(input.equals("Show Functions")||input.equals("show functions")){
            System.out.println(functionTable);
        }
        else if(input.contains("_")){
            String noSpaceInput = input.replace(" ", "");
            String relFunctionInput = noSpaceInput.substring(noSpaceInput.indexOf("_"),noSpaceInput.indexOf("]"))+"]";
            String functionName = relFunctionInput.substring(0, relFunctionInput.indexOf("["))+"[]";
            if(noSpaceInput.contains("]=")){ //This input is defining a function
                String functionExp = noSpaceInput.substring(noSpaceInput.indexOf("=")+1);
                functionTable.put(functionName,functionExp);
            }
            else{ // The input is trying to call a function that is already defined
                String inputNumbers = input.substring(relFunctionInput.indexOf("[") + 1, relFunctionInput.indexOf("]"));
                String[] inputNumbersArray = inputNumbers.split(",");
                if(functionTable.containsKey(functionName)){ //Function does exist
                    double solution;
                    switch (functionName) {
                        case "_sin[]":
                            solution = Math.sin(Double.parseDouble(inputNumbersArray[0]));
                            break;
                        case "_cos[]":
                            solution = Math.cos(Double.parseDouble(inputNumbersArray[0]));
                            break;
                        case "_tan[]":
                            solution = Math.tan(Double.parseDouble(inputNumbersArray[0]));
                            break;
                        default:
                            String functionExp = functionTable.get(functionName);
                            ArrayList<String> functionExpList = toArrayList(functionExp);
                            for (int i = 0; i < functionExpList.size(); i++) {
                                String s = functionExpList.get(i);
                                if ((s.equals("x"))) {
                                    functionExpList.set(i, inputNumbersArray[0]);
                                } else if ((inputNumbersArray.length >= 2) && (s.equals("y"))) {
                                    functionExpList.set(i, inputNumbersArray[1]);
                                } else if ((inputNumbersArray.length >= 3) && (s.equals("z"))) {
                                    functionExpList.set(i, inputNumbersArray[2]);
                                }
                            }
                            solution = evalPostfix(infixToPostFix(functionExpList));
                            break;
                    }
                    String functionInput = relFunctionInput.substring(0,relFunctionInput.indexOf("]"))+"]";
                    input = input.replace(functionInput, Double.toString(solution));
                    solution = evalPostfix(infixToPostFix(toArrayList(input)));
                    System.out.println(solution);
                }
                else{
                    exception("The function "+functionName+" does not exist");
                }
            }
        }
        else if(input.contains("=")){
            assignVariables(toArrayList(input));
        }
        else{ // Just evaluate and print the answer to the expression
            Double result = evalPostfix(infixToPostFix(toArrayList(input)));
            System.out.println(result);
        }
    }

    private static void exception(String message){
        System.out.println(message);
        while(true){
            System.out.print(":");
            String entry = scanner.nextLine();
            evalInput(entry);
        }
    }

    public static void main(String[] args) {
        System.out.println("URCalculator");
        System.out.println("by Nicholas Romano");
        while(true){
            System.out.print(":");
            String entry = scanner.nextLine();
            evalInput(entry);
        }
    }
}
