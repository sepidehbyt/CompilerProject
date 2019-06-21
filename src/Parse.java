import java.util.Stack;

public class Parse {

    enum parse_type {
        nd,
        exp,
        block,
        param
    }

    private int id;
    private String place;
    private parse_type type;
    private boolean processed;
    private String TLabel;
    private String FLabel;
    private String nextLabel;
    private String beginLabel;
    private String code;


    private String functionName;

    public Stack<String> getParamStack() {
        return paramStack;
    }

    private Stack<String> paramStack = new Stack<>();

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public parse_type getType() {
        return type;
    }

    public void setType(parse_type type) {
        this.type = type;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getTLabel() {
        return TLabel;
    }

    public void setTLabel(String TLabel) {
        this.TLabel = TLabel;
    }

    public String getFLabel() {
        return FLabel;
    }

    public void setFLabel(String FLabel) {
        this.FLabel = FLabel;
    }

    public String getNextLabel() {
        return nextLabel;
    }

    public void setNextLabel(String nextLabel) {
        this.nextLabel = nextLabel;
    }

    public String getBeginLabel() {
        return beginLabel;
    }

    public void setBeginLabel(String beginLabel) {
        this.beginLabel = beginLabel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void addToParams(String param){
        paramStack.push(param);
    }


    @Override
    public String toString() {
        return "Parse{" +
                "id=" + id +
                ", place='" + place + '\'' +
                ", type=" + type +
                ", processed=" + processed +
                ", TLabel=" + TLabel +
                ", FLabel=" + FLabel +
                ", nextLabel=" + nextLabel +
                ", beginLabel=" + beginLabel +
                ", code='" + code + '\'' +
                '}';
    }
}
