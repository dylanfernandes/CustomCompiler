package codeGeneration;

import semanticAnalyzer.SymbolTable.EntryKind;
import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.SymbolTable.SymbolTableEntry;
import semanticAnalyzer.SymbolTable.VariableType;
import semanticAnalyzer.visitor.Visitor;
import syntacticAnalyzer.AST.ASTNode;
import syntacticAnalyzer.AST.StringASTNode;
import syntacticAnalyzer.AST.TokenASTNode;
import syntacticAnalyzer.AST.semanticNodes.*;

import java.util.List;

public class CodeGenerationVisitor extends Visitor {
    private String moonCode;
    private int intSize = 4;
    private int floatSize = 8;
    private SymbolTable generationTable;

    public CodeGenerationVisitor() {
        moonCode = "";
    }

    public String getMoonCode() {
        return moonCode;
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
            } else if(head.getFirstChild().getValue().equals("idProd") && head.getFirstChild().getFirstChild().getRightSibling().getFirstChild().getValue().equals("varDeclId")) {
                //ID type
                var = head.getFirstChild().getFirstChild().getRightSibling().getFirstChild().getFirstChild();
                id = var.getValue();
                variableType = currentTable.search(id, EntryKind.VARIABLE).getEntryType().getElementType();
                //get size of class from generation table
                generateAllocateCode(id, getObjectSize(variableType));
            }
            head = head.getFirstChild().getRightSibling();
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
        int allocSize = 0;
        int baseSize = generationTable.search(varType.getType(), EntryKind.CLASS).getSize();


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
        moonCode += id + " res " + allocSize + "\n";
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
            //TODO implement for objects(not defined case)
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
}
