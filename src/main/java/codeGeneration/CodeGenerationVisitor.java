package codeGeneration;

import org.apache.commons.lang3.StringUtils;
import semanticAnalyzer.SymbolTable.*;
import semanticAnalyzer.visitor.Visitor;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.*;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerationVisitor extends Visitor {
    private String moonInit;
    private String moonMain;
    private int intSize = 4;
    private int floatSize = 8;
    private SymbolTable generationTable;
    private String ZERO = "r0";
    private String TEMP_LOAD0 = "r1";
    private String TEMP_LOAD1 = "r2";
    private String TEMP_CALC = "r3";
    private String EXPR_TAG = "expr";
    private String EXPR_BRANCH = "expr_branch";
    private int exprNum = 0;
    private int exprbranchNum = 0;

    public CodeGenerationVisitor() {
        moonInit = "";
        moonMain = "";
    }

    public String getMoonInitCode() {
        return moonInit;
    }
    public String getMoonMain(){
        return moonMain;
    }

    public String getMoonCode(){
        return  moonInit + "entry\n" + moonMain + "hlt";
    }

    public void visit(StringASTNode astNode) {

    }

    public void visit(TokenASTNode astNode) {

    }

    public void visit(ProgASTNode astNode) {
        generationTable = astNode.getGlobalTable();
        decorateClasses(astNode);

        ASTNode classDeclRep = astNode.getFirstChild();
        ASTNode funcDefRep = classDeclRep.getRightSibling();
        //main method
        VarDeclStatFuncRepASTNode varDeclStatFuncRep = (VarDeclStatFuncRepASTNode) funcDefRep.getRightSibling().getRightSibling().getFirstChild().getRightSibling();

        ClassDeclASTNode classNode;
        FuncDefASTNode functionNode;

        while(classDeclRep.getValue().equals("classDeclRep") ) {
            classDeclRep = classDeclRep.getFirstChild();
            if(classDeclRep.getClass() == ClassDeclASTNode.class) {
                classNode = (ClassDeclASTNode) classDeclRep;
                classNode.accept(this);
                classDeclRep = classNode.getRightSibling();
            }
        }

        while(funcDefRep.getValue().equals("funcDefRep") ) {
            funcDefRep = funcDefRep.getFirstChild();
            if(funcDefRep.getClass() == FuncDefASTNode.class) {
                functionNode = (FuncDefASTNode) funcDefRep;
                functionNode.accept(this);
                funcDefRep = functionNode.getRightSibling();
            }
        }

        varDeclStatFuncRep.accept(this);

    }

    public void visit(ClassDeclASTNode astNode) {

    }

    public void visit(FuncDefASTNode astNode) {

    }

    public void visit(FParamsASTNode astNode) {

    }

    public void visit(VarOrFuncCheckASTNode astNode) {

    }

    public void visit(VarDeclStatFuncRepASTNode astNode) {
        ASTNode head = astNode;
        ASTNode var;
        String id = "";
        VariableType variableType;

        SymbolTable currentTable = new SymbolTable("funcTable");
        currentTable.addEntries(astNode.getEntries());

        while(head != null && head.getValue().equals("varDeclStatFuncRep")) {
            //type not ID
            if (head.getFirstChild().getValue().equals("varDeclNotId") && head.getFirstChild().getFirstChild().getValue().equals("typeNotId")) {
                var = head.getFirstChild().getFirstChild();
                id = var.getRightSibling().getValue();
                variableType = currentTable.search(id, EntryKind.VARIABLE).getEntryType().getElementType();
                //production guaranteed to produce int or float
                generateAllocateCode(id, getBasicSize(variableType));
            }else if(head.getFirstChild().getValue().equals("statementNoId") && head.getFirstChild().getFirstChild().getValue().equals("write")){
                id = head.getFirstChild().getFirstChild().getRightSibling().getRightSibling().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getValue();
                printInt(id);

            } else if(head.getFirstChild().getValue().equals("statementNoId") && head.getFirstChild().getFirstChild().getValue().equals("if")){
                generateIf(head.getFirstChild().getFirstChild());
            }
            else if(head.getFirstChild().getValue().equals("idProd") && head.getFirstChild().getFirstChild().getRightSibling().getFirstChild().getValue().equals("varDeclId")) {
                //ID type
                var = head.getFirstChild().getFirstChild().getRightSibling().getFirstChild().getFirstChild();
                id = var.getValue();
                variableType = currentTable.search(id, EntryKind.VARIABLE).getEntryType().getElementType();
                //get size of class from generation table
                generateAllocateCode(id, getObjectSize(variableType));
            }
            else if(head.getFirstChild().getValue().equals("idProd") && head.getFirstChild().getFirstChild().getRightSibling().getFirstChild().getValue().equals("oldVarEndNest")){
                //left child = verify integer indice
                // right child = verify instance attributes
                verifyIdProdNotDeclaration(head.getFirstChild());
            }
            head = head.getFirstChild().getRightSibling();
        }
    }

    private void verifyStatement(ASTNode statement){
        ASTNode head =  statement;
        if(!head.getValue().equals("EPSILON")) {
            head = statement.getFirstChild();
            String id;

            while (head != null && !head.getValue().equals("EPSILON")) {
                if (head.getValue().equals("statementNoId") && head.getFirstChild().getValue().equals("write")) {
                    id = head.getFirstChild().getRightSibling().getRightSibling().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getValue();
                    printInt(id);

                } else if (head.getValue().equals("statementNoId") && head.getFirstChild().getValue().equals("if")) {
                    generateIf(head.getFirstChild());
                }
                statement = statement.getRightSibling().getFirstChild();
                head = statement.getFirstChild();
            }
        }
    }

    private void verifyIdProdNotDeclaration(ASTNode idProdRoot) {
        //check if id in class or inherited classes
        String variableId;
        ASTNode oldVarEndNest;
        ASTNode oldVarEndNestNext;
        ASTNode exprNode;
        String value;

        while(idProdRoot.getValue().equals("idProd")) {
            variableId = idProdRoot.getFirstChild().getValue();
            //verifyVariableFunction(variableId, functionTable);


            oldVarEndNest = idProdRoot.getFirstChild().getRightSibling().getFirstChild();
            //check indices
//            if(oldVarEndNest.getFirstChild().getValue().equals("indiceRep")){
//                verifyIndice(oldVarEndNest.getFirstChild(), varType, variableId, functionTable);
//            }
            //check dot operations
            if(oldVarEndNest.getFirstChild().getRightSibling().getValue().equals("oldVarEndNestNext")) {
                oldVarEndNestNext = oldVarEndNest.getFirstChild().getRightSibling();
                if(oldVarEndNestNext.getFirstChild() != null){
                    //type not integer or float
//                    if(oldVarEndNestNext.getFirstChild().getValue().equals(".")) {
//                        if (verifyClass(varType.getElementType().getType()) == -2) {
//                            hasError = true;
//                            errorOutput += "Undefined member for " + variableId + "\n";
//                            break;
//                        } else {
//                            //variable is of class type
//                            idProdRoot = oldVarEndNestNext.getFirstChild().getRightSibling();
//                            if (globalSymbolTable.find(varType.getElementType().getType(), EntryKind.CLASS) != -1) {
//                                functionTable = globalSymbolTable.search(varType.getElementType().getType(), EntryKind.CLASS).getLink();
//                            } else
//                                continue;
//                        }
//                    }
//                    else
                    if (oldVarEndNestNext.getFirstChild().getValue().equals("assignStatEnd")) {
                        exprNode = oldVarEndNestNext.getFirstChild().getFirstChild().getRightSibling();
                        //factor value
                        generateExpression(exprNode, variableId);
                        break;
                    }
                    //get table of class
                }
                else
                    break;
            }
            else
                break;
        }
    }

    private void generateExpression(ASTNode exprNode, String variableId){
        String value;
        String operator;
        String operand;
        ASTNode term = exprNode.getFirstChild().getFirstChild();
        ASTNode termPrime = term.getFirstChild().getRightSibling();
        ASTNode factor = term.getFirstChild();
        TokenASTNode operatorToken;
        List<String> operators = new ArrayList<String>();
        List<String> operands = new ArrayList<String>();
        List<String> factors;

        //can set value directly if numeric
        if(termPrime.getFirstChild().getValue().equals("EPSILON"))
            setVariableSimple(variableId, factor);
        else {
            factors = extractFromFactor(factor);
            if(factors.size() == 2){
                operands.add(factors.get(1));
                operators.add(factors.get(0));
            }
            else
                operands.add(factors.get(0));
            while (!termPrime.getFirstChild().getValue().equals("EPSILON")){
                if(termPrime.getFirstChild().getValue().equals("multOp")) {
                    factor = termPrime.getFirstChild().getRightSibling();
                    factors = extractFromFactor(factor);
                    if(factors.size() == 2){
                        operands.add(factors.get(1));
                        operators.add(factors.get(0));
                    }
                    else {
                        operands.add(factors.get(0));
                        operatorToken = (TokenASTNode) termPrime.getFirstChild().getFirstChild();
                        operators.add(operatorToken.getValue());
                    }

                    termPrime = termPrime.getFirstChild().getRightSibling().getRightSibling();
                }
            }
            evaluateExpression(variableId, operands, operators);
        }
    }


    private int getBasicSize(VariableType variableType) {
        int allocSize = 0;
        int baseSize = 0;
        String type = variableType.getType();
        if(type.equals("integer")) {
            baseSize = intSize;
        } else if(type.equals("float")){
            baseSize = floatSize;
        }

        return getArrayAllocation(baseSize, variableType);
    }

    private int getObjectSize(VariableType varType){
        SymbolTableEntry objectEntry = generationTable.search(varType.getType(), EntryKind.CLASS);
        int baseSize = objectEntry.getSize();
        if(baseSize == -1) {
            baseSize = decorateAClass(objectEntry.getLink());
            objectEntry.setSize(baseSize);
        }

        return getArrayAllocation(baseSize, varType);
    }

    //sets allocation according to if type array(regular allocation if not an array)
    private int getArrayAllocation(int baseSize, VariableType variableType){
        int allocSize = 0;
        int prod = 1;
        if(variableType.isArray()) {
            //get number of indices in array
            for (int i = 0; i < variableType.getNumDimensions(); i++) {
                prod *=  Integer.parseInt(variableType.getSingleDimension(i));
            }
            allocSize = prod * baseSize;
        }
        else
            allocSize += baseSize;
        return allocSize;
    }

    private void generateAllocateCode(String id, int allocSize){
        moonInit += id + " res " + allocSize + "\n";
    }

    //sets total size of class + calls decoration for individual class
    private void decorateClasses(ProgASTNode progASTNode){
        List<SymbolTableEntry> classes = progASTNode.getGlobalTable().getClasses();
        int classSize;
        SymbolTableEntry classEntry;
        for(int i = 0;i < classes.size();i++){
            classEntry = classes.get(i);
            //decorate if not set
            if(classEntry.getSize() == -1) {
                classSize = decorateAClass(classEntry.getLink());
                classEntry.setSize(classSize);
            }
        }

    }

    //Adds offset + size to each class variable + returns size of class
    private int decorateAClass(SymbolTable symbolTable){
        int classSize = 0;
        List<SymbolTableEntry> classVariables = symbolTable.getVariables();
        SymbolTableEntry variableEntry;
        VariableType variableType;
        int currVarSize = 0;

        //add inheritance size
        classSize += getInheritanceSize(symbolTable.getInheritances());

        for(int i = 0;i < classVariables.size();i++){
            variableEntry = classVariables.get(i);
            variableType = variableEntry.getEntryType().getElementType();
            if(isBasicType(variableType)){
                currVarSize = getBasicSize(variableType);
            }
            else{
                currVarSize = getObjectSize(variableType);
            }
            variableEntry.setSize(currVarSize);
            //offset for current variable is size of class at this point
            variableEntry.setOffset(classSize);
            classSize += currVarSize;
        }

        return classSize;
    }

    private int getInheritanceSize(List<SymbolTableEntry> inheritedVariables){
        int allocSize = 0;
        int classSize;
        SymbolTableEntry inheritedEntry;
        SymbolTableEntry classEnrry;

        //TODO add inherited class vars + offsets to class
        for(int i = 0; i < inheritedVariables.size();i++){
            if(inheritedVariables.size() != -1){
                inheritedEntry = inheritedVariables.get(i);
                classEnrry = generationTable.search(inheritedEntry.getName(), EntryKind.CLASS);
                if(classEnrry.getSize() == -1) {
                    classSize = decorateAClass(classEnrry.getLink());
                    classEnrry.setSize(classSize);
                }
                allocSize += classEnrry.getSize();
            }
        }
        return allocSize;
    }

    private boolean isBasicType(VariableType variableType){
        String type= variableType.getType();
        return type.equals("integer") || type.equals("float");
    }

    private void loadWord(String r1, String r2, String tag){
        moonMain += "lw " + r1 + ", " + tag + "(" + r2 + ") \n";
    }
    private void printInt(String tag){
        loadWord("r1", ZERO, tag);
        moonMain += "jl r15, putint\n";
    }
    private void setVariableSimple(String var, ASTNode factor){
        String operand;
        List<String> operands = extractFromFactor(factor);
        if(operands.size() == 1) {
            operand = operands.get(0);
            if (StringUtils.isNumeric(operand)) {
                moonMain += "addi " + TEMP_CALC + ", " + ZERO + ", " + operand + "\n";
                moonMain += "sw " + var + "(" + ZERO + "), " + TEMP_CALC + "\n";
            }
             else {
                loadWord(TEMP_LOAD0, ZERO, operand);
                moonMain += "add " + TEMP_CALC + ", " + ZERO + ", " + TEMP_LOAD0 + "\n";
                moonMain += "sw " + var + "(" + ZERO + "), " + TEMP_CALC + "\n";
            }
        }
        else if(operands.size() == 2){
            evaluateNot(operands.get(1), var);
        }
    }
    private void evaluateMultOp(String var, String op1, String op2, String operand){
        String operation;
        if(operand.equals("*"))
            operation = "mul";
        else
            operation = "div";

        if(StringUtils.isNumeric(op1)){
            moonMain += "addi " + TEMP_LOAD0 + ", " + ZERO + ", " + op1 + "\n";
        }else
            moonMain += "lw " + TEMP_LOAD0 + ", " + op1 + "("+ ZERO + ")\n";

        if(StringUtils.isNumeric(op2)){
            moonMain += "addi " + TEMP_LOAD1 + ", " + ZERO + ", " + op2 + "\n";
        }else
            moonMain += "lw " + TEMP_LOAD1 + ", " + op2 + "("+ ZERO + ")\n";

        moonMain += operation +" " + TEMP_CALC + ", " + TEMP_LOAD0 + ", " + TEMP_LOAD1 + "\n";
        moonMain += "sw " + var + "(" + ZERO + "), " + TEMP_CALC + "\n";
    }

    private String generateBranchTag(){
        String currExprBranchTag = EXPR_BRANCH + exprbranchNum;
        exprNum++;
        return currExprBranchTag;
    }

    //uses r1 for operand and r3 for result
    private String evaluateNot(String operandId, String exprTag){
        String currExprBranchTag = generateBranchTag();
        String endCurrExprBranchTag = currExprBranchTag + "_end";

        if(StringUtils.isNumeric(operandId))
            moonMain += "addi " + TEMP_LOAD0 + ", " + ZERO + ", " + operandId + "\n";
       else
           moonMain += "lw " + TEMP_LOAD0 + ", " + operandId + "("+ ZERO + ")\n";

        moonMain += "not " + TEMP_CALC + ", " + TEMP_LOAD0 + "\n";
        moonMain += "bz " + TEMP_CALC + ", " + currExprBranchTag + "\n";
        moonMain += "addi " + TEMP_CALC + ", " + ZERO + ", 1\n";
        moonMain += "sw " + exprTag + "(" + ZERO + "), " + TEMP_CALC + "\n";
        moonMain += "j " + endCurrExprBranchTag + "\n";
        moonMain += currExprBranchTag + " sw " + exprTag + " (" + ZERO + "), " + ZERO + "\n";
        moonMain += endCurrExprBranchTag + "\n";

        return currExprBranchTag;
    }

    private String allocateExpression(){
        String currExprTag = EXPR_TAG + exprNum;
        exprNum++;
        moonInit += currExprTag + " res 4\n";
        return currExprTag;
    }

    private String allocateIf(){
        String currExprTag = "IF" + exprNum;
        exprNum++;
        moonInit += currExprTag + " res 4\n";
        return currExprTag;
    }



    private void evaluateExpression(String var, List<String> operands, List<String> operators) {
        String currExprTag = allocateExpression();
        int currentOperand = 0;
        for(int i =0;i < operators.size();i++){
            if(currentOperand < operands.size()) {
                if (operators.get(i).equals("!")) {
                    evaluateNot(operands.get(currentOperand), currExprTag);
                } else if (operators.get(i).equals("*") || operators.get(i).equals("/")) {
                    if(currentOperand == 0)
                        evaluateMultOp(currExprTag, operands.get(currentOperand), operands.get(currentOperand+1), operators.get(i));
                    else
                        evaluateMultOp(currExprTag, currExprTag, operands.get(currentOperand+1), operators.get(i));
                    currentOperand++;
                }
            }
            else
                break;
        }
        moonMain += "lw " + TEMP_CALC + ", " + currExprTag + "("+ ZERO + ")\n";
        moonMain += "sw " + var + "(" + ZERO + "), " + TEMP_CALC + "\n";
    }

    //Not operand can be child of factor so list needed
    private List<String> extractFromFactor(ASTNode factor){
        String child;
        List<String> children = new ArrayList<String>();
        if(factor.getFirstChild().getFirstChild() == null) {
            child = factor.getFirstChild().getValue();
            if(StringUtils.isNumeric(child)) {
                //number
                children.add(child);
            } else {
                //add not
                children.add(child);
                //add operand
                children.addAll(extractFromFactor(factor.getFirstChild().getRightSibling()));
            }
        } else {
            //variable ID
            children.add(factor.getFirstChild().getFirstChild().getValue());
        }
        return children;
    }

    private void generateIf(ASTNode ifHead){
        ASTNode expression = ifHead.getRightSibling().getFirstChild().getRightSibling();
        ASTNode ifState = expression.getRightSibling().getRightSibling().getRightSibling().getFirstChild().getRightSibling().getFirstChild();
        ASTNode elseState = expression.getRightSibling().getRightSibling().getRightSibling().getRightSibling().getRightSibling().getFirstChild().getRightSibling().getFirstChild();
        String tag = allocateIf();
        String tagElse = tag + "_else";
        String tagEnd = tag + "_end";

        generateExpression(expression,tag);
        moonMain += "lw " + TEMP_LOAD0 + ", " + tag + "("+ ZERO + ")\n";
        moonMain += "bz " + TEMP_LOAD0 + ", " + tagElse + "\n";
        //if code
        verifyStatement(ifState);
        moonMain += "j " + tagEnd + "\n";
        moonMain += tagElse + " ";
        verifyStatement(elseState);
        moonMain += tagEnd + "\n";
    }
}
