import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Stack {

    private ArrayList<Parse> parses;
    private ArrayList<ArrayList<Parse>> allScopesParses;
    private int id = 0;
    private int placeHolder = 0;
    private int labelHolder = 0;
    private int statementHolder = 0;
    private int whileHolder = 0;
    private int ifHolder = 0;
    private int caseHolder = 0;
    private boolean functionreturn = false;

    public Stack() {
        parses = new ArrayList<>();
        allScopesParses = new ArrayList<>();
        allScopesParses.add(parses);
    }

    public void nextScope(){
        parses = new ArrayList<>();
        allScopesParses.add(parses);
    }

    public void prevScope(Parse currentFunctionParse){
//        ArrayList.remove(int index);
        String setValueCode = "";
        for(int i = 0 ; i < currentFunctionParse.getParamStack().size() ; i++){
            setValueCode += "\nsetValue(scopes,\"" +currentFunctionParse.getParamStack().get(i)+"\",*top);" + "\ntop = top + 1;";
        }
        getLastParse().get(0).setCode("scopes = scopes - 1;" + setValueCode +"\n" + getAllThePossibleNDCode() + "top = top - 1;\n*top ="
                + getLastNDNotTrue(1).get(0).getPlace() + ";\nreturnAddress = *rtop;\nrtop = rtop + 1;\ngoto *returnAddress;");
//        System.out.println(allScopesParses.get(allScopesParses.size()-1).get(allScopesParses.get(allScopesParses.size()-1).size()-1));
    }

    public void resetScope(String funcName){
        Parse tmp = getLastParse().get(0);
        tmp.setCode(funcName + " :" + tmp.getCode());



        parses = allScopesParses.get(0);
    }

    public void addGotoFunction(String name,ArrayList<String> functionValues){
        Parse parse = new Parse();
        parse.setId(id++);
        parse.setPlace("T"+(placeHolder++));
        parse.setType(Parse.parse_type.nd);
        parse.setProcessed(false);
        String pushInputs = "";
//        top = top - 1;
//	*top = 40;
        for(int i = 0 ; i < functionValues.size() ; i++){
            pushInputs += "top = top - 1;\n";
            pushInputs += "*top =" + functionValues.get(i)+";\n";
        }
        parse.setCode((getAllThePossibleNDCode() + "rtop = rtop - 1;\n" + "*rtop = &&L" + (labelHolder++) + ";\n" + pushInputs + "goto " + name + ";\n L" + (labelHolder-1)
                + ": scopes = scopes + 1;\n" + "setValue(scopes,\"T"+ (placeHolder-1) + "\",*top);"+"\n"+"top = top + 1;\n"));
        getLastND(getFunctionInputSize(name));
//        T0 = *top;
//        top = top + 1;
        parses.add(parse);
//        Parse check = parses.get(parses.size() - 1);
//        if(check.getType().equals(Parse.parse_type.nd) && !check.isProcessed()) {
//            check.setCode((check.getCode() == null ? "" : (check.getCode()+"\n")) + "rtop = rtop - 1;\n" + "*rtop = &&L" + (labelHolder++) + ";\n" +  "goto " + name + ";\n");
//        }
//          nemitonam check konam ke age null nabashe dorost dar miad ya naß


//        Parse parse = new Parse();
//        parse.setId(id++);
//        parse.setPlace("T"+(placeHolder++));
//        parse.setType(Parse.parse_type.nd);
//        parse.setProcessed(false);
//        parses.add(parse);

        functionreturn = true;
    }


    public Parse addND (String place) {
        Parse parse = new Parse();
        parse.setId(id++);
        parse.setPlace(place.trim());
        parse.setType(Parse.parse_type.nd);
        parse.setProcessed(false);
        parses.add(parse);
        return parse;
    }

    public Parse addCaseValue (String place) {
        Parse parse = new Parse();
        parse.setId(id++);
        parse.setPlace(place.trim());
        parse.setType(Parse.parse_type.caseValue);
        parse.setProcessed(false);
        parses.add(parse);
        return parse;
    }

    public Parse addArithmaticStatement(String op) {
        Parse parse = new Parse();
        parse.setId(id++);
        parse.setPlace("T"+(placeHolder++));
        parse.setType(Parse.parse_type.nd);
        parse.setProcessed(false);
        ArrayList<Parse> twoLast = getLastND(2);
        Parse first = twoLast.get(1);
        Parse second = twoLast.get(0);
        parse.setCode(getAllThePossibleNDCode()+ "setValue(scopes,\""+parse.getActualPlace()+"\"," + first.getPlace() +op+ second.getPlace() +");\n");
        parses.add(parse);
        return parse;
    }

    public Parse addAssignmentStatement() {
        Parse parse = new Parse();
        parse.setId(id++);
        ArrayList<Parse> twoLast = getLastND(2);
        Parse first = twoLast.get(1);
        Parse second = twoLast.get(0);
        parse.setPlace("setValue(scopes,\"" + first.getActualPlace() + "\"," + second.getPlace() + ");");
        parse.setType(Parse.parse_type.nd);
        parse.setProcessed(false);
        if(functionreturn){
            parse.setCode(getAllThePossibleNDCode());
            functionreturn = false;
        }else{
            parse.setCode(getAllThePossibleNDCode()+parse.getPlace()+"\n");
        }
        parses.add(parse);
        return parse;
    }

    private String getAllThePossibleNDCode() {
        StringBuilder code = new StringBuilder();
        for (int i = parses.size() - 1 ; i >= 0; i--) {
            Parse check = parses.get(i);
            if(check.getType().equals(Parse.parse_type.nd) && check.getCode() != null) {
                code.append(check.getCode());
                break;
            }
            else if(check.getType().equals(Parse.parse_type.exp)
                    || check.getType().equals(Parse.parse_type.block)
                    || check.getType().equals(Parse.parse_type.caseElement)
                    || check.getType().equals(Parse.parse_type.caseValue))
                break;
        }
        return code.toString();
    }

    public Parse addRelOpExp (String op) {
        Parse parse = new Parse();
        parse.setId(id++);
        ArrayList<Parse> twoLast = getLastND(2);
        Parse first = twoLast.get(1);
        Parse second = twoLast.get(0);
        parse.setPlace(first.getPlace()+op+second.getPlace());
        parse.setType(Parse.parse_type.nd);
        parse.setProcessed(false);
        parse.setTLabel("L"+(labelHolder++));
        parse.setFLabel("L"+(labelHolder++));
        parse.setCode(getAllThePossibleNDCode()+"if("+parse.getPlace()+") goto "+parse.getTLabel()+";\ngoto "+parse.getFLabel()+";");
        parses.add(parse);
        return parse;
    }

    public Parse makeTheLastNDExp() {
        for (int i = parses.size() - 1 ; i >= 0; i--) {
            Parse check = parses.get(i);
            if(check.getType().equals(Parse.parse_type.nd) && !check.isProcessed()) {
                check.setType(Parse.parse_type.exp);
                return check;
            }
        }
        return null;
    }

    public Parse addStatement() {
        Parse parse = new Parse();
        parse.setId(id++);
        parse.setPlace("s"+(statementHolder++));
        parse.setType(Parse.parse_type.block);
        parse.setProcessed(false);
        ArrayList<Parse> lastND = getLastND(1);
        Parse last = lastND.get(0);
        if(last.getNextLabel() != null)
            parse.setNextLabel(last.getNextLabel());
        parse.setCode(last.getCode());
        parses.add(parse);
        return parse;
    }

    public Parse addWhile(){
        Parse parse = new Parse();
        parse.setId(id++);
        parse.setPlace("w"+(whileHolder++));
        parse.setType(Parse.parse_type.nd);
        parse.setProcessed(false);
        Map<String, Parse> data = getLastExpAndLastBlock(false);
        Parse exp = data.get("exp");
        Parse block = data.get("block1");
        parse.setNextLabel(exp.getFLabel());
        parse.setBeginLabel("L"+(labelHolder++));
        parse.setCode(parse.getBeginLabel()+": "+exp.getCode()+"\n"+exp.getTLabel()+": "+block.getCode()+
                (block.getNextLabel() != null ? "\n"+(block.getNextLabel()) + ": " : "") +
                "goto "+parse.getBeginLabel() + ";" +
                (block.getNextLabel() != null ? "\n"+(parse.getNextLabel()) + ": " : "") );
        parses.add(parse);
        return parse;
    }

    public Parse addIf(){
        Parse parse = new Parse();
        parse.setId(id++);
        parse.setPlace("I"+(ifHolder++));
        parse.setType(Parse.parse_type.nd);
        parse.setProcessed(false);
        Map<String, Parse> data = getLastExpAndLastBlock(false);
        Parse exp = data.get("exp");
        Parse block = data.get("block1");
        parse.setNextLabel(exp.getFLabel());
        parse.setCode(exp.getCode()+"\n"+exp.getTLabel()+": "+block.getCode()+ " " +
                exp.getFLabel()+": ");
        parses.add(parse);
        return parse;
    }

    public Parse addIfElse(){
        Parse parse = new Parse();
        parse.setId(id++);
        parse.setPlace("w"+(whileHolder++));
        parse.setType(Parse.parse_type.nd);
        parse.setProcessed(false);
        Map<String, Parse> data = getLastExpAndLastBlock(true);
        Parse exp = data.get("exp");
        Parse elseBlock = data.get("block1");
        Parse ifBlock = data.get("block2");
        parse.setNextLabel("L"+(labelHolder++));
        parse.setCode(exp.getCode()+"\n"+exp.getTLabel()+": "+ifBlock.getCode()+
                "goto " +parse.getNextLabel()+ ";\n"+
                exp.getFLabel()+": "+elseBlock.getCode()+
                parse.getNextLabel()+": ");
        parses.add(parse);
        return parse;
    }

    public Parse addCase(){
        Parse parse = new Parse();
        parse.setId(id++);
        parse.setPlace("c"+(caseHolder++));
        parse.setType(Parse.parse_type.nd);
        parse.setProcessed(false);
        Map<String, Parse> data = getLastCaseStuff();
        ArrayList<Parse> arrayList = getLastND(1);
        Parse caseValue = arrayList.get(0);
        parse.setNextLabel("L"+(labelHolder++));
        parse.setCode("");
        for (int i = data.size()/2 - 1; i >= 0; i--) {
            parse.setCode(parse.getCode()+"L"+(labelHolder++)+": "+
                    "if("+caseValue.getPlace()+"!="+(data.get("value"+i).getPlace())+") goto L"+(labelHolder)+";\n"+
                    data.get("element"+i).getCode()+
                    "goto "+parse.getNextLabel()+";\n"
                    );
        }
        ArrayList<Parse> lastND = getLastND(1);
        parse.setCode(((lastND != null) ? lastND.get(0).getCode() : "") +
                parse.getCode()+"L"+(labelHolder++)+": "+parse.getNextLabel()+": ");
        parses.add(parse);
        return parse;
    }

    public Parse addFor(String state){
        Parse parse = new Parse();
        return parse;
    }

    public Parse makeTheLastBlockCaseElement() {
        for (int i = parses.size() - 1 ; i >= 0; i--) {
            Parse check = parses.get(i);
            if(check.getType().equals(Parse.parse_type.block) && !check.isProcessed()) {
                check.setType(Parse.parse_type.caseElement);
                return check;
            }
        }
        return null;
    }

    private Map<String, Parse> getLastCaseStuff() {
        Map<String, Parse> res = new HashMap<>();
        int caseValue = 0;
        int caseElement = 0;
        for (int i = parses.size() - 1 ; i >= 0 ; i--) {
            Parse check = parses.get(i);
            if(check.getType().equals(Parse.parse_type.caseElement) && !check.isProcessed()) {
                check.setProcessed(true);
                res.put("element"+(caseElement++), check);
            }
            else if(check.getType().equals(Parse.parse_type.caseValue) && !check.isProcessed()) {
                check.setProcessed(true);
                res.put("value"+(caseValue++), check);
            }
        }
        return res;
    }

    public Parse addParam(String name) {
        Parse parse = new Parse();
        parse.setId(id++);
        parse.addToParams(name.trim());
        //parse.setPlace( function name ) ??
        parse.setType(Parse.parse_type.param);
        parse.setProcessed(false);
        parses.add(parse);
        return parse;
    }

    private Map<String, Parse> getLastExpAndLastBlock(boolean anotherBlock) {
        Map<String, Parse> res = new HashMap<>();
        boolean exp = false, block = false, block2 = false;
        int blockNum = 0;
        for (int i = parses.size() - 1 ; i >= 0; i--) {
            Parse check = parses.get(i);
            if(check.getType().equals(Parse.parse_type.exp) && !check.isProcessed() && !exp) {
                check.setProcessed(true);
                res.put("exp", check);
                exp = true;
            }
            else if(check.getType().equals(Parse.parse_type.block) && !check.isProcessed() && !block) {
                check.setProcessed(true);
                res.put("block"+(++blockNum), check);
                block = true;
            }
            else if(check.getType().equals(Parse.parse_type.block) && !check.isProcessed() && (anotherBlock && !block2)) {
                check.setProcessed(true);
                res.put("block"+(++blockNum), check);
                block2 = true;
            }
            if(exp && block) {
                if(anotherBlock)
                    if(block2)
                        return res;
                else
                    return res;
            }
        }
        return res;
    }

    private ArrayList<Parse> getLastND(int count) {
        ArrayList<Parse> res = new ArrayList<>();
        for (int i = parses.size() - 1 ; i >= 0 && res.size() != count ; i--) {
            Parse check = parses.get(i);
            if(check.getType().equals(Parse.parse_type.nd) && !check.isProcessed()) {
                check.setProcessed(true);
                res.add(check);
            }
        }
        return res;
    }

    public ArrayList<Parse> getLastNDNotTrue(int count) {
        ArrayList<Parse> res = new ArrayList<>();
        for (int i = parses.size() - 1 ; i >= 0 && res.size() != count ; i--) {
            Parse check = parses.get(i);
            if(check.getType().equals(Parse.parse_type.nd) && !check.isProcessed()) {
                res.add(check);
            }
        }
        return res;
    }

    private ArrayList<Parse> getLastParse() {
            ArrayList<Parse> res = new ArrayList<>();
            res.add(parses.get(parses.size() - 1));
        return res;
    }

    public boolean contains (String name) {
        for (Parse parse : parses) {
            if (parse.getPlace().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Parse> getParses() {
        return parses;
    }

    public int getFunctionInputSize(String name){
        for (int i = 0 ; i < allScopesParses.size() ; i++){
            for (int j = 0 ; j < allScopesParses.get(i).size() ; j++){
                if(allScopesParses.get(i).get(j).getFunctionName() != null && allScopesParses.get(i).get(j).getFunctionName().contains(name)){
                //contains nist vali ; dare tahesh nemidonam chera
                    return allScopesParses.get(i).get(j).getParamStack().size();
                }
            }
        }
        return 0;
//        System.out.println(allScopesParses.get(1));
    }

    public void print(){
        for (int i = 0 ; i < allScopesParses.size() ; i++){
            System.out.println(i + " : " + allScopesParses.get(i).get(allScopesParses.get(i).size()-1).getCode());
        }
//        System.out.println(allScopesParses.get(1));
    }

    public void printAnswer()  {
        File file = new File("labels.c");
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        printWriter.append("#include <stdio.h>\n" +
                "#include <stdlib.h>\n" +
                "#include <string.h>\n" +
                "\n" +
                "struct variable{\n" +
                "\tchar* id;\n" +
                "\tdouble value;\n" +
                "\tstruct variable* next;\n" +
                "};\n" +
                "\n" +
                "void setValue(struct variable** scope, char* id, double value);\n" +
                "double getValue(struct variable** scope, char* id);\n" +
                "char* createString(char* string);\n" +
                "\n" +
                "int main(){");
        System.out.println("#include <stdio.h>\n" +
                "#include <stdlib.h>\n" +
                "#include <string.h>\n" +
                "\n" +
                "struct variable{\n" +
                "\tchar* id;\n" +
                "\tdouble value;\n" +
                "\tstruct variable* next;\n" +
                "};\n" +
                "\n" +
                "void setValue(struct variable** scope, char* id, double value);\n" +
                "double getValue(struct variable** scope, char* id);\n" +
                "char* createString(char* string);\n" +
                "\n" +
                "int main(){");

        printWriter.append("\n" +
                "\tvoid* returnAddress;\n" +
                "double* top = (double*) malloc(1000 * sizeof(double));\n" +
                "\tvoid** rtop = (void**) malloc(1000 * sizeof(void*));\n" +
                "\tstruct variable** scopes = (struct variable**) malloc(100 * sizeof(struct variable*));\n" +
                "\t\n" +
                "\ttop += 1000;\n" +
                "\trtop += 1000;\n" +
                "\tscopes += 100;");
        System.out.println("\n" +
                "\tvoid* returnAddress;\n" +
                "double* top = (double*) malloc(1000 * sizeof(double));\n" +
                "\tvoid** rtop = (void**) malloc(1000 * sizeof(void*));\n" +
                "\tstruct variable** scopes = (struct variable**) malloc(100 * sizeof(struct variable*));\n" +
                "\t\n" +
                "\ttop += 1000;\n" +
                "\trtop += 1000;\n" +
                "\tscopes += 100;");

        printWriter.append("\tgoto MainFunction; \n");
        System.out.println("\tgoto MainFunction; \n");


        for (int i = 1 ; i < allScopesParses.size() ; i++){
            System.out.println(allScopesParses.get(i).get(allScopesParses.get(i).size()-1).getCode());
            printWriter.append(allScopesParses.get(i).get(allScopesParses.get(i).size()-1).getCode());
        }

        System.out.println("MainFunction : scopes = scopes - 1; " +allScopesParses.get(0).get(allScopesParses.get(0).size()-1).getCode());
        printWriter.append("MainFunction : scopes = scopes - 1; " +allScopesParses.get(0).get(allScopesParses.get(0).size()-1).getCode());

        System.out.println("return 0;" +
                "}");
        printWriter.append("return 0;" +
                "}");

        System.out.println("\n" +
                "void setValue(struct variable** scope, char* id, double value){\n" +
                "\tif(*scope == NULL){\n" +
                "\t\tstruct variable* newVar = (struct variable*) malloc(sizeof(struct variable));\n" +
                "\t\tnewVar->id = createString(id);\n" +
                "\t\tnewVar->value = value;\n" +
                "\t\tnewVar->next = NULL;\n" +
                "\t\t*scope = newVar;\n" +
                "\t}else{\n" +
                "\t\tstruct variable* node = *scope;\n" +
                "\t\twhile(node->next != NULL){\n" +
                "\t\t\tif(strcmp(node->id, id) == 0){\n" +
                "\t\t\t\tnode->value = value;\n" +
                "\t\t\t\treturn;\n" +
                "\t\t\t}\n" +
                "\t\t\tnode = node->next;\n" +
                "\t\t}\n" +
                "\t\tif(strcmp(node->id, id) == 0){\n" +
                "\t\t\tnode->value = value;\n" +
                "\t\t\treturn;\n" +
                "\t\t}\n" +
                "\t\tstruct variable* newVar = (struct variable*) malloc(sizeof(struct variable));\n" +
                "\t\tnewVar->id = createString(id);\n" +
                "\t\tnewVar->value = value;\n" +
                "\t\tnewVar->next = NULL;\n" +
                "\t\tnode->next = newVar;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "double getValue(struct variable** scope, char* id){\n" +
                "\twhile(1){\n" +
                "\t\tstruct variable* node = *scope;\n" +
                "\t\twhile(node != NULL){\n" +
                "\t\t\tif(strcmp(node->id, id) == 0){\n" +
                "\t\t\t\treturn node->value;\n" +
                "\t\t\t}\n" +
                "\t\t\tnode = node->next;\n" +
                "\t\t}\n" +
                "\t\tscope = scope + 1;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "char* createString(char* string){\n" +
                "\tchar* pointer = (char*) malloc(strlen(string) + 1);\n" +
                "\tstrcpy(pointer, string);\n" +
                "\tpointer[strlen(string)] = 0;\n" +
                "\treturn pointer;\n" +
                "}");
        printWriter.append("\n" +
                "void setValue(struct variable** scope, char* id, double value){\n" +
                "\tif(*scope == NULL){\n" +
                "\t\tstruct variable* newVar = (struct variable*) malloc(sizeof(struct variable));\n" +
                "\t\tnewVar->id = createString(id);\n" +
                "\t\tnewVar->value = value;\n" +
                "\t\tnewVar->next = NULL;\n" +
                "\t\t*scope = newVar;\n" +
                "\t}else{\n" +
                "\t\tstruct variable* node = *scope;\n" +
                "\t\twhile(node->next != NULL){\n" +
                "\t\t\tif(strcmp(node->id, id) == 0){\n" +
                "\t\t\t\tnode->value = value;\n" +
                "\t\t\t\treturn;\n" +
                "\t\t\t}\n" +
                "\t\t\tnode = node->next;\n" +
                "\t\t}\n" +
                "\t\tif(strcmp(node->id, id) == 0){\n" +
                "\t\t\tnode->value = value;\n" +
                "\t\t\treturn;\n" +
                "\t\t}\n" +
                "\t\tstruct variable* newVar = (struct variable*) malloc(sizeof(struct variable));\n" +
                "\t\tnewVar->id = createString(id);\n" +
                "\t\tnewVar->value = value;\n" +
                "\t\tnewVar->next = NULL;\n" +
                "\t\tnode->next = newVar;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "double getValue(struct variable** scope, char* id){\n" +
                "\twhile(1){\n" +
                "\t\tstruct variable* node = *scope;\n" +
                "\t\twhile(node != NULL){\n" +
                "\t\t\tif(strcmp(node->id, id) == 0){\n" +
                "\t\t\t\treturn node->value;\n" +
                "\t\t\t}\n" +
                "\t\t\tnode = node->next;\n" +
                "\t\t}\n" +
                "\t\tscope = scope + 1;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "char* createString(char* string){\n" +
                "\tchar* pointer = (char*) malloc(strlen(string) + 1);\n" +
                "\tstrcpy(pointer, string);\n" +
                "\tpointer[strlen(string)] = 0;\n" +
                "\treturn pointer;\n" +
                "}");
        printWriter.flush();
        printWriter.close();
    }

    public void setParses(ArrayList<Parse> parses) {
        this.parses = parses;
    }

}
