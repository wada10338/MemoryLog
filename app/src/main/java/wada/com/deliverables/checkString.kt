package wada.com.deliverables

public class checkString {
    fun canDataIn(title: String, contents: String): Boolean {
        return if ("" == title) {
            false
        } else {
            "" != contents
        }
    }
}