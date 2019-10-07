package javax0.geci.test.tools.lexeger;

import javax0.geci.api.Segment;
import javax0.geci.api.Source;
import javax0.geci.engine.Geci;
import javax0.geci.tools.AbstractJavaGenerator;
import javax0.geci.tools.CaseTools;
import javax0.geci.tools.CompoundParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class TestGenerateLexpressionBuilderMethods extends AbstractJavaGenerator {

    @Test
    void createMethodsInLexpressionBuilder() throws IOException {
        final var geci = new Geci();
        Assertions.assertFalse(geci.source("../javageci-tools/src/main/java", "./javageci-tools/src/main/java")
                                   .register(new TestGenerateLexpressionBuilderMethods())
                                   .only("LexpressionBuilder.java")
                                   .generate(),
            geci.failed());
    }

    private static final String patternedMatchers = "identifier, character, string, type, comment";

    @Override
    public void process(Source source, Class<?> klass, CompoundParams global) throws Exception {
        Source lexpression = source.newSource("matchers/Lexpression.java");
        Segment segment = source.open("testGenerateLexpressionBuilderMethods");
        Segment expSegment = lexpression.open("methods");
        final var pattern = Pattern.compile("\\s*public\\s+LexMatcher\\s+(\\w+)\\((.*?)\\)\\s*\\{\\s*");
        for (final var line : lexpression.getLines()) {
            final var match = pattern.matcher(line);
            if (match.matches()) {
                final var methodName = match.group(1);
                final var argString = match.group(2);
                final var parameters = calculateExpressionCallParameterListString(argString);
                createMethod(segment, methodName, argString);
                if (argString.trim().length() == 0) {
                    expSegment.write_r("public LexMatcher " + methodName + "(GroupNameWrapper nameWrapper) {")
                        .write("return group(nameWrapper.toString()," + methodName + "());")
                        .write_l("}");
                }else if( !argString.contains("GroupNameWrapper")){
                    expSegment.write_r("public LexMatcher " + methodName + "(GroupNameWrapper nameWrapper, "+argString+") {")
                        .write("return group(nameWrapper.toString()," + methodName + "("+parameters+"));")
                        .write_l("}");
                }
            }
        }
        for (final var patterned : patternedMatchers.split(",")) {
            final var id = patterned.trim();
            final var matcher = CaseTools.ucase(id) + "Matcher";
            expSegment.param("id", id, "matcher", matcher);
            expSegment.write_r("public LexMatcher {{id}}() {")
                .write("return new {{matcher}}(this, javaLexed);")
                .write_l("}").newline();
            expSegment.write_r("public LexMatcher {{id}}(String text) {")
                .write("return new {{matcher}}(this, javaLexed, text);")
                .write_l("}").newline();
            expSegment.write_r("public LexMatcher {{id}}(Pattern pattern) {")
                .write("return new {{matcher}}(this, javaLexed, pattern);")
                .write_l("}").newline();
            expSegment.write_r("public LexMatcher {{id}}(String name, Pattern pattern) {")
                .write("return new {{matcher}}(this, javaLexed, pattern, name);")
                .write_l("}").newline();
        }
    }

    private void createMethod(final Segment segment, final String methodName, final String argString) {
        final var parameters = calculateBuilderCallParameterListString(argString);
        final var arguments = argString.replaceAll("LexMatcher\\s+matcher",
            "BiFunction<JavaLexed, Lexpression, LexMatcher> matcher")
                        .replaceAll("LexMatcher\\.\\.\\.\\s+matchers", "BiFunction<JavaLexed, Lexpression, LexMatcher>... matchers");
        segment.write_r("public static BiFunction<JavaLexed, Lexpression, LexMatcher> " + methodName + "(" + arguments + ") {");
        segment.write("return (jLex, e) -> e." + methodName + "(" + parameters + ");");
        segment.write_l("}");
        segment.newline();
    }

    /**
     * Calculate the call parameter list represented as a string from
     * the string of the arguments. The string of the arguments contains
     * the types and the parameter names comma separated. This method
     * removes the types.
     *
     * @param argString the comma separated list of arguments with the
     *                  types
     * @return the arguments comma separated without the types
     */
    private String calculateBuilderCallParameterListString(String argString) {
        return calculateCallParameterListString(argString, s -> s.startsWith("matcher") ? "X(" + s + ", jLex, e)" : s);
    }

    private String calculateExpressionCallParameterListString(String argString) {
        return calculateCallParameterListString(argString, s -> s);
    }

    private String calculateCallParameterListString(String argString, Function<String, String> transform) {
        return Arrays.stream(argString.split(",", -1))
                   .map(s -> s.trim().replaceAll("^.*?\\s+", ""))
                   .map(transform)
                   .collect(Collectors.joining(", "));
    }
}
