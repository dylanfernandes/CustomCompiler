package semanticAnalyzer.visitor;

import semanticAnalyzer.SymbolTable.*;
import sun.awt.Symbol;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.*;

import java.util.List;

public class TypeCheckingVisitor extends Visitor {
    private boolean hasError;
    private String errorOutput;
    private SymbolTable globalSymbolTable;

    public TypeCheckingVisitor() {
        hasError = false;
        errorOutput = "";
        globalSymbolTable = null;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public void visit(StringASTNode astNode) {

    }

    public void visit(TokenASTNode astNode) {

    }

    public void visit(ProgASTNode astNode) {
        ASTNode classDeclRep = astNode.getFirstChild();
        ASTNode funcDefRep = classDeclRep.getRightSibling();
        VarDeclStatFuncRepASTNode varDeclStatFuncRep = (VarDeclStatFuncRepASTNode) funcDefRep.getRightSibling().getRightSibling().getFirstChild().getRightSibling();

        ClassDeclASTNode classNode;
        FuncDefASTNode functionNode;

        globalSymbolTable = astNode.getGlobalTable();

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
        List<SymbolTableEntry> symbolTableEntries;
        ASTNode child = astNode.getFirstChild();
        ASTNode temp = new StringASTNode("");
        VarOrFuncCheckASTNode varOrFuncCheck;

        if(child.getValue().equals("class")) {
            child = child.getRightSibling();
        }

        varOrFuncCheck = (VarOrFuncCheckASTNode) child.getRightSibling().getRightSibling().getRightSibling();

        //verify inheritance of class
        if(child.getRightSibling().getValue().equals("classExOpt")) {
            child = child.getRightSibling();
            if(child.getFirstChild().getValue().equals(":")) {
                verifyClass(getInheritanceEntry(child, temp).getName());
                child = temp;


                //verify several inheritance
                child = child.getRightSibling();
                while (child.getFirstChild().getValue().equals(",")) {
                    verifyClass(getInheritanceEntry(child, temp).getName());
                    child = temp;
                    child = child.getRightSibling();
                }
            }
        }

        varOrFuncCheck.accept(this);
        symbolTableEntries = varOrFuncCheck.getEntries();
        //check if declared functions are defined
        for(int i = 0; i < symbolTableEntries.size(); i++) {
            if(symbolTableEntries.get(i).getEntryKind() == EntryKind.FUNCTION) {
                verifyClassFunction(astNode.getEntry().getName(), symbolTableEntries.get(i).getName());
            }
        }
    }

    private int verifyClass(String classCheck){
        int status = globalSymbolTable.find(classCheck, EntryKind.CLASS);

        if (classCheck.equals("integer") || classCheck.equals("float")) {
            //base types
            return -2;
        }
        if (status == -1){
            hasError = true;
            errorOutput += "Class "+ classCheck + " is not defined \n";
        }
        return status;
    }

    private int verifyClassFunction(String classCheck, String functioName){
        SymbolTable classTable;
        SymbolTable funcTable;
        int status = -1;


        if (verifyClass(classCheck) != -1){
            classTable = globalSymbolTable.search(classCheck, EntryKind.CLASS).getLink();
            status = classTable.find(functioName, EntryKind.FUNCTION);
            if(status == -1) {
                hasError = true;
                errorOutput += "Class function "+ functioName + " is not declared \n";
            } else {
                funcTable = classTable.search(functioName, EntryKind.FUNCTION).getLink();
                status = funcTable.find(classCheck, EntryKind.INHERIT);
                if(status == -1){
                    hasError = true;
                    errorOutput += "Class function "+ functioName + " is not defined \n";
                }
            }
        }
        return status;
    }

    public void visit(FuncDefASTNode astNode) {
        String typeStr;

        ASTNode head = astNode.getFirstChild();
        VarDeclStatFuncRepASTNode varDeclStatFuncRepASTNode = (VarDeclStatFuncRepASTNode) head.getRightSibling().getFirstChild().getRightSibling();
        ASTNode type = head.getFirstChild();
        ASTNode id = type.getRightSibling();
        ASTNode headerChoice = type.getRightSibling().getRightSibling();
        FParamsASTNode fParams;

        typeStr = getType(type);
        verifyClass(typeStr);
        //free function definition
        if(headerChoice.getFirstChild().getValue().equals("(")) {
            fParams = (FParamsASTNode) id.getRightSibling().getFirstChild().getRightSibling();
        } else {
            //class function definition
            //id was id of class
            id = headerChoice.getFirstChild().getRightSibling();
            fParams = (FParamsASTNode) id.getRightSibling().getRightSibling();
        }


        //add function paramters to function table
        if(!fParams.getFirstChild().getValue().equals("EPSILON")) {
            fParams.accept(this);
        }

        varDeclStatFuncRepASTNode.accept(this);

    }

    public void visit(FParamsASTNode astNode) {
        String paramTypeStr;

        ASTNode type;
        ASTNode head;

        head = astNode;

        while(!head.getFirstChild().getValue().equals("EPSILON")) {

            if(head.getValue().equals("fParams")) {
                type = head.getFirstChild();
                head = type.getRightSibling().getRightSibling().getRightSibling();
            }
            else {
                type = head.getFirstChild().getFirstChild().getRightSibling();
                head = head.getFirstChild().getRightSibling();
            }

            paramTypeStr = getType(type);
            verifyClass(paramTypeStr);
        }

    }

    public void visit(VarOrFuncCheckASTNode astNode) {
        String elementTypeStr;
        EntryType elementType;
        FParamsASTNode fParamsASTNode;

        ASTNode type;
        ASTNode id;
        ASTNode array;
        ASTNode head;
        ASTNode varCheckNext;

        head = astNode;

        while(head.getValue().equals("varOrFuncCheck") && !head.getFirstChild().getValue().equals("EPSILON")) {
            type = head.getFirstChild();
            id = type.getRightSibling();
            varCheckNext = id.getRightSibling();

            elementTypeStr = getType(type);

            if (!(elementTypeStr.equals("integer") || elementTypeStr.equals("float"))) {
                verifyClass(elementTypeStr);
            }

            //element is variable
            if (varCheckNext.getFirstChild().getValue().equals("arraySizeRep")) {
                array = varCheckNext.getFirstChild();
                head = varCheckNext.getFirstChild().getRightSibling().getRightSibling();

            }
            else if(varCheckNext.getFirstChild().getValue().equals("(")) {
                //Element is a function declaration
                head = varCheckNext.getFirstChild();
                fParamsASTNode = (FParamsASTNode) head.getRightSibling();
                fParamsASTNode.accept(this);
                //point to function delaration repitition
                head = varCheckNext.getFirstChild().getRightSibling().getRightSibling().getRightSibling().getRightSibling();
            }
        }

        if(head != null && head.getValue().equals("funcDeclRep")) {
            while(head.getValue().equals("funcDeclRep") && !head.getFirstChild().getValue().equals("EPSILON")) {
                type = head.getFirstChild().getFirstChild();
                elementTypeStr = getType(type);
                if (!(elementTypeStr.equals("integer") || elementTypeStr.equals("float"))) {
                    verifyClass(elementTypeStr);
                }
                id = type.getRightSibling();
                fParamsASTNode = (FParamsASTNode) id.getRightSibling().getRightSibling();
                fParamsASTNode.accept(this);
                head = head.getFirstChild().getRightSibling();
            }
        }

    }

    public void visit(VarDeclStatFuncRepASTNode astNode) {
        ASTNode head = astNode;
        String type = "";
        ASTNode var;
        //create function table
        SymbolTable currentTable = new SymbolTable("funcTable");
        currentTable.addEntries(astNode.getEntries());

        while(head != null && head.getValue().equals("varDeclStatFuncRep")) {
            //type not ID
             if(head.getFirstChild().getValue().equals("statementNoId") && head.getFirstChild().getFirstChild().getValue().equals("for")){
                //for loop variable
                type = getType(head.getFirstChild().getFirstChild().getRightSibling().getFirstChild().getRightSibling());
                verifyClass(type);
            }
            else if(head.getFirstChild().getValue().equals("idProd") && head.getFirstChild().getFirstChild().getRightSibling().getFirstChild().getValue().equals("varDeclId")) {
                //ID type declaration
                type = head.getFirstChild().getFirstChild().getValue();
                verifyClass(type);
            } else if(head.getFirstChild().getValue().equals("idProd") && head.getFirstChild().getFirstChild().getRightSibling().getFirstChild().getValue().equals("oldVarEndNest")){
                //left child = verify integer indice
                 // right child = verify instance attributes
                 verifyIdProdNotDeclaration(head.getFirstChild(), currentTable);
             }
            head = head.getFirstChild().getRightSibling();
        }
    }

    private void verifyIdProdNotDeclaration(ASTNode idProdRoot, SymbolTable functionTable) {
        //check if id in class or inherited classes
        String variableId;
        ASTNode oldVarEndNest;
        ASTNode oldVarEndNestNext;
        ASTNode exprNode;
        EntryType varType;

        while(idProdRoot.getValue().equals("idProd")) {
            variableId = idProdRoot.getFirstChild().getValue();
            verifyVariableFunction(variableId, functionTable);

            if(functionTable.find(variableId, EntryKind.VARIABLE) != -1) {
                varType = functionTable.search(variableId, EntryKind.VARIABLE).getEntryType();
            } else
                break;

            oldVarEndNest = idProdRoot.getFirstChild().getRightSibling().getFirstChild();
            //check indices
            if(oldVarEndNest.getFirstChild().getValue().equals("indiceRep")){
                verifyIndice(oldVarEndNest.getFirstChild(), varType, variableId, functionTable);
            }
            //check dot operations
            if(oldVarEndNest.getFirstChild().getRightSibling().getValue().equals("oldVarEndNestNext")) {
                oldVarEndNestNext = oldVarEndNest.getFirstChild().getRightSibling();
                if(oldVarEndNestNext.getFirstChild() != null){
                        //type not integer or float
                    if(oldVarEndNestNext.getFirstChild().getValue().equals(".")) {
                        if (verifyClass(varType.getElementType().getType()) == -2) {
                            hasError = true;
                            errorOutput += "Undefined member for " + variableId + "\n";
                            break;
                        } else {
                            //variable is of class type
                            idProdRoot = oldVarEndNestNext.getFirstChild().getRightSibling();
                            if (globalSymbolTable.find(varType.getElementType().getType(), EntryKind.CLASS) != -1) {
                                functionTable = globalSymbolTable.search(varType.getElementType().getType(), EntryKind.CLASS).getLink();
                            } else
                                continue;
                        }
                    }
                    else if (oldVarEndNestNext.getFirstChild().getValue().equals("assignStatEnd")) {
                        exprNode = oldVarEndNestNext.getFirstChild().getFirstChild().getRightSibling();
                        //verify expression is same type as var
                        verifyExpression(exprNode, varType.getElementType(), functionTable);
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

    //verifies if variable is in current table or inherited table
    private boolean verifyVariableFunction(String varName, SymbolTable funcTable){
        boolean defined = true;
        SymbolTableEntry funcEntry;
        SymbolTable classTable;
        boolean varDef = funcTable.find(varName, EntryKind.VARIABLE) != -1;
        boolean paramDef= funcTable.find(varName, EntryKind.PARAMETER) != -1;
        boolean hasInehrit = funcTable.hasInheritance() != -1;

        // Is variable in current function table
        if(!varDef && !paramDef) {
            if (!hasInehrit) {
                defined = false;
            }
            //check inheritance tables if exist
            else if (hasInehrit) {
                for (int i = 0; i < funcTable.getNumEntries(); i++) {
                    funcEntry = funcTable.getEntryByRow(i);
                    if (funcEntry.getEntryKind() == EntryKind.INHERIT) {
                        //verify with inherited table
                        classTable = globalSymbolTable.search(funcEntry.getName(), EntryKind.CLASS).getLink();
                        if (classTable != null)
                            verifyVariableFunction(varName, classTable);
                        else
                            defined = false;
                    }
                }
            }
        }

        if (!defined){
            hasError = true;
            errorOutput += "Variable "+ varName + " is not defined in scope \n";
        }
        return defined;
    }
    //verifies if function is in current table, inherited table or global table
    private SymbolTable verifyFunctionFunction(String funcName, SymbolTable funcTable){
        boolean defined = true;
        SymbolTableEntry funcEntry;
        SymbolTable classTable;
        SymbolTable definitionTable = null;

        boolean currDef = funcTable.find(funcName, EntryKind.FUNCTION) != -1;
        boolean globalDef = globalSymbolTable.find(funcName, EntryKind.FUNCTION) != -1;
        boolean funcDef = currDef || globalDef;
        boolean hasInehrit = funcTable.hasInheritance() != -1;


        // Is function in current function table
        if(!funcDef) {
            if (!hasInehrit) {
                defined = false;
            }
            //check inheritance tables if exist
            else if (hasInehrit) {
                for (int i = 0; i < funcTable.getNumEntries(); i++) {
                    //get rows of table
                    funcEntry = funcTable.getEntryByRow(i);
                    if (funcEntry.getEntryKind() == EntryKind.INHERIT) {
                        //verify with inherited table
                        classTable = globalSymbolTable.search(funcEntry.getName(), EntryKind.CLASS).getLink();
                        if (classTable != null)
                            definitionTable = verifyFunctionFunction(funcName, classTable);
                        else
                            defined = false;
                    }
                }
            }
        } else if(funcDef){
            if(currDef) {
                definitionTable = funcTable.search(funcName, EntryKind.FUNCTION).getLink();
            }
            else
                definitionTable = globalSymbolTable.search(funcName, EntryKind.FUNCTION).getLink();
        }

        if (!defined){
            hasError = true;
            errorOutput += "Function "+ funcName + " is not defined in scope \n";
        }
        return definitionTable;
    }


    private void verifyIndice(ASTNode indiceRoot, EntryType varType, String varId, SymbolTable functionTable) {
        ASTNode head= indiceRoot;
        ASTNode exprNode;
        VariableType variableType = new VariableType("integer");

        int numIndice = 0;
        while(head.getValue().equals("indiceRep") && !head.getFirstChild().getValue().equals("EPSILON")) {
            numIndice++;
            head = head.getFirstChild();
            exprNode = head.getFirstChild().getRightSibling();
            //verify expression is int
            verifyExpression(exprNode, variableType, functionTable);
            //go to next indice rep
            head = head.getRightSibling();
        }

        if(!varType.getElementType().isArray() && numIndice > 0){
            hasError = true;
            errorOutput +=  varId + " is not of type array \n";
        }
        else if (varType.getElementType().getNumDimensions() != numIndice ) {
            hasError = true;
            errorOutput +=  "Invalid number of dimensions for " + varId + ": " + varType.getElementType().getNumDimensions() + " needed " +  numIndice + " provided\n";
        }
    }

    private void verifyExpression(ASTNode exprNode, VariableType type, SymbolTable functionTable) {
        ASTNode head;
        if (exprNode.getValue().equals("expr")) {
            head = exprNode.getFirstChild().getFirstChild();
        } else {
            //arithExpr
            head = exprNode.getFirstChild();
        }
        ASTNode factor;
        TokenASTNode baseFactor;
        String tokenType;

        while((head.getValue().equals("term") || head.getValue().equals("termPrime")) && !head.getFirstChild().getValue().equals("EPSILON")) {
            if (head.getValue().equals("term")){
                factor = head.getFirstChild();
            } else {
                factor = head.getFirstChild().getRightSibling();
            }
            if(factor.getFirstChild().getValue().equals("varOrFuncCall")){
                verifyVarOrFuncCall(factor.getFirstChild(), type, functionTable);
            }
            else if(factor.getFirstChild().getClass() == TokenASTNode.class) {
                baseFactor = (TokenASTNode) factor.getFirstChild();
                tokenType = baseFactor.getToken().getType().toString().toLowerCase();
                if (tokenType.equals( "int")) {
                    tokenType = "integer";
                }
                else if(tokenType.equals("flo")) {
                    tokenType = "float";
                }
                if (!tokenType.equals(type.getType()) && !tokenType.equals("not")){
                    hasError = true;
                    errorOutput +=  "Invalid type at line " + baseFactor.getToken().getLineNumber() + ": " + type.getType() + " needed " +  tokenType + " provided\n";

                }
            }
            //two terms can't be reached within a expression, go to termPrime
            head = head.getRightSibling();
        }
    }

    private void verifyVarOrFuncCall(ASTNode varOrFuncCall, VariableType type,SymbolTable symbolTable){
        ASTNode head = varOrFuncCall.getFirstChild();
        String elementName;
        VariableType currentType;

        //element can be variable or function
        elementName = head.getValue();
        head = head.getRightSibling().getFirstChild();

        if(head.getValue().equals("EPSILON")) {
            //prevent type error if ID not found in fuction table
            if(verifyVariableFunction(elementName, symbolTable)){
                currentType = symbolTable.search(elementName, EntryKind.VARIABLE).getEntryType().getElementType();
                if (!currentType.getType().equals(type.getType())){
                    hasError = true;
                    errorOutput +=  "Invalid type: " + type.getType() + " needed " +  currentType.getType() + " provided\n";

                }

            }
        } else if(head.getValue().equals("(")){
            //element name is function name
            verifyVarFuncParams(head.getRightSibling(), elementName, symbolTable);
        }

    }

    private void verifyVarFuncParams(ASTNode varFuncParams, String funcName, SymbolTable symbolTable) {
        SymbolTable funcTable = verifyFunctionFunction(funcName, symbolTable);
        int paramsFound = 0;
        int paramsExpected;
        ASTNode head= varFuncParams.getFirstChild();
        ASTNode expr;
        List<SymbolTableEntry> params;

        if(funcTable == null){
            paramsExpected = 0;
        } else{
            paramsExpected = funcTable.getNumParams();
        }
        //function call without parameters
        if(head.getFirstChild().getValue().equals("EPSILON")){
            paramLengthCheck(symbolTable.getName(), paramsFound, paramsExpected);
        } else if(paramsExpected > 0){
            params = funcTable.getParameters();
            //expr of aParams
            expr = head.getFirstChild();
            if(paramsFound < params.size())
                verifyExpression(expr, params.get(paramsFound).getEntryType().getElementType(), symbolTable);
            else{
                hasError = true;
                errorOutput +=  "Invalid number of parameters for function " + funcTable.getName() + " : " + paramsExpected + " needed\n";
            }
            paramsFound++;
            head = expr.getRightSibling();
            while(!head.getFirstChild().getValue().equals("EPSILON")){
                expr = head.getFirstChild().getFirstChild().getRightSibling();
                if(paramsFound < params.size())
                    verifyExpression(expr, params.get(paramsFound).getEntryType().getElementType(), symbolTable);
                else{
                    hasError = true;
                    errorOutput +=  "Invalid number of parameters for function " + funcTable.getName() + " : " + paramsExpected + " needed\n";
                }
                paramsFound++;
                head = head.getFirstChild().getRightSibling();
            }
            if(paramsFound < params.size()) {
                hasError = true;
                errorOutput +=  "Invalid number of parameters for function " + funcTable.getName() + " : " + paramsExpected + " needed\n";
            }
        }
        else{
            hasError = true;
            errorOutput +=  "Invalid number of parameters for function " + funcTable.getName() + " : " + paramsExpected + " needed\n";
        }
    }

    private void paramLengthCheck(String funcName, int provided, int desired){
        if(provided != desired){
            hasError = true;
            errorOutput +=  "Invalid number of parameters for function " + funcName + " : " + desired + " needed " +  provided + " provided\n";
        }
    }

    private void verifyVarStart(ASTNode varStart, VariableType type,SymbolTable symbolTable){

    }
}


