package postpc.huji.SandwichApp;

import android.text.InputFilter;
import android.text.Spanned;

class InputFilterPickles implements InputFilter {
    private final int minVal;
    private final int maxVal;

    public InputFilterPickles(int minVal, int maxVal){
        this.minVal = minVal;
        this.maxVal = maxVal;
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.subSequence(0, dstart).toString() + source + dest.subSequence(dend, dest.length()));
            if (isInRange(minVal, maxVal, input))
                return null;
        }
        catch (NumberFormatException nfe) {
        }
        return "";
    }
}