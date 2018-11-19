package br.edu.utfpr.cm.pasteanalyzer.entity;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegularExpression {
    private String name;
    private String expressionText;
    private Pattern expression;
    private double weight;

    /**
     * @param name
     * @param expression
     */
    public RegularExpression(String name, String expression) {
        this(name, expression, 0);
    }

    /**
     * @param name
     * @param expressionText
     * @param weight
     */
    public RegularExpression(String name, String expressionText, double weight) {
        super();
        this.name = name;
        this.expressionText = expressionText;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpressionText() {
        return expressionText;
    }

    public void setExpressionText(String expressionText) {
        this.expressionText = expressionText;
    }

    public Pattern getExpression() {
        return expression;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        long temp;
        temp = Double.doubleToLongBits(weight);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RegularExpression)) {
            return false;
        }
        RegularExpression other = (RegularExpression) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return this.name + ": " + this.weight;
    }

    public void compile() throws PatternSyntaxException{
            expression = Pattern.compile(expressionText, Pattern.CASE_INSENSITIVE);
    }
}