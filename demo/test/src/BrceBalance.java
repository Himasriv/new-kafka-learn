import java.util.HashMap;
import java.util.Map;

public class BrceBalance {
   // Balanced
   // str = "{[}]"; // Not Balanced
   // str = {()[]}  //Balanced((()))
   static void main() {
       String s = "{[(]}";
       boolean isbalanced = true;
       char[] ch = s.toCharArray();
       int top = -1;
       for (int i = 0; i < s.length(); i++) {
           if (ch[i] == '(' || ch[i] == '{' || ch[i] == '[') {

               ch[++top] = ch[i];
           }
           else if (ch[i] == ')' || ch[i] == '}' || ch[i] == ']') {


               if (top == -1) isbalanced=false;
               if ((ch[i] == ')' && ch[top] != '(') ||
                       (ch[i] == '}' && ch[top] != '{') ||
                       (ch[i] == ']' && ch[top] != '[')) {
                   isbalanced=false;
               }
               top--;
           }
       }

       // balanced if stack empty
       System.out.println(isbalanced);
   }
}
